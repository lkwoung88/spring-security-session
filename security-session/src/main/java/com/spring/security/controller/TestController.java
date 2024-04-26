package com.spring.security.controller;

import com.spring.security.response.BaseResponse;
import com.spring.security.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/api/pass")
    public BaseResponse pass() {
        return testService.pass();
    }

    @PostMapping("/api/none/pass")
    public BaseResponse nonePass() {
        return testService.nonePass();
    }
}
