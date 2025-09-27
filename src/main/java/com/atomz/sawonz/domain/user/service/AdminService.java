package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.entity.UsersEntity;
import com.atomz.sawonz.domain.user.repository.UsersRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsersRepository usersRepository;

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

}
