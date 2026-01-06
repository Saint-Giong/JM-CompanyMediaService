package rmit.saintgiong.mediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmit.saintgiong.mediaapi.internal.common.type.MediaType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyMediaRequestDto {
    @NotBlank
    private String mediaTitle;

    private String mediaDescription;

    private MediaType mediaType;

    private String mediaUrl;
}
