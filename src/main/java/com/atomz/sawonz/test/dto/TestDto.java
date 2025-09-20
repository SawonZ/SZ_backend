package com.atomz.sawonz.test.dto;

import com.atomz.sawonz.test.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TestDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCreateRequest {

        private String testName;

        public static TestEntity toEntity(
                TestCreateRequest testCreateRequest
        ) {
            return TestEntity.builder()
                    .testName(testCreateRequest.getTestName())
                    .build();

        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResponse {

        private Long testPk;
        private String testName;

        public static TestResponse fromEntity(
                TestEntity testEntity
        ) {
           return TestResponse.builder()
                   .testPk(testEntity.getTestPk())
                   .testName(testEntity.getTestName())
                   .build();
        }
    }
}
