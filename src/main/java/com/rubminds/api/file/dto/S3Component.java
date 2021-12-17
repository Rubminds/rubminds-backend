package com.rubminds.api.file.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
@Component
public class S3Component {
    String bucket;
    String region;
    String publicUrl;
}
