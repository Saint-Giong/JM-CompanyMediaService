package rmit.saintgiong.companymediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyMediaRequestDto {
    @NotBlank
    private String mediaTitle;

    private String mediaDescription;

    private MediaType mediaType;

    private String mediaUrl;

    @NotBlank
    private String companyId;
}
