package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.UserRepository;
import com.mr_syrus.credit.api.service.MailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
public class UserController {

    private final UserRepository userRepository;
    private final MailVerificationService mailVerificationService;
    public UserController(UserRepository userRepository, MailVerificationService mailVerificationService) {
        this.userRepository = userRepository;
        this.mailVerificationService = mailVerificationService;
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

        if (userEntity.getMail() != dto.mail){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail not found");
        }

        String code = mailVerificationService.sendVerificationCode(dto.mail);


        return "code 200";
    }
}
