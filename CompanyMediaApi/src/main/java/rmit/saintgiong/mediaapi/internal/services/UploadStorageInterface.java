package rmit.saintgiong.mediaapi.internal.services;

import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;

public interface UploadStorageInterface {
    UploadStorageResponseDto uploadCompanyMedia(String companyId, byte[] bytes, String contentType, String originalFilename);
}

