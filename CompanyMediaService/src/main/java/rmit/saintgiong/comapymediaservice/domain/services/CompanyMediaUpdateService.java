package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.comapymediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;


@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaUpdateService implements UpdateCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    private final ObjectStorageService objectStorageService;

    private final GcsStorageProperties gcsProps;

    @Override
    @Transactional
    public void updateCompanyMedia(String mediaId, UpdateCompanyMediaRequestDto request, byte[] bytes, String contentType, String originalFilename) {
        boolean hasUpload = bytes != null && bytes.length > 0;
        log.info("method=updateCompanyMedia, message=Start updating company media, mediaId={}, hasUpload={}", mediaId, hasUpload);

        UUID mediaUuid = UUID.fromString(mediaId);

        CompanyMediaEntity existing = repository.findById(mediaUuid)
                .orElseThrow(() -> new DomainException(RESOURCE_NOT_FOUND,
                        "Company media with ID '" + mediaId + "' does not exist"));


        // Update meta fields
        existing.setMediaTitle(request.getMediaTitle());
        existing.setMediaDescription(request.getMediaDescription());

        // Allow explicit null: if client passes null, we clear it.
        existing.setMediaType(request.getMediaType() == null ? null : request.getMediaType().name());

        if (hasUpload) {
            String ext = inferExtension(originalFilename, contentType);
            String objectName = normalizePrefix(gcsProps.getUploadPrefix())
                    + "company/" + existing.getCompanyId() + "/"
                    + UUID.randomUUID() + "-" + Instant.now().toEpochMilli()
                    + (ext.isBlank() ? "" : "." + ext);

            String savedObjectName = objectStorageService.upload(objectName, bytes, contentType);
            // Persist uploaded object reference
            existing.setMediaUrl(savedObjectName);

            log.info("method=updateCompanyMedia, message=Uploaded replacement objectName={}", savedObjectName);
        } else {
            if (request.getMediaType() == null) {
                existing.setMediaUrl(null);
            }
        }

        repository.save(existing);

        log.info("method=updateCompanyMedia, message=Successfully updated company media, mediaId={}", mediaId);
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
