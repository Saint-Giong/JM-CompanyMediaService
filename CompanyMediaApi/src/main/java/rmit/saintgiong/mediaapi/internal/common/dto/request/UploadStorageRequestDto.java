package rmit.saintgiong.mediaapi.internal.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadStorageRequestDto {
    @NotBlank
    private String companyId;
}

