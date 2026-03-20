package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.UserRepository;
import com.mr_syrus.credit.api.service.MailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
public class UserController {

    private final UserRepository userRepository;
    //конструктор который указывает что usercontroller хочет userRepository
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static class DtoAuthWithPassportAndMail {
        private String passportSeries;
        private String passportNumber;
        private String mail;
    }

    @Autowired
    private MailVerificationService mailService;

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

        String code = mailService.generateVericationCode();

        //добавить отправку

        return "code ok";
    }
}
