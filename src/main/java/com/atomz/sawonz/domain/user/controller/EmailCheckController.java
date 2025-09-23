package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.EmailCheckDto.EmailSendRequest;
import com.atomz.sawonz.domain.user.service.EmailCheckService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailCheckController {

    private final EmailCheckService emailCheckService;

    @PostMapping("/send-code")
    public HttpCustomResponse<String> sendCode(@RequestBody EmailSendRequest emailSendRequest) {
        String msg = emailCheckService.sendVerificationCode(emailSendRequest.getEmail().trim().toLowerCase());
        return new HttpCustomResponse<>(ResponseCode.SUCCESS, msg);
    }
}
