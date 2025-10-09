package com.atomz.sawonz.domain.calendar.controller;

import com.atomz.sawonz.domain.calendar.dto.AttendanceDto.MyAttendanceResponse;
import com.atomz.sawonz.domain.calendar.service.AttendanceService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import com.atomz.sawonz.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/in")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<String> checkIn(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            HttpServletRequest request
    ) {

        String ip = ClientIpUtil.extractClientIp(request);
        System.out.println("ip = " + ip);

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                attendanceService.checkIn(principal.getEmail()));
    }

    @PostMapping("/out")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<String> checkOut(
            @AuthenticationPrincipal CustomUserPrincipal principal
//            HttpServletRequest request
    ) {
//        String ip = ClientIpUtil.extractClientIp(request);

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                attendanceService.checkOut(principal.getEmail()));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<List<MyAttendanceResponse>> me(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                attendanceService.me(principal.getEmail())
        );
    }


}
