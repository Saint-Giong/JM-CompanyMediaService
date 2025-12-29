package rmit.saintgiong.comapymediaservice.domain.validators;

import org.springframework.stereotype.Component;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;

@Component
public class CompanyMediaCreateValidator extends CompanyMediaBaseValidator<CreateCompanyMediaRequestDto> {

    protected CompanyMediaCreateValidator(CompanyMediaRepository repository) {
        super(repository);
    }

    @Override
    public void validate(CreateCompanyMediaRequestDto dto) {
        errors.clear();

        // TODO: Add business rules here if needed.

        throwIfErrors();
    }
}
