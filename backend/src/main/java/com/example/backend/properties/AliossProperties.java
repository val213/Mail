package com.example.backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="aliyunoss")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliossProperties
{
	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;
}
