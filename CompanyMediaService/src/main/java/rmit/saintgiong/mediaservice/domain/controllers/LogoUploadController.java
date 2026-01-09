package rmit.saintgiong.mediaservice.domain.controllers;

import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import rmit.saintgiong.mediaapi.internal.common.dto.request.UploadStorageRequestDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;
import rmit.saintgiong.mediaapi.internal.services.UploadStorageInterface;

/**
 * Controller for handling company logo uploads.
 * Provides a dedicated endpoint for logo-specific uploads with image
 * validation.
 */
@RestController
@AllArgsConstructor
public class LogoUploadController {

    private final UploadStorageInterface uploadStorageService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    /**
     * Upload a company logo image.
     * 
     * Accepts multipart form data with:
     * - meta: JSON containing companyId
     * - file: The image file (png, jpg, webp, max 2MB)
     * 
     * @param metaBytes JSON metadata containing companyId
     * @param file      The logo image file
     * @return Upload response with success status and URL
     */
    @PostMapping(value = "/storage/upload-logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Callable<ResponseEntity<UploadStorageResponseDto>> uploadCompanyLogo(
            @RequestPart("meta") byte[] metaBytes,
            @RequestPart("file") MultipartFile file) {
        return () -> {
            UploadStorageRequestDto meta = objectMapper.readValue(metaBytes, UploadStorageRequestDto.class);
            validate(meta);

            UploadStorageResponseDto resp = uploadStorageService.uploadCompanyLogo(
                    meta.getCompanyId(),
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename());

            if (resp.isSuccess()) {
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }
        };
    }

    private <T> void validate(T dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException((Set<ConstraintViolation<?>>) (Set<?>) violations);
        }
    }
}
