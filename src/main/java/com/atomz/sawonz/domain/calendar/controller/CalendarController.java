package com.atomz.sawonz.domain.calendar.controller;

import com.atomz.sawonz.domain.calendar.dto.CalendarDto;
import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarCreateRequest;
import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarResponse;
import com.atomz.sawonz.domain.calendar.service.CalendarService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public HttpCustomResponse<CalendarResponse> createCalendar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid CalendarCreateRequest calendarCreateRequest) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.createCalendar(
                        principal.getEmail(),
                        calendarCreateRequest
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public HttpCustomResponse<List<CalendarResponse>> getAllCalendars(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(name = "list", defaultValue = "all") String list
    ) {
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.listAllCalendars(principal.getEmail(), list)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{calendarId}")
    public HttpCustomResponse<String> deleteCalendar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long calendarId
        ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.deleteCalendar(principal.getEmail(), calendarId)
        );
    }

}
