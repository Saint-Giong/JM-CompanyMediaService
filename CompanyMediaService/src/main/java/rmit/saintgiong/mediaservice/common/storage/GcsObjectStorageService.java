package rmit.saintgiong.mediaservice.common.storage;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GcsObjectStorageService implements ObjectStorageService {

    private final Storage storage;
    private final GcsStorageProperties props;

    @Override
    public String upload(String objectName, byte[] bytes, String contentType) {
        String bucket = Objects.requireNonNull(props.getBucket(), "companymedia.gcs.bucket must be set");
        if (bucket.isBlank()) {
            throw new IllegalStateException("companymedia.gcs.bucket must be set");
        }

        BlobInfo.Builder builder = BlobInfo.newBuilder(bucket, objectName);
        if (contentType != null && !contentType.isBlank()) {
            builder.setContentType(contentType);
        }

        storage.create(builder.build(), bytes);
        return objectName;
    }

    @Override
    public URL signUrl(String objectName, Duration ttl) {
        String bucket = Objects.requireNonNull(props.getBucket(), "companymedia.gcs.bucket must be set");
        if (bucket.isBlank()) {
            throw new IllegalStateException("companymedia.gcs.bucket must be set");
        }
        long seconds = Math.max(1, ttl.getSeconds());

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket, objectName).build();
        return storage.signUrl(blobInfo, seconds, TimeUnit.SECONDS, SignUrlOption.withV4Signature());
    }
}

