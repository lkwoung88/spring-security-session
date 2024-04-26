package com.spring.security.service;

import com.spring.code.ResponseCode;
import com.spring.security.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    public BaseResponse pass() {
        return new BaseResponse(ResponseCode.SUCCESS);
    }

    public BaseResponse nonePass() {
        return new BaseResponse(ResponseCode.SUCCESS);
    }
}
