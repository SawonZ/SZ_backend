package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.UsersDto.MyCoworkerInfoResponse;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoResponse;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupResponse;
import com.atomz.sawonz.domain.user.service.UsersService;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                usersService.signup(signupRequest)
        );
    }

    @GetMapping("/my-info")
    public HttpCustomResponse<MyInfoResponse> myInfo(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        if (principal == null) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                usersService.myInfo(principal.getEmail())
        );
    }

    @GetMapping("/coworkers")
    public HttpCustomResponse<List<MyCoworkerInfoResponse>> coworker(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        if (principal == null) {
            throw new ErrorException(ResponseCode.TOKEN_INVALID);
        }

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                usersService.coworkerList()
        );
    }
}
