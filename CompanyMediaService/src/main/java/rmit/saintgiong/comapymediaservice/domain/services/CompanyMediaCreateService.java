package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.comapymediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaCreateService implements CreateCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    private final CompanyMediaCreateValidator createValidator;

    private final ObjectStorageService objectStorageService;

    private final GcsStorageProperties gcsProps;

    @Override
    @Transactional
    public CreateCompanyMediaResponseDto createCompanyMedia(CreateCompanyMediaMetaRequestDto meta, byte[] bytes, String contentType, String originalFilename) {
        log.info("method=createCompanyMedia(upload), message=Start uploading company media, meta={}", meta);

        // Validate business rules on meta
        createValidator.validate(meta);

        UUID companyId = UUID.fromString(meta.getCompanyId());
        String ext = inferExtension(originalFilename, contentType);
        String objectName = normalizePrefix(gcsProps.getUploadPrefix())
                + "company/" + companyId + "/"
                + UUID.randomUUID() + "-" + Instant.now().toEpochMilli()
                + (ext.isBlank() ? "" : "." + ext);

        String savedObjectName = objectStorageService.upload(objectName, bytes, contentType);

        CompanyMediaEntity entity = CompanyMediaEntity.builder()
                .mediaTitle(meta.getMediaTitle())
                .mediaDescription(meta.getMediaDescription())
                .mediaType(meta.getMediaType().name())
                .mediaUrl(savedObjectName)
                .companyId(companyId)
                .active(false)
                .build();

        CompanyMediaEntity saved = repository.save(entity);
        log.info("method=createCompanyMedia(upload), message=Successfully uploaded company media, id={}, objectName={}", saved.getId(), savedObjectName);

        return CreateCompanyMediaResponseDto.builder()
                .id(String.valueOf(saved.getId()))
                .build();
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
