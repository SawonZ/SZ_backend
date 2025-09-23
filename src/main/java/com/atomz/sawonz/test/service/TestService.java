package com.atomz.sawonz.test.service;

import com.atomz.sawonz.test.dto.TestDto.TestCreateRequest;
import com.atomz.sawonz.test.dto.TestDto.TestResponse;
import com.atomz.sawonz.test.repository.TestRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public TestResponse testCreate(TestCreateRequest testCreateRequest) {

        return TestResponse.fromEntity(
                testRepository.save(
                        TestCreateRequest.toEntity(testCreateRequest)
                )
        );
    }

    @Transactional(readOnly = true)
    public List<TestResponse> testList(){
        return testRepository.findAll().stream().map(TestResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("[Sawonz] 이메일 인증번호");
        msg.setText("아래 인증번호를 5분 내에 입력해 주세요.\n\n인증번호: " + code);
        mailSender.send(msg);
    }

}
