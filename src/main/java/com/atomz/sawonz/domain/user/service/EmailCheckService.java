package com.atomz.sawonz.domain.user.service;

import com.atomz.sawonz.domain.user.dto.EmailCheckDto.VerificationCodeCheckRequest;
import com.atomz.sawonz.domain.user.dto.EmailCheckDto.VerificationCodeSave;
import com.atomz.sawonz.domain.user.entity.EmailCheckEntity;
import com.atomz.sawonz.domain.user.repository.EmailCheckRepository;
import com.atomz.sawonz.global.exception.ErrorException;
import com.atomz.sawonz.global.exception.ResponseCode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailCheckService {

    private final EmailCheckRepository emailCheckRepository;
    private final JavaMailSender mailSender;

    private static final SecureRandom RANDOM = new SecureRandom();

    private String generate6DigitCode() {
        int n = RANDOM.nextInt(1_000_000);         // 0 ~ 999999
        return String.format("%06d", n);           // 6자리 0패딩
    }

    @Transactional
    public String sendVerificationCode(String email) {
        try {
            String code = generate6DigitCode();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[Sawonz] 이메일 인증번호");
            helper.setText("아래 인증번호를 3분 내에 입력해 주세요.\n\n인증번호: " + code);

            // 명시적으로 발신자 주소 설정
            helper.setFrom(new InternetAddress("no-reply@sawonz.com", "SawonZ"));

            mailSender.send(message);

            emailCheckRepository.findByEmail(email)
                    .ifPresentOrElse(
                            // 존재: code와 verified만 갱신 (updatedAt은 @PreUpdate로 자동)
                            existing -> {
                                existing.setVerificationCode(code);
                                existing.setVerified(false);
                                // save 호출 불필요: JPA dirty checking으로 업데이트됨
                            },
                            // 미존재: 신규 저장 (createdAt/updatedAt은 @PrePersist로 자동)
                            () -> emailCheckRepository.save(VerificationCodeSave.toEntity(email, code))
                    );

            return email + "로 인증번호가 정상적으로 발송되었습니다.";
        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }

    @Transactional
    public Boolean checkVaricationCode(VerificationCodeCheckRequest verificationCodeCheckRequest) {

        EmailCheckEntity emailCheckEntity = emailCheckRepository.findByEmail(verificationCodeCheckRequest.getEmail())
                .orElseThrow(() -> new ErrorException(ResponseCode.NOT_FOUND, "요청하신 Email로 보낸 인증번호가 확인되지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        if (emailCheckEntity.getUpdatedAt().plusSeconds(180).isBefore(now)) {
            throw new ErrorException(ResponseCode.VERIFICATION_CODE_EXPIRED, "인증번호 유효시간(3분)이 초과되었습니다.");
        }

        emailCheckEntity.setVerified(emailCheckEntity.getVerificationCode()
                .equals(verificationCodeCheckRequest.getVerificationCode()));

       return emailCheckEntity.getVerified();
    }
}
