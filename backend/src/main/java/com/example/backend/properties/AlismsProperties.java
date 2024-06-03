package com.example.backend.properties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="aliyunsms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlismsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;
}

