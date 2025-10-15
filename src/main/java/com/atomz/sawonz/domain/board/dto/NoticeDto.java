package com.atomz.sawonz.domain.board.dto;

import com.atomz.sawonz.domain.board.entity.NoticeEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NoticeDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUpdateRequest {

        @NotBlank
        @Size(max = 255)
        private String title;

        @NotBlank
        private String content;

        public static NoticeEntity toEntity(
                UsersEntity usersEntity,
                CreateUpdateRequest createUpdateRequest
        ) {
            return NoticeEntity.builder()
                    .title(createUpdateRequest.getTitle())
                    .content(createUpdateRequest.getContent())
                    .writer(usersEntity)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeListResponse {
        private Long noticeId;
        private String title;
        private String writerName;
        private LocalDateTime createdAt;

        public static NoticeListResponse fromEntity(NoticeEntity noticeEntity) {
            return NoticeListResponse.builder()
                    .noticeId(noticeEntity.getNoticeId())
                    .title(noticeEntity.getTitle())
                    .writerName(noticeEntity.getWriter().getUserName())
                    .createdAt(noticeEntity.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeDetailResponse {
        private Long noticeId;
        private String title;
        private String content;
        private String writerName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static NoticeDetailResponse fromEntity(NoticeEntity noticeEntity) {
            return NoticeDetailResponse.builder()
                    .noticeId(noticeEntity.getNoticeId())
                    .title(noticeEntity.getTitle())
                    .content(noticeEntity.getContent())
                    .writerName(noticeEntity.getWriter().getUserName())
                    .createdAt(noticeEntity.getCreatedAt())
                    .updatedAt(noticeEntity.getUpdatedAt())
                    .build();
        }
    }
}
