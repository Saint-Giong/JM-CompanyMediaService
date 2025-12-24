package rmit.saintgiong.companymediaapi.internal.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryCompanyMediaResponseDto {
    private String id;
    private String mediaTitle;
    private String mediaDescription;
    private String mediaType;
    private String mediaPath;
    private String companyId;
    private boolean active;
}
