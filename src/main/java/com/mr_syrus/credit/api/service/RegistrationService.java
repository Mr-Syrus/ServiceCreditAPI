package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.RegistrationDto;
import com.mr_syrus.credit.api.entity.*;
import com.mr_syrus.credit.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PersonalDataRepository personalDataRepository;
    private final RegistrationRepository registrationRepository;
    private final RoleRepository roleRepository;
    private final AuthorizationCodeRepository codeRepository;
    private final MailVerificationService mailService;
    private final SimplePasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
                               PersonalDataRepository personalDataRepository,
                               RegistrationRepository registrationRepository,
                               RoleRepository roleRepository,
                               AuthorizationCodeRepository codeRepository,
                               MailVerificationService mailService,
                               SimplePasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.registrationRepository = registrationRepository;
        this.roleRepository = roleRepository;
        this.codeRepository = codeRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegistrationDto dto) {
        //проверка уникальности
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByMail(dto.getMail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone already exists");
        }
        if (personalDataRepository.existsActiveByPassport(dto.getPassportSeries(), dto.getPassportNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passport already registered");
        }

        RoleEntity clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));

        //cоздание пользователя (неактивного до подтверждения)
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        UserEntity user = new UserEntity(
                dto.getUsername(),
                dto.getMail(),
                hashedPassword,
                dto.getPhone(),
                false, // active = false до подтверждения
                clientRole
        );
        user = userRepository.save(user);

        //запись персональных данных
        PersonalDataEntity personalData = new PersonalDataEntity(
                user,
                RosfinmonitoringStatus.NOT_RESTRICTED,
                dto.getPassportSeries(),
                dto.getPassportNumber(),
                dto.getPassportIssuedBy(),
                dto.getDepartmentCode(),
                dto.getPassportIssueDate(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getMiddleName(),
                GenderStatus.valueOf(dto.getGender().toUpperCase()),
                dto.getBirthDate(),
                dto.getInn(),
                dto.getSnils()
        );
        personalData.setActive(true);
        personalData = personalDataRepository.save(personalData);

        //запись прописки
        RegistrationStatus status = RegistrationStatus.valueOf(dto.getRegistrationType().toUpperCase());

        RegistrationEntity registration = new RegistrationEntity(
                personalData,
                dto.getRegistrationDate(),
                dto.getPostalIndex(),
                dto.getMigrationDepartment(),
                dto.getRegion(),
                dto.getDistrict(),
                dto.getCity(),
                dto.getStreet(),
                dto.getHouse(),
                dto.getFlat(),
                status
        );
        registration.setActive(true); // текущая, активная прописка
        registrationRepository.save(registration);

        //отправка кода подтверждения
        String code = mailService.sendVerificationCode(dto.getMail());
        AuthorizationCodeEntity authCode = new AuthorizationCodeEntity(code, user);
        codeRepository.save(authCode);

        return authCode.getId().toString();
    }

    @Transactional
    public void confirmRegistration(String codeIdStr, String code) {
        UUID codeId;
        try {
            codeId = UUID.fromString(codeIdStr);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid codeId");
        }

        AuthorizationCodeEntity authCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code not found"));

        if (!authCode.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(authCode.getDateTimeStart()) || now.isAfter(authCode.getDateTimeEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expired");
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
