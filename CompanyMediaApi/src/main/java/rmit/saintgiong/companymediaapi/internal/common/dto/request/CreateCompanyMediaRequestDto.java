package rmit.saintgiong.companymediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

@Data
@Builder
public class CreateCompanyMediaRequestDto {
    @NotBlank
    private String mediaTitle;

    private String mediaDescription;

    @NotNull
    private MediaType mediaType;

    @NotBlank
    private String mediaPath;

    @NotBlank
    private String companyId;
}