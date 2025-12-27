package rmit.saintgiong.companymediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyMediaRequestDto {
    @NotBlank
    private String mediaTitle;

    private String mediaDescription;

    private MediaType mediaType;
}
