package rmit.saintgiong.comapymediaservice.domain.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaBaseValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.QueryCompanyMediaInterface;

import java.util.UUID;
import java.util.stream.Collectors;

import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;


@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaQueryService implements QueryCompanyMediaInterface {

    private final CompanyMediaMapper mapper;

    private final CompanyMediaBaseValidator<Void> baseValidator;

    private final CompanyMediaRepository companyProfileRepository;

    @Override
    public QueryCompanyMediaResponseDto getCompanyMedia(String id) {
        log.info("method=getCompanyMedia, message=Start fetching company details, id={}", id);

        UUID uuid = UUID.fromString(id);

        // validate existence
        CompanyMediaEntity existing = baseValidator.assertExistsById(uuid);

        // map to response DTO
        QueryCompanyMediaResponseDto response = mapper.toQueryResponse(existing);

        log.info("method=getCompanyMedia, message=Successfully fetched company details, id={}", id);

        return response;
    }

    @Override
    public QueryCompanyMediaListResponseDto listCompanyMediaByCompany(String companyId) {
        log.info("method=listCompanyMediaByCompany, message=Start listing company media, companyId={}", companyId);

        UUID companyUuid = UUID.fromString(companyId);

        QueryCompanyMediaListResponseDto response = QueryCompanyMediaListResponseDto.builder()
                .items(companyProfileRepository.findAllByCompanyId(companyUuid).stream()
                        .map(mapper::toQueryResponse)
                        .collect(Collectors.toList()))
                .build();

        log.info("method=listCompanyMediaByCompany, message=Successfully listed company media, companyId={}", companyId);
        return response;
    }

    @Override
    public QueryCompanyMediaResponseDto getActiveCompanyProfileImage(String companyId) {
        log.info("method=getActiveCompanyProfileImage, message=Start fetching active company profile image, companyId={}", companyId);

        UUID companyUuid = UUID.fromString(companyId);

        CompanyMediaEntity active = companyProfileRepository.findFirstByCompanyIdAndActiveTrue(companyUuid)
                .orElseThrow(() -> new DomainException(RESOURCE_NOT_FOUND,
                        "Active company profile image not found for companyId '" + companyId + "'"));

        QueryCompanyMediaResponseDto response = mapper.toQueryResponse(active);

        log.info("method=getActiveCompanyProfileImage, message=Successfully fetched active company profile image, companyId={}", companyId);
        return response;
    }
}
