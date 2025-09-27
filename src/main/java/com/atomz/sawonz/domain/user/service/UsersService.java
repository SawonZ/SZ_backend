package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.UsersDto;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupRequest;
import com.atomz.sawonz.domain.user.dto.UsersDto.SignupResponse;
import com.atomz.sawonz.domain.user.entity.UserPrivateEntity;
import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.EmailCheckRepository;
import com.atomz.sawonz.domain.user.repository.UserProfileRepository;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsersDto.SignupResponse signup(SignupRequest signupRequest) {

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
                .salary("")
                .annualLeaveCount("")
                .positionTitle("")
                .hiredAt("")
                .resignedAt("")
                .build();
        userProfileRepository.save(profile);

        return SignupResponse.fromEntity(usersEntity);
    }
}
