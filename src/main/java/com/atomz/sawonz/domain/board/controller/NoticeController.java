package com.atomz.sawonz.domain.board.controller;

import com.atomz.sawonz.domain.board.dto.NoticeDto.CreateUpdateRequest;
import com.atomz.sawonz.domain.board.dto.NoticeDto.NoticeDetailResponse;
import com.atomz.sawonz.domain.board.dto.NoticeDto.NoticeListResponse;
import com.atomz.sawonz.domain.board.service.NoticeService;
import com.atomz.sawonz.global.exception.HttpCustomResponse;
import com.atomz.sawonz.global.exception.ResponseCode;
import com.atomz.sawonz.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<NoticeDetailResponse> noticeCreate(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid CreateUpdateRequest request
    ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                noticeService.noticeCreate(principal.getEmail(), request)
        );
    }

    @PutMapping("/{noticeId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<NoticeDetailResponse> noticeUpdate(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long noticeId,
            @RequestBody @Valid CreateUpdateRequest request
    ) {
        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                noticeService.noticeUpdate(principal.getEmail(), noticeId, request)
        );
    }

    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpCustomResponse<String> noticeDelete(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long noticeId
    ) {
        boolean deleted = noticeService.noticeDelete(principal.getEmail(), noticeId);
        if (deleted) {
            return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                "공지사항이 정상적으로 삭제되었습니다."
        );} else {
            return new HttpCustomResponse<>(
                    ResponseCode.BAD_REQUEST,
                    "공지사항 삭제 요청이 실패하였습니다."
            );
        }

    }

    @GetMapping
    public HttpCustomResponse<List<NoticeListResponse>> noticeList() {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                noticeService.noticeList()
        );
    }

    @GetMapping("/{noticeId}")
    public HttpCustomResponse<NoticeDetailResponse> noticeDetail(
            @PathVariable Long noticeId
    ) {

        return new HttpCustomResponse<>(
                ResponseCode.SUCCESS,
                noticeService.noticeDetail(noticeId)
        );
    }
}
