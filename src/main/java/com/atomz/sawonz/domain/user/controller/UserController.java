package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.UsersDto.SignupRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupResponse;
import com.atomz.sawonz.domain.user.service.UsersService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public HttpCustomResponse<SignupResponse> signup(
            @RequestBody SignupRequest signupRequest
    ) {
        return new HttpCustomResponse<>(ResponseCode.SUCCESS, usersService.signup(signupRequest));
    }
}
