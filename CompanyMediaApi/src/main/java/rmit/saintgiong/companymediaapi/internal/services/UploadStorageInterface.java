package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.response.UploadStorageResponseDto;

public interface UploadStorageInterface {
    UploadStorageResponseDto uploadCompanyMedia(String companyId, byte[] bytes, String contentType, String originalFilename);
}

