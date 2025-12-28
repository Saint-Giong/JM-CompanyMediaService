package rmit.saintgiong.comapymediaservice.domain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UploadStorageRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.UploadStorageResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.UploadStorageInterface;

import java.util.Set;
import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
public class StorageController {

    private final UploadStorageInterface uploadStorageService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(value = "/storage/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Callable<ResponseEntity<UploadStorageResponseDto>> uploadCompanyMedia(
            @RequestPart("meta") byte[] metaBytes,
            @RequestPart("file") MultipartFile file
    ) {
        return () -> {
            UploadStorageRequestDto meta = objectMapper.readValue(metaBytes, UploadStorageRequestDto.class);
            validate(meta);

            UploadStorageResponseDto resp = uploadStorageService.uploadCompanyMedia(
                    meta.getCompanyId(),
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );

            return new ResponseEntity<>(resp, HttpStatus.OK);
        };
    }

    private <T> void validate(T dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException((Set<ConstraintViolation<?>>) (Set<?>) violations);
        }
    }
}
