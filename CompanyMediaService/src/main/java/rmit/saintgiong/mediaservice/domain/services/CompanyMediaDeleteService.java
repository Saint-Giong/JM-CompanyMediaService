package rmit.saintgiong.mediaservice.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rmit.saintgiong.mediaservice.common.exception.domain.DomainException;
import rmit.saintgiong.mediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.mediaapi.internal.services.DeleteCompanyMediaInterface;

import java.util.UUID;

import static rmit.saintgiong.mediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyMediaDeleteService implements DeleteCompanyMediaInterface {

    private final CompanyMediaRepository repository;

    @Override
    @Transactional
    public void deleteCompanyMedia(String mediaId) {
        log.info("method=deleteCompanyMedia, message=Start deleting company media, mediaId={}", mediaId);

        UUID id = UUID.fromString(mediaId);

        if (!repository.existsById(id)) {
            throw new DomainException(RESOURCE_NOT_FOUND,
                    "Company media with ID '" + mediaId + "' does not exist");
        }

        repository.deleteById(id);

        log.info("method=deleteCompanyMedia, message=Successfully deleted company media, mediaId={}", mediaId);
    }
}

