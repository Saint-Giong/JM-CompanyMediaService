package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.domain.models.CompanyMedia;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaUpdateValidator;


import java.util.UUID;

import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;


@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaUpdateService implements UpdateCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    @Override
    @Transactional
    public void activateCompanyProfileImage(String mediaId) {
        log.info("method=activateCompanyProfileImage, message=Start activating company profile image, mediaId={}", mediaId);

        UUID mediaUuid = UUID.fromString(mediaId);

        CompanyMediaEntity toActivate = repository.findById(mediaUuid)
                .orElseThrow(() -> new DomainException(RESOURCE_NOT_FOUND,
                        "Company media with ID '" + mediaId + "' does not exist"));

        // Ensure only one active per company.
        repository.deactivateAllByCompanyId(toActivate.getCompanyId());

        toActivate.setActive(true);

        repository.save(toActivate);

        log.info("method=activateCompanyProfileImage, message=Successfully activated company profile image, mediaId={}", mediaId);
    }
}
