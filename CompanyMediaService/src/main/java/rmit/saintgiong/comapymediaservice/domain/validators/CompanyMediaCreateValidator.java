package rmit.saintgiong.comapymediaservice.domain.validators;

import org.springframework.stereotype.Component;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;

@Component
public class CompanyMediaCreateValidator extends CompanyMediaBaseValidator<CreateCompanyMediaMetaRequestDto> {

    protected CompanyMediaCreateValidator(CompanyMediaRepository repository) {
        super(repository);
    }

    @Override
    public void validate(CreateCompanyMediaMetaRequestDto dto) {
        errors.clear();

        // TODO: Add business rules here if needed.

        throwIfErrors();
    }
}
