package rmit.saintgiong.mediaservice.common.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "companymedia.gcs")
public class GcsStorageProperties {
    /** Bucket name (required). */
    private String bucket;

    /** Prefix applied to all uploads, e.g. "company-media/". */
    private String uploadPrefix = "company-media/";

    /** Signed URL TTL in minutes. */
    private long signedUrlTtlMinutes = 30;
}

