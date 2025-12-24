package rmit.saintgiong.comapymediaservice.domain.validators;

import org.springframework.stereotype.Component;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;

import java.util.UUID;

@Component
public class CompanyMediaUpdateValidator extends CompanyMediaBaseValidator<UpdateCompanyMediaRequestDto> {

    public CompanyMediaUpdateValidator(CompanyMediaRepository repository) {
        super(repository);
    }

    @Override
    public void validate(UpdateCompanyMediaRequestDto dto, UUID existingId) {
        errors.clear();


        throwIfErrors();
    }
}
