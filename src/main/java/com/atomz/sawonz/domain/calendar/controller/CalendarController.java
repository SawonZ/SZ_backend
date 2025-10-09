package com.atomz.sawonz.domain.calendar.controller;

import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarRequest;
import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarResponse;
import com.atomz.sawonz.domain.calendar.dto.CalendarDto.CalendarStatusUpdateRequest;
import com.atomz.sawonz.domain.calendar.service.CalendarService;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<CalendarResponse> createCalendar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid CalendarRequest calendarRequest
    ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.createCalendar(
                        principal.getEmail(),
                        calendarRequest
                )
        );
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<List<CalendarResponse>> getAllCalendars(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(name = "list", defaultValue = "all") String list
    ) {
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.listAllCalendars(
                        principal.getEmail(),
                        list
                )
        );
    }

    @DeleteMapping("/{calendarId}")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<String> deleteCalendar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long calendarId
        ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.deleteCalendar(
                        principal.getEmail(),
                        calendarId
                )
        );
    }

    @PutMapping("/{calendarId}")
    @PreAuthorize("isAuthenticated()")
    public HttpCustomResponse<CalendarResponse> updateCalendar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid CalendarRequest calendarRequest,
            @PathVariable Long calendarId
    ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarService.updateCalendar(
                        principal.getEmail(),
                        calendarRequest,
                        calendarId
                )
        );
    }

    @PatchMapping("/{calendarId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<CalendarResponse> statusUpdateCalendar(
            @RequestBody @Valid CalendarStatusUpdateRequest calendarStatusUpdateRequest,
            @PathVariable Long calendarId
    ) {
        if (calendarStatusUpdateRequest.getStatus() == null) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "일정 승인 여부 값이 없습니다.");
        }

        CalendarResponse calendarResponse;

        if (calendarStatusUpdateRequest.getStatus()) {
            calendarResponse = calendarService.statusApproved(calendarId);
        } else {
            calendarResponse = calendarService.statusRejected(calendarId);
        }

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                calendarResponse
        );
    }
}
