package com.atomz.sawonz.domain.board.service;

import com.atomz.sawonz.domain.board.dto.NoticeDto.CreateUpdateRequest;
import com.atomz.sawonz.domain.board.dto.NoticeDto.NoticeDetailResponse;
import com.atomz.sawonz.domain.board.dto.NoticeDto.NoticeListResponse;
import com.atomz.sawonz.domain.board.entity.NoticeEntity;
import com.atomz.sawonz.domain.board.repository.NoticeRepository;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public NoticeDetailResponse noticeCreate(
            String email,
            CreateUpdateRequest createUpdateRequest
    ) {
        UsersEntity writer = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        NoticeEntity noticeEntity = noticeRepository.save(
                CreateUpdateRequest.toEntity(writer, createUpdateRequest)
        );

        return NoticeDetailResponse.fromEntity(noticeEntity);
    }

    @Transactional
    public NoticeDetailResponse noticeUpdate(String email, Long noticeId, CreateUpdateRequest createUpdateRequest) {

        NoticeEntity entity = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND, "공지글을 찾을 수 없습니다."));

        if (!entity.getWriter().getEmail().equals(email)) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "작성자만 수정할 수 있습니다.");
        }

        entity.setTitle(createUpdateRequest.getTitle());
        entity.setContent(createUpdateRequest.getContent());

        return NoticeDetailResponse.fromEntity(entity);
    }

    @Transactional(readOnly = true)
    public List<NoticeListResponse> noticeList() {

        List<NoticeEntity> noticeEntityList = noticeRepository.findAll();

        List<NoticeListResponse> noticeListResponseList = new ArrayList<>();

        for (NoticeEntity noticeEntity : noticeEntityList) {
            noticeListResponseList.add(NoticeListResponse.fromEntity(noticeEntity));
        }

        return noticeListResponseList;
    }

    @Transactional
    public boolean noticeDelete(String email, Long noticeId) {
        NoticeEntity entity = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND, "공지글을 찾을 수 없습니다."));

        if (!entity.getWriter().getEmail().equals(email)) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "작성자만 삭제할 수 있습니다.");
        }

        noticeRepository.delete(entity);

        return true;
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse noticeDetail(Long noticeId) {

        NoticeEntity noticeEntity = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND, "공지글을 찾을 수 없습니다."));

        return NoticeDetailResponse.fromEntity(noticeEntity);
    }
}
