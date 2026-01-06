package rmit.saintgiong.mediaapi.internal.services;

import rmit.saintgiong.mediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;

public interface UpdateCompanyMediaInterface {
    void updateCompanyMedia(String mediaId, UpdateCompanyMediaRequestDto request, byte[] bytes, String contentType, String originalFilename);
}
