package rmit.saintgiong.companymediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyMediaMetaRequestDto {
    @NotBlank
    private String mediaTitle;

    private String mediaDescription;

    @NotNull
    private MediaType mediaType;

    @NotBlank
    private String companyId;
}
