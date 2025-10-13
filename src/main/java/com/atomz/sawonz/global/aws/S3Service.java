package com.atomz.sawonz.global.aws;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3;

    @Value("${app.aws.s3.bucket}")
    private String bucket;

    @Value("${app.aws.region}")
    private String region;

    @Value("${app.aws.s3.public-base-url:}")
    private String publicBaseUrl;

    public String uploadImg(String userId, MultipartFile file) throws IOException {
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload";
        String ext = extractExt(original);
        String key = "profileImg/%s/%s.%s".formatted(userId, UUID.randomUUID(), ext);
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        s3.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return buildUrl(key);
    }

    public void deleteByUrl(String url) {
        String key = extractKeyFromUrl(url);
        deleteByKey(key);
    }

    public void deleteByKey(String key) {
        if (key == null || key.isBlank()) return;
        var req = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3.deleteObject(req);
    }

    private String extractExt(String name) {
        int idx = name.lastIndexOf('.');
        String candidate = idx > -1 ? name.substring(idx + 1) : "bin";
        return candidate.toLowerCase();
    }

    private String buildUrl(String key) {
        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8).replace("+", "%20");
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return "%s/%s".formatted(trimSlash(publicBaseUrl), key);
        }
        return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, encodedKey);
    }

    private String trimSlash(String base) {
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    private String extractKeyFromUrl(String url) {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("URL이 비어 있습니다.");
        URI u = URI.create(url);
        String host = u.getHost();
        String rawPath = u.getRawPath();             // 인코딩된 원본 path
        String path = rawPath != null ? rawPath : "/";

        // 1) 가상호스트형: https://{bucket}.s3.{region}.amazonaws.com/{key}
        if (host != null && host.startsWith(bucket + ".s3")) {
            String rawKey = path.startsWith("/") ? path.substring(1) : path;
            return urlDecode(rawKey);
        }

        // 2) 경로형: https://s3.{region}.amazonaws.com/{bucket}/{key}
        //    또는 https://s3.amazonaws.com/{bucket}/{key}
        String[] parts = path.split("/", 3); // ["", bucket, key...]
        if (parts.length >= 3 && parts[1].equals(bucket)) {
            return urlDecode(parts[2]);
        }

        // 3) CloudFront/커스텀 도메인: publicBaseUrl 이 prefix일 때
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            String base = trimSlash(publicBaseUrl);
            if (url.startsWith(base + "/")) {
                String rawKey = url.substring((base + "/").length());
                return urlDecode(rawKey);
            }
        }

        // 4) 마지막 시도: path의 선행 '/' 제거 후 디코드
        String fallback = path.startsWith("/") ? path.substring(1) : path;
        if (!fallback.isBlank()) return urlDecode(fallback);

        throw new IllegalArgumentException("URL에서 S3 object key를 추출할 수 없습니다.");
    }

    private String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}
