package com.spring.security.response;

import com.spring.code.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BaseResponse{
    public LoginResponse(ResponseCode responseCode) {
        super(responseCode);
    }
}
