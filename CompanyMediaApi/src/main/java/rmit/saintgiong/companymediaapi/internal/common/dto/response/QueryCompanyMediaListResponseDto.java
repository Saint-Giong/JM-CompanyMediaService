package rmit.saintgiong.companymediaapi.internal.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryCompanyMediaListResponseDto {
    private List<QueryCompanyMediaResponseDto> items;
}

