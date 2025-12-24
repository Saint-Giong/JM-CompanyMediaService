package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;

public interface CreateCompanyMediaInterface {
    CreateCompanyMediaResponseDto createCompanyMedia(CreateCompanyMediaRequestDto request);
}
