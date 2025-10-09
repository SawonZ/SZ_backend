package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.calendar.dto.AttendanceDto.MyAttendanceResponse;
import com.atomz.sawonz.domain.calendar.entity.AttendanceEntity;
import com.atomz.sawonz.domain.calendar.repository.AttendanceRepository;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserInfoRequest;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserResignRequest;
import com.atomz.sawonz.domain.user.dto.AdminDto.UserStatusRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoResponse;
import com.atomz.sawonz.domain.user.entity.UserPrivateEntity;
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
public class AdminService {

    private final UsersRepository usersRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public String userStatus(UserStatusRequest userStatusRequest) {

        UsersEntity user = usersRepository.findByEmail(userStatusRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        user.setStatus(userStatusRequest.getStatus());

        if (userStatusRequest.getStatus()) {
            return userStatusRequest.getEmail() + " 가입요청이 승인되었습니다.";
        } else {
            return userStatusRequest.getEmail() + " 가입요청이 거절되었습니다.";
        }
    }

    @Transactional(readOnly = true)
    public List<MyInfoResponse> userList(){

        List<UsersEntity> users = usersRepository.findAll();

        List<MyInfoResponse> myInfoResponseList = new ArrayList<>();

        for (UsersEntity usersEntity : users) {
            MyInfoResponse myInfoResponse = MyInfoResponse.fromEntity(
                    usersEntity,
                    myAttendanceResponseList(usersEntity)
            );
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

        return MyInfoResponse.fromEntity(
                usersEntity,
                myAttendanceResponseList(usersEntity)
        );
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

        return MyInfoResponse.fromEntity(
                usersEntity,
                myAttendanceResponseList(usersEntity)
        );
    }

    private List<MyAttendanceResponse> myAttendanceResponseList(UsersEntity usersEntity) {

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findByUser(usersEntity);

        List<MyAttendanceResponse> myAttendanceResponseList = new ArrayList<>();
        for (AttendanceEntity attendanceEntity : attendanceEntities) {
            myAttendanceResponseList.add(MyAttendanceResponse.fromEntity(attendanceEntity));
        }

        return myAttendanceResponseList;
    }

}
