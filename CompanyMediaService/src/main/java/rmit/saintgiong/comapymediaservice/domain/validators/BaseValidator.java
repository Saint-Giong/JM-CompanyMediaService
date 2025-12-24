package rmit.saintgiong.comapymediaservice.domain.validators;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import rmit.saintgiong.companymediaapi.internal.common.type.DomainCode;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseValidator<T> {

    @Builder.Default
    protected List<String> errors = new ArrayList<>();

    protected void reject(String message) {
        errors.add(message);
    }

    protected boolean hasErrors() {
        return !errors.isEmpty();
    }

    protected void throwIfErrors() {
        if (hasErrors()) {
            throw new DomainException(DomainCode.INVALID_BUSINESS_LOGIC, String.join("; ", errors));
        }
    }

    public abstract void validate(T target);

    public abstract void validate(T target, UUID excludeId);
}
