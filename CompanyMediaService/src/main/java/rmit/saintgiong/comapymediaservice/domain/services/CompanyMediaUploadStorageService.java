package rmit.saintgiong.comapymediaservice.domain.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.comapymediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.UploadStorageResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.UploadStorageInterface;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaUploadStorageService implements UploadStorageInterface {

    private final ObjectStorageService objectStorageService;
    private final GcsStorageProperties gcsProps;

    @Override
    public UploadStorageResponseDto uploadCompanyMedia(String companyId, byte[] bytes, String contentType, String originalFilename) {
        try {
            UUID companyUuid = UUID.fromString(companyId);
            if (bytes == null || bytes.length == 0) {
                return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
            }

            String ext = inferExtension(originalFilename, contentType);
            String objectName = normalizePrefix(gcsProps.getUploadPrefix())
                    + "company/" + companyUuid + "/"
                    + UUID.randomUUID() + "-" + Instant.now().toEpochMilli()
                    + (ext.isBlank() ? "" : "." + ext);

            String savedObjectName = objectStorageService.upload(objectName, bytes, contentType);
            log.info("method=uploadCompanyMedia, message=Uploaded object, objectName={}", savedObjectName);

            return UploadStorageResponseDto.builder()
                    .isSuccess(true)
                    .url(savedObjectName)
                    .build();
        } catch (Exception ex) {
            log.warn("method=uploadCompanyMedia, message=Upload failed, companyId={}, error={}", companyId, ex.toString());
            return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
        }
    }

    private static String normalizePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) return "";
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    private static String inferExtension(String originalFilename, String contentType) {
        if (originalFilename != null) {
            int idx = originalFilename.lastIndexOf('.');
            if (idx >= 0 && idx < originalFilename.length() - 1) {
                return originalFilename.substring(idx + 1).toLowerCase(Locale.ROOT);
            }
        }
        if (contentType == null) return "";
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            case "video/mp4" -> "mp4";
            case "video/webm" -> "webm";
            default -> "";
        };
    }
}

