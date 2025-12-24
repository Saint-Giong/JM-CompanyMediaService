package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.comapymediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.comapymediaservice.domain.models.CompanyMedia;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaCreateService implements CreateCompanyMediaInterface {

    private final CompanyMediaMapper mapper;

    private final CompanyMediaRepository repository;

    private final CompanyMediaCreateValidator createValidator;

    @Override
    @Transactional
    public CreateCompanyMediaResponseDto createCompanyMedia(CreateCompanyMediaRequestDto request) {
        log.info("method=createCompanyMedia, message=Start creating company profile, param={}", request);

        // Validate business rules
        createValidator.validate(request);

        // Map from creation request DTO to domain model
        CompanyMedia newCompanyMedia = mapper.fromCreateCompanyProfileCommand(request);

        // Map from domain model to persistence entity
        CompanyMediaEntity companyMediaEntity = mapper.toEntity(newCompanyMedia);

        // Persist the new company profile
        CompanyMediaEntity savedEntity = repository.save(companyMediaEntity);

        log.info("method=createCompanyMedia, message=Successfully created company media, id={}", savedEntity.getId());

        // Build and return the response DTO
        return CreateCompanyMediaResponseDto.builder()
                .id(String.valueOf(savedEntity.getId()))
                .build();
    }
}
