package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.RegistrationEmployeeDto;
import com.mr_syrus.credit.api.entity.AuthorizationCodeEntity;
import com.mr_syrus.credit.api.entity.RoleEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.AuthorizationCodeRepository;
import com.mr_syrus.credit.api.repository.RoleRepository;
import com.mr_syrus.credit.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmployeeService {
    public final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorizationCodeRepository codeRepository;
    private final MailVerificationService mailService;
    private final SimplePasswordEncoder passwordEncoder;

    public EmployeeService(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AuthorizationCodeRepository codeRepository,
                           MailVerificationService mailService,
                           SimplePasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.codeRepository = codeRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegistrationEmployeeDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByMail(dto.getMail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        RoleEntity role = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        UserEntity user = new UserEntity(
                dto.getUsername(),
                dto.getMail(),
                hashedPassword,
                false, // active = false до подтверждения
                role
        );
        user = userRepository.save(user);
        String code = mailService.sendVerificationCode(dto.getMail());
        AuthorizationCodeEntity authCode = new AuthorizationCodeEntity(code, user);
        codeRepository.save(authCode);

        return authCode.getId().toString();
    }

    @Transactional
    public void confirmRegistration(CodeVerificationDto dto) {
        UUID codeId;
        try {
            codeId = UUID.fromString(dto.getCodeId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid codeId");
        }

        AuthorizationCodeEntity authCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(authCode.getDateTimeStart()) || now.isAfter(authCode.getDateTimeEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expired");
        }

        if (!authCode.getCode().equals(dto.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }


        UserEntity user = authCode.getUser();
        if (user.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already confirmed");
        }
        user.setActive(true);
        userRepository.save(user);

        codeRepository.delete(authCode);
    }
}
