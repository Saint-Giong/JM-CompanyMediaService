package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;

public interface CreateCompanyMediaInterface {
    CreateCompanyMediaResponseDto createCompanyMedia(CreateCompanyMediaMetaRequestDto meta, byte[] bytes, String contentType, String originalFilename);
}
