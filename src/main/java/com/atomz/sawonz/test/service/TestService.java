package com.atomz.sawonz.test.service;

import com.atomz.sawonz.test.dto.TestDto.TestCreateRequest;
import com.atomz.sawonz.test.dto.TestDto.TestResponse;
import com.atomz.sawonz.test.repository.TestRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

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

}
