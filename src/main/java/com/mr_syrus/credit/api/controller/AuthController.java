package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.AuthorizationCodeEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.AuthorizationCodeRepository;
import com.mr_syrus.credit.api.repository.UserRepository;
import com.mr_syrus.credit.api.service.MailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final MailVerificationService mailVerificationService;
    private final AuthorizationCodeRepository authorizationCodeRepository;
    public AuthController(UserRepository userRepository, MailVerificationService mailVerificationService, AuthorizationCodeRepository authorizationCodeRepository) {
        this.userRepository = userRepository;
        this.mailVerificationService = mailVerificationService;
        this.authorizationCodeRepository = authorizationCodeRepository;
    }

    public static class DtoAuthWithPassportAndMail {
        private String passportSeries;
        private String passportNumber;
        private String mail;
    }

    @PostMapping("/send_code")
    public String sendCode(@RequestBody DtoAuthWithPassportAndMail dto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findBySeriesAndNumber(dto.passportSeries, dto.passportNumber);
        if (optionalUserEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        UserEntity userEntity = optionalUserEntity.get();

        if (userEntity.getMail().equals(dto.mail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail not found");
        }

        String code = mailVerificationService.sendVerificationCode(dto.mail);

        AuthorizationCodeEntity authorizationCodeEntity = new AuthorizationCodeEntity(code, userEntity);
        authorizationCodeRepository.save(authorizationCodeEntity);

        return authorizationCodeEntity.getId().toString();
    }
}
