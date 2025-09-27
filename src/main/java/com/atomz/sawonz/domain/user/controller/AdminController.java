package com.atomz.sawonz.domain.user.controller;

import static com.atomz.sawonz.domain.user.entity.UsersEntity.Role.ROLE_ADMIN;
import static com.atomz.sawonz.domain.user.entity.UsersEntity.Role.ROLE_MANAGER;
import static com.atomz.sawonz.domain.user.entity.UsersEntity.Role.ROLE_MEMBER;

import com.atomz.sawonz.domain.user.dto.AdminDto.UserStatusRequest;
import com.atomz.sawonz.domain.user.service.AdminService;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<String> userStatus(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody UserStatusRequest userStatusRequest
    ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userStatus(userStatusRequest.getEmail())
        );

    }

}
