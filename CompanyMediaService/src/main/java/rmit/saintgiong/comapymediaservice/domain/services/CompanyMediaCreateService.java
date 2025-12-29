package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaCreateService implements CreateCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    private final CompanyMediaCreateValidator createValidator;

    @Override
    @Transactional
    public CreateCompanyMediaResponseDto createCompanyMedia(CreateCompanyMediaRequestDto meta, byte[] bytes, String contentType, String originalFilename) {
        log.info("method=createCompanyMedia, message=Start creating company media, meta={}", meta);

        // Validate business rules on meta
        createValidator.validate(meta);

        UUID companyId = UUID.fromString(meta.getCompanyId());

        CompanyMediaEntity entity = CompanyMediaEntity.builder()
                .mediaTitle(meta.getMediaTitle())
                .mediaDescription(meta.getMediaDescription())
                .mediaType(meta.getMediaType() == null ? null : meta.getMediaType().name())
                .mediaUrl(meta.getMediaUrl())
                .companyId(companyId)
                .build();

        CompanyMediaEntity saved = repository.save(entity);
        log.info("method=createCompanyMedia, message=Successfully created company media, id={}", saved.getId());

        return CreateCompanyMediaResponseDto.builder()
                .id(String.valueOf(saved.getId()))
                .build();
    }
}
