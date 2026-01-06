package rmit.saintgiong.mediaapi.internal.services;

import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

public interface QueryCompanyMediaInterface {
    QueryCompanyMediaResponseDto getCompanyMedia(String id);

    QueryCompanyMediaListResponseDto listCompanyMediaByCompany(String companyId);
}
