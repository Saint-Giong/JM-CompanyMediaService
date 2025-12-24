package rmit.saintgiong.comapymediaservice.common.exception;

import rmit.saintgiong.companymediaapi.internal.common.type.DomainCode;

public class DomainException extends RuntimeException {

    private final DomainCode code;

    public DomainException(DomainCode code, Object... args) {
        super(String.format(code.getMessageTemplate(), args));
        this.code = code;
    }

    public DomainCode getDomainCode() {
        return code;
    }
}
