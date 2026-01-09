package rmit.saintgiong.mediaapi.internal.services;

import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;

public interface UploadStorageInterface {
    UploadStorageResponseDto uploadCompanyMedia(String companyId, byte[] bytes, String contentType,
            String originalFilename);

    /**
     * Upload a company logo image.
     * Validates that the file is an image (png, jpg, webp) and within size limit.
     * Stores in company/{companyId}/logo/ path.
     */
    UploadStorageResponseDto uploadCompanyLogo(String companyId, byte[] bytes, String contentType,
            String originalFilename);
}
