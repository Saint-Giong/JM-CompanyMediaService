package rmit.saintgiong.mediaservice.common.storage;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GcsStorageProperties.class)
public class GcsStorageConfig {

    @Bean
    public Storage gcsStorageClient() {
        // Uses Application Default Credentials (GOOGLE_APPLICATION_CREDENTIALS for local dev)
        return StorageOptions.getDefaultInstance().getService();
    }
}

