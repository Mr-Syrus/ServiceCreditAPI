package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @PostMapping("/send_code")
    public String sendCode(@RequestBody DtoAuthWithPassportAndMail dto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findBySeriesAndNumber(dto.passportSeries, dto.passportNumber);
        if (optionalUserEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        UserEntity userEntity = optionalUserEntity.get();

        //добавить отправку

        return "code ok";
    }
}
