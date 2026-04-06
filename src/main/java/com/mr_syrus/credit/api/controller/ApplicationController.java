package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.repository.ApplicationRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    public ApplicationController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }
}
