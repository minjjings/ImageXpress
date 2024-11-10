package image.module.cdn.service;

import image.module.cdn.dto.ImageResponseDto;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdnService {

    private final RedisService redisService;


    // @Value는 static 변수에 주입되지 않는다고 함
    @Value("${server.port}")
    private String port;

    @Value("${cdn.image.path}")
    public String filePath;

    @Value("${cdn.image.url}")
    public String fileUrl;

    public String getPartCdnUrl() {
        return "http://" + fileUrl + ":" + port + "/cdn/";
    }


    }



