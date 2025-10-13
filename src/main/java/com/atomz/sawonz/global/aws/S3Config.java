package com.atomz.sawonz.global.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${app.aws.region}")
    private String region;

    @Value("${app.aws.credentials.access-key}")
    private String accessKey;

    @Value("${app.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        var creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(creds)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        var creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(creds)
                .build();
    }
}
