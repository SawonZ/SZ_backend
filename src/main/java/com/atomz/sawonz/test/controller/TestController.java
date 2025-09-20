package com.atomz.sawonz.test.controller;

import com.atomz.sawonz.test.dto.TestDto.TestCreateRequest;
import com.atomz.sawonz.test.dto.TestDto.TestResponse;
import com.atomz.sawonz.test.service.TestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("")
    public TestResponse testCreate(
            @RequestBody TestCreateRequest testCreateRequest
    ){
        return testService.testCreate(testCreateRequest);
    }

    @GetMapping("")
    public List<TestResponse> testList(){
        return testService.testList();
    }
}
