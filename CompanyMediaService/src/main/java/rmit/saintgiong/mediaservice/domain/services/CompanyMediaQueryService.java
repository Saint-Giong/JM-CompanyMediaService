package rmit.saintgiong.mediaservice.domain.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.mediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.mediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.mediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.mediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.mediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.mediaservice.domain.validators.CompanyMediaBaseValidator;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.mediaapi.internal.services.QueryCompanyMediaInterface;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaQueryService implements QueryCompanyMediaInterface {

    private final CompanyMediaMapper mapper;

    private final CompanyMediaBaseValidator<Void> baseValidator;

    private final CompanyMediaRepository companyProfileRepository;

    private final ObjectStorageService objectStorageService;

    private final GcsStorageProperties gcsProps;

    @Override
    public QueryCompanyMediaResponseDto getCompanyMedia(String id) {
        log.info("method=getCompanyMedia, message=Start fetching company details, id={}", id);

        UUID uuid = UUID.fromString(id);

        // validate existence
        CompanyMediaEntity existing = baseValidator.assertExistsById(uuid);

        // map to response DTO
        QueryCompanyMediaResponseDto response = mapper.toQueryResponse(existing);
        response.setMediaPath(signOrNull(existing.getMediaUrl()));

        log.info("method=getCompanyMedia, message=Successfully fetched company details, id={}", id);

        return response;
    }

    @Override
    public QueryCompanyMediaListResponseDto listCompanyMediaByCompany(String companyId) {
        log.info("method=listCompanyMediaByCompany, message=Start listing company media, companyId={}", companyId);

        UUID companyUuid = UUID.fromString(companyId);

        QueryCompanyMediaListResponseDto response = QueryCompanyMediaListResponseDto.builder()
                .items(companyProfileRepository.findAllByCompanyId(companyUuid).stream()
                        .map(entity -> {
                            QueryCompanyMediaResponseDto dto = mapper.toQueryResponse(entity);
                            dto.setMediaPath(signOrNull(entity.getMediaUrl()));
                            return dto;
                        })
                        .collect(Collectors.toList()))
                .build();

        log.info("method=listCompanyMediaByCompany, message=Successfully listed company media, companyId={}", companyId);
        return response;
    }

    private String signOrNull(String objectName) {
        if (objectName == null || objectName.isBlank()) return null;
        return objectStorageService
                .signUrl(objectName, Duration.ofMinutes(gcsProps.getSignedUrlTtlMinutes()))
                .toString();
    }
}
