package rmit.saintgiong.mediaservice.domain.services.external.storage;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;
import rmit.saintgiong.mediaapi.internal.services.UploadStorageInterface;
import rmit.saintgiong.mediaapi.external.services.kafka.EventProducerInterface;
import rmit.saintgiong.mediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.mediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.shared.dto.avro.media.UpdateLogoRequestRecord;
import rmit.saintgiong.shared.type.KafkaTopic;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaUploadStorageService implements UploadStorageInterface {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/png", "image/jpeg", "image/jpg", "image/webp");
    private static final long MAX_LOGO_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

    private final ObjectStorageService objectStorageService;
    private final GcsStorageProperties gcsProps;
    private final EventProducerInterface eventProducerInterface;

    @Override
    public UploadStorageResponseDto uploadCompanyMedia(String companyId, byte[] bytes, String contentType,
            String originalFilename) {
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

            String signedUrl = objectStorageService
                    .signUrl(savedObjectName, Duration.ofMinutes(gcsProps.getSignedUrlTtlMinutes()))
                    .toString();

            return UploadStorageResponseDto.builder()
                    .isSuccess(true)
                    .url(signedUrl)
                    .build();
        } catch (Exception ex) {
            log.warn("method=uploadCompanyMedia, message=Upload failed, companyId={}, error={}", companyId,
                    ex.toString());
            return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
        }
    }

    @Override
    public UploadStorageResponseDto uploadCompanyLogo(String companyId, byte[] bytes, String contentType,
            String originalFilename) {
        try {
            UUID companyUuid = UUID.fromString(companyId);

            // Validate file exists
            if (bytes == null || bytes.length == 0) {
                log.warn("method=uploadCompanyLogo, message=Empty file, companyId={}", companyId);
                return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
            }

            // Validate file size (max 2MB)
            if (bytes.length > MAX_LOGO_SIZE_BYTES) {
                log.warn("method=uploadCompanyLogo, message=File too large, companyId={}, size={}", companyId,
                        bytes.length);
                return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
            }

            // Validate content type is an image
            String normalizedContentType = contentType != null ? contentType.toLowerCase(Locale.ROOT) : "";
            if (!ALLOWED_IMAGE_TYPES.contains(normalizedContentType)) {
                log.warn("method=uploadCompanyLogo, message=Invalid content type, companyId={}, contentType={}",
                        companyId, contentType);
                return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
            }

            String ext = inferExtension(originalFilename, contentType);
            // Store logos in a dedicated path: company/{companyId}/logo/
            String objectName = normalizePrefix(gcsProps.getUploadPrefix())
                    + "company/" + companyUuid + "/logo/"
                    + "logo-" + Instant.now().toEpochMilli()
                    + (ext.isBlank() ? "" : "." + ext);

            String savedObjectName = objectStorageService.upload(objectName, bytes, contentType);
            log.info("method=uploadCompanyLogo, message=Uploaded logo, companyId={}, objectName={}", companyId,
                    savedObjectName);

            // Use longer TTL for logos (they're displayed often)
            String signedUrl = objectStorageService
                    .signUrl(savedObjectName, Duration.ofDays(7))
                    .toString();

            // Send Kafka event to Profile Service
            try {
                UpdateLogoRequestRecord event = UpdateLogoRequestRecord
                        .newBuilder()
                        .setCompanyId(companyId)
                        .setLogoUrl(signedUrl)
                        .build();

                eventProducerInterface.send(
                        KafkaTopic.JM_UPDATE_LOGO_REQUEST_TOPIC,
                        event
                );
                log.info("method=uploadCompanyLogo, message=Sent logo update event, companyId={}", companyId);
            } catch (Exception ex) {
                log.error("method=uploadCompanyLogo, message=Failed to send Kafka event, companyId={}, error={}",
                        companyId, ex.getMessage());
                // Non-blocking - continue even if Kafka fails
            }

            return UploadStorageResponseDto.builder()
                    .isSuccess(true)
                    .url(signedUrl)
                    .build();
        } catch (IllegalArgumentException ex) {
            log.warn("method=uploadCompanyLogo, message=Invalid companyId, companyId={}", companyId);
            return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
        } catch (Exception ex) {
            log.warn("method=uploadCompanyLogo, message=Upload failed, companyId={}, error={}", companyId,
                    ex.toString());
            return UploadStorageResponseDto.builder().isSuccess(false).url(null).build();
        }
    }

    private static String normalizePrefix(String prefix) {
        if (prefix == null || prefix.isBlank())
            return "";
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    private static String inferExtension(String originalFilename, String contentType) {
        if (originalFilename != null) {
            int idx = originalFilename.lastIndexOf('.');
            if (idx >= 0 && idx < originalFilename.length() - 1) {
                return originalFilename.substring(idx + 1).toLowerCase(Locale.ROOT);
            }
        }
        if (contentType == null)
            return "";
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
