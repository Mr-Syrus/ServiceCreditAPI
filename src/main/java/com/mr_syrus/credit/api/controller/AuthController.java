package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.entity.SessionEntity;
import com.mr_syrus.credit.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static class DtoSendCode {
        private String passportSeries;
        private String passportNumber;
        private String mail;

        public String getPassportSeries() { return passportSeries; }
        public void setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }
        public String getPassportNumber() { return passportNumber; }
        public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
    }

    public static class DtoAuth {
        private String code;
        private String codeId;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getCodeId() { return codeId; }
        public void setCodeId(String codeId) { this.codeId = codeId; }
    }

    @PostMapping("/send_code")
    public String sendCode(@RequestBody DtoSendCode dto) {
        return authService.sendCode(dto.getMail(), dto.getPassportSeries(), dto.getPassportNumber());
    }

    @PostMapping("/auth")
    public String auth(@RequestBody DtoAuth dto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        SessionEntity session = authService.authenticate(dto.getCodeId(), dto.getCode(), ipAddress, userAgent);

        Cookie cookie = new Cookie("session", session.getSessionKey());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 2); // 2 часа
        response.addCookie(cookie);

        return "OK";
    }
}