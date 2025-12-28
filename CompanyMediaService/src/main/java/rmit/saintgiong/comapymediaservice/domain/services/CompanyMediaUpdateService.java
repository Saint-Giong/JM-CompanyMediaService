package rmit.saintgiong.comapymediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;

import java.util.UUID;

import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;


@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaUpdateService implements UpdateCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    @Override
    @Transactional
    public void updateCompanyMedia(String mediaId, UpdateCompanyMediaRequestDto request, byte[] bytes, String contentType, String originalFilename) {
        log.info("method=updateCompanyMedia, message=Start updating company media, mediaId={} ", mediaId);

        UUID mediaUuid = UUID.fromString(mediaId);

        CompanyMediaEntity existing = repository.findById(mediaUuid)
                .orElseThrow(() -> new DomainException(RESOURCE_NOT_FOUND,
                        "Company media with ID '" + mediaId + "' does not exist"));

        // Update meta fields
        existing.setMediaTitle(request.getMediaTitle());
        existing.setMediaDescription(request.getMediaDescription());

        // Allow explicit nulls
        existing.setMediaType(request.getMediaType() == null ? null : request.getMediaType().name());
        existing.setMediaUrl(request.getMediaUrl());

        repository.save(existing);

        log.info("method=updateCompanyMedia, message=Successfully updated company media, mediaId={}", mediaId);
    }
}
