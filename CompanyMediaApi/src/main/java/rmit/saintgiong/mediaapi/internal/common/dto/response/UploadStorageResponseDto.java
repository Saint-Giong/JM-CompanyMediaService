package rmit.saintgiong.mediaapi.internal.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadStorageResponseDto {
    private String url;
    private boolean isSuccess;
}

