package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.AdminDto.UserInfoRequest;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserResignRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoResponse;
import com.atomz.sawonz.domain.user.entity.UserPrivateEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UserPrivateRepository;
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
public class AdminService {

    private final UsersRepository usersRepository;
    private final UserPrivateRepository userPrivateRepository;

    @Transactional
    public String userStatus(String email) {

        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        if (user.getStatus()) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "이미 승인된 사용자입니다.");
        }

        user.setStatus(true);

        usersRepository.save(user);

        return email + " 승인 되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<MyInfoResponse> userList(){

        List<UsersEntity> users = usersRepository.findAll();

        List<MyInfoResponse> myInfoResponseList = new ArrayList<>();

        for (UsersEntity user : users) {
            MyInfoResponse myInfoResponse = MyInfoResponse.fromEntity(user);
            myInfoResponseList.add(myInfoResponse);
        }

        return myInfoResponseList;
    }

    @Transactional
    public MyInfoResponse userUpdateInfo(
            UserInfoRequest userInfoRequest
    ) {
        if(userInfoRequest.getEmail() == null || userInfoRequest.getEmail().isEmpty()) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "email 값은 필수 입니다.");
        }

        boolean hasSalary = userInfoRequest.getSalary() != null;
        boolean hasAnnualLeaveCount = userInfoRequest.getAnnualLeaveCount() != null;
        boolean hasPositionTitle = userInfoRequest.getPositionTitle() != null;
        boolean hasHiredAt = userInfoRequest.getHiredAt() != null;

        if (!hasSalary && !hasAnnualLeaveCount && !hasPositionTitle && !hasHiredAt) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "수정하려는 값이 최소 하나 이상 존재해야 합니다.");
        }

        UsersEntity usersEntity = usersRepository.findByEmail(userInfoRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        UserPrivateEntity userPrivateEntity = usersEntity.getUserPrivate();

        if(hasSalary) {
            userPrivateEntity.setSalary(userInfoRequest.getSalary());
        }

        if(hasAnnualLeaveCount) {
            userPrivateEntity.setAnnualLeaveCount(userInfoRequest.getAnnualLeaveCount());
        }

        if(hasPositionTitle) {
            userPrivateEntity.setPositionTitle(userInfoRequest.getPositionTitle());
        }

        if(hasHiredAt) {
            userPrivateEntity.setHiredAt(userInfoRequest.getHiredAt());
        }

        return MyInfoResponse.fromEntity(usersEntity);
    }

    @Transactional
    public MyInfoResponse userResign(
            UserResignRequest userResignRequest
    ) {
        if(userResignRequest.getEmail() == null || userResignRequest.getEmail().isEmpty()) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "email 값은 필수 입니다.");
        }
        if (userResignRequest.getResigned() == null) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "퇴직 여부 값은 필수 입니다.");
        }

        UsersEntity usersEntity = usersRepository.findByEmail(userResignRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        if (usersEntity.getRole() == UsersEntity.Role.ROLE_ADMIN) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "ADMIN은 퇴직처리할 수 없습니다.");
        }

        UserPrivateEntity userPrivateEntity = usersEntity.getUserPrivate();

        if (userResignRequest.getResigned()) {
            if(userResignRequest.getResignedAt() == null) {
                throw new ErrorException(ResponseCode.BAD_REQUEST, "퇴직 처리시 퇴직일 값은 필수 입니다.");
            }
            userPrivateEntity.setResignedAt(userResignRequest.getResignedAt());
        } else {
            userPrivateEntity.setResignedAt(null);
        }

        return MyInfoResponse.fromEntity(usersEntity);
    }

}
