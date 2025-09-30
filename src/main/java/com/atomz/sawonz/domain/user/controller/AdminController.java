package com.atomz.sawonz.domain.user.controller;

import com.atomz.sawonz.domain.user.dto.AdminDto.UserInfoRequest;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserResignRequest;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserStatusRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoResponse;
import com.atomz.sawonz.domain.user.service.AdminService;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/user/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<String> userStatus(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid UserStatusRequest userStatusRequest
    ) {

        if (Objects.equals(principal.getEmail(), userStatusRequest.getEmail())) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "자신의 아이디는 요청할 수 없습니다.");
        }

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userStatus(userStatusRequest)
        );
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<List<MyInfoResponse>> userList(
    ) {
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userList()
        );
    }

    @PatchMapping("/user/info")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public HttpCustomResponse<MyInfoResponse> userUpdateInfo(
            @RequestBody @Valid UserInfoRequest userInfoRequest
    ){
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userUpdateInfo(userInfoRequest)

        );
    }

    @PatchMapping("/user/resign")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public HttpCustomResponse<MyInfoResponse> userResign(
            @RequestBody @Valid UserResignRequest userResignRequest
    ){
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                adminService.userResign(userResignRequest)
        );
    }

}
