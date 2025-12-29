package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;

public interface UpdateCompanyMediaInterface {
    void updateCompanyMedia(String mediaId, UpdateCompanyMediaRequestDto request, byte[] bytes, String contentType, String originalFilename);
}
