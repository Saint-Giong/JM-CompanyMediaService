package rmit.saintgiong.comapymediaservice.common.storage;

import java.net.URL;
import java.time.Duration;

public interface ObjectStorageService {

    /**
     * Uploads bytes to object storage.
     *
     * @param objectName   destination object name (path inside the bucket)
     * @param bytes        content
     * @param contentType  MIME content type (may be null)
     * @return the saved objectName (stable reference)
     */
    String upload(String objectName, byte[] bytes, String contentType);

    /**
     * Creates a temporary signed URL for downloading from object storage.
     */
    URL signUrl(String objectName, Duration ttl);
}

