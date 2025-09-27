package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.AdminDto.UserStatusRequest;
import com.atomz.sawonz.domain.user.service.AdminService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/user-status")
    public HttpCustomResponse<String> userStatus(
            @RequestBody UserStatusRequest userStatusRequest
    ) {
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userStatus(userStatusRequest.getEmail())
        );

    }

}
