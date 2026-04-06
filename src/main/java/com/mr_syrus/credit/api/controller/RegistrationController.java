package com.mr_syrus.credit.api.controller;

import ch.qos.logback.core.Context;
import com.mr_syrus.credit.api.dto.RegistrationDto;
import com.mr_syrus.credit.api.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;
    //контроллер для внедрения зависимости
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public String register( @RequestBody RegistrationDto dto) {
        return registrationService.register(dto);
    }
}
