package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

public interface QueryCompanyMediaInterface {
    QueryCompanyMediaResponseDto getCompanyMedia(String id);

    QueryCompanyMediaListResponseDto listCompanyMediaByCompany(String companyId);
}
