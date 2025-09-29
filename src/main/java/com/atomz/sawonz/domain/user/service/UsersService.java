package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.UsersDto.MyCoworkerInfoResponse;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoResponse;
import com.atomz.sawonz.domain.user.dto.UsersDto.MyInfoUpdateRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupResponse;
import com.atomz.sawonz.domain.user.entity.UserPrivateEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.EmailCheckRepository;
import com.atomz.sawonz.domain.user.repository.UserPrivateRepository;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserPrivateRepository userPrivateRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        // 이메일 검증 여부 확인
        if (!emailCheckRepository.existsByEmailAndVerifiedTrue(signupRequest.getEmail())) {
            throw new ErrorException(ResponseCode.EMAIL_VERIFICATION_FAILED);
        }
        // 이메일 사용 여부 확인
        if (usersRepository.existsByEmail(signupRequest.getEmail())) {
        throw new ErrorException(ResponseCode.EMAIL_ALREADY_EXISTS);
        }
        // 연락처 사용 여부 확인
        if (usersRepository.existsByPhone(signupRequest.getPhone())) {
            throw new ErrorException(ResponseCode.PHONE_ALREADY_EXISTS);
        }

        UsersEntity usersEntity = usersRepository.save(SignupRequest.toEntity(
                signupRequest, passwordEncoder.encode(signupRequest.getPassword())));

        // 가입 직후 프로필 빈값으로 생성
        UserPrivateEntity profile = UserPrivateEntity.builder()
                .user(usersEntity)
                .address("")
                .salary(0)
                .annualLeaveCount(0)
                .positionTitle("사원")
                .hiredAt(null)
                .resignedAt(null)
                .build();
        userPrivateRepository.save(profile);

        return SignupResponse.fromEntity(usersEntity);
    }

    @Transactional(readOnly = true)
    public MyInfoResponse myInfo(String email) {

        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        return MyInfoResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public List<MyCoworkerInfoResponse> coworkerList(){

        List<UsersEntity> users = usersRepository.findAll();

        List<MyCoworkerInfoResponse> myCoworkerInfoResponseList = new ArrayList<>();

        for (UsersEntity user : users) {
            MyCoworkerInfoResponse myCoworkerInfoResponse = MyCoworkerInfoResponse.fromEntity(user);
            myCoworkerInfoResponseList.add(myCoworkerInfoResponse);
        }

        return myCoworkerInfoResponseList;
    }

    @Transactional
    public MyInfoResponse updateMyInfo(String email, MyInfoUpdateRequest myInfoUpdateRequest) {

        if (
                (myInfoUpdateRequest.getPhone() == null || myInfoUpdateRequest.getPhone().isEmpty()) &&
                        (myInfoUpdateRequest.getAddress() == null || myInfoUpdateRequest.getAddress().isEmpty())) {
            throw new ErrorException(ResponseCode.BAD_REQUEST, "수정하려는 값이 최소 하나이상 존재해야합니다.");
        }

        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND_USER));

        if (myInfoUpdateRequest.getPhone() != null && !myInfoUpdateRequest.getPhone().isEmpty()) {
            user.setPhone(myInfoUpdateRequest.getPhone());
        }

        if (myInfoUpdateRequest.getAddress() != null && !myInfoUpdateRequest.getAddress().isEmpty()) {
            UserPrivateEntity userPrivateEntity = user.getUserPrivate();
            userPrivateEntity.setAddress(myInfoUpdateRequest.getAddress());
        }

        return MyInfoResponse.fromEntity(user);
    }
}
