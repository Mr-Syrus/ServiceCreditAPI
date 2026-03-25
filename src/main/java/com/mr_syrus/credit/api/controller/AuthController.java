package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.AuthorizationCodeEntity;
import com.mr_syrus.credit.api.entity.SessionEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.AuthorizationCodeRepository;
import com.mr_syrus.credit.api.repository.SessionRepository;
import com.mr_syrus.credit.api.repository.UserRepository;
import com.mr_syrus.credit.api.service.MailVerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;


@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final MailVerificationService mailVerificationService;
    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final SessionRepository sessionRepository;

    public AuthController(
            UserRepository userRepository,
            MailVerificationService mailVerificationService,
            AuthorizationCodeRepository authorizationCodeRepository,
            SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.mailVerificationService = mailVerificationService;
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.sessionRepository = sessionRepository;
    }

    public static class DtoSendCode {
        private String passportSeries;
        private String passportNumber;
        private String mail;
    }

    public static class DtoAuth {
        private String code;
        private String codeId;
    }

    @PostMapping("/send_code")
    public String sendCode(@RequestBody DtoSendCode dto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findBySeriesAndNumber(dto.passportSeries, dto.passportNumber);
        if (optionalUserEntity.isEmpty()) {
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

    @PostMapping("/auth")
    public String auth(@RequestBody DtoAuth dto, HttpServletRequest request,
                     HttpServletResponse response) {
        UUID codeId;
        try {
            codeId = UUID.fromString(dto.codeId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid codeId");
        }

        Optional<AuthorizationCodeEntity> optionalAuthorizationCodeEntity = authorizationCodeRepository.findById(codeId);
        if (optionalAuthorizationCodeEntity.isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        AuthorizationCodeEntity authorizationCodeEntity = optionalAuthorizationCodeEntity.get();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        SessionEntity sessionEntity = new SessionEntity(authorizationCodeEntity.getUser(), ipAddress, userAgent);
        sessionRepository.save(sessionEntity);

        Cookie cookie = new Cookie("session", sessionEntity.getSessionKey());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 2);

        response.addCookie(cookie);

        return "200 ok";
    }
}
