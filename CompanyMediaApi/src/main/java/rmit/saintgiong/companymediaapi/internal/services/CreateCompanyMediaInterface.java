package rmit.saintgiong.companymediaapi.internal.services;

import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;

public interface CreateCompanyMediaInterface {

    /**
     * Create a company media post.
     *
     * If the post includes an image/video, provide bytes/contentType/originalFilename.
     * If it doesn't include media, pass null/empty bytes and other file fields as null.
     */
    CreateCompanyMediaResponseDto createCompanyMedia(
            CreateCompanyMediaRequestDto meta,
            byte[] bytes,
            String contentType,
            String originalFilename
    );
}
