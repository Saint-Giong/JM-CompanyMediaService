package rmit.saintgiong.mediaservice.domain.validators;

import org.springframework.stereotype.Component;
import rmit.saintgiong.mediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.mediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.mediaapi.internal.common.type.DomainCode;
import rmit.saintgiong.mediaservice.common.exception.domain.DomainException;
import java.util.UUID;

@Component
public class CompanyMediaBaseValidator<T> extends BaseValidator<T> {

    protected final CompanyMediaRepository repository;

    protected CompanyMediaBaseValidator(CompanyMediaRepository repository) {
        this.repository = repository;
    }

    public CompanyMediaEntity assertExistsById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new DomainException(DomainCode.RESOURCE_NOT_FOUND,
                        "Company profile with ID '" + id + "' does not exist"));
    }

    @Override
    public void validate(T target) {
    }

    @Override
    public void validate(T target, UUID excludeId) {

    }
}
