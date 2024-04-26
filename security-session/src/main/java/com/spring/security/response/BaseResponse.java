package com.spring.security.response;

import com.spring.code.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {

    private String status;
    private String message;

    public BaseResponse() {
    }

    public BaseResponse(ResponseCode responseCode) {
        this.status = responseCode.getStatus();
        this.message = responseCode.getMessage();
    }
}
