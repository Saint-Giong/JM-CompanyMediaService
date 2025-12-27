package rmit.saintgiong.comapymediaservice.domain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.DeleteCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.QueryCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;

import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
public class CompanyMediaController {

    private final CreateCompanyMediaInterface createService;

    private final UpdateCompanyMediaInterface updateService;

    private final QueryCompanyMediaInterface queryService;

    private final DeleteCompanyMediaInterface deleteService;

    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Callable<ResponseEntity<CreateCompanyMediaResponseDto>> createCompanyMedia(
            @RequestPart("meta") byte[] metaBytes,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return () -> {
            CreateCompanyMediaMetaRequestDto meta = objectMapper.readValue(metaBytes, CreateCompanyMediaMetaRequestDto.class);
            validate(meta);

            byte[] bytes = null;
            String contentType = null;
            String originalFilename = null;
            if (file != null && !file.isEmpty()) {
                bytes = file.getBytes();
                contentType = file.getContentType();
                originalFilename = file.getOriginalFilename();
            }

            CreateCompanyMediaResponseDto response = createService.createCompanyMedia(meta, bytes, contentType, originalFilename);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    public Callable<ResponseEntity<QueryCompanyMediaResponseDto>> getCompanyMedia(@PathVariable @UUID String id) {
        return () -> {
            QueryCompanyMediaResponseDto response = queryService.getCompanyMedia(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @GetMapping
    public Callable<ResponseEntity<QueryCompanyMediaListResponseDto>> listCompanyMediaByCompany(
            @RequestParam("companyId") String companyId) {
        return () -> {
            QueryCompanyMediaListResponseDto response = queryService.listCompanyMediaByCompany(companyId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Callable<ResponseEntity<Void>> updateCompanyMedia(
            @PathVariable("id") @UUID String id,
            @RequestPart("meta") byte[] metaBytes,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return () -> {
            UpdateCompanyMediaRequestDto request = objectMapper.readValue(metaBytes, UpdateCompanyMediaRequestDto.class);
            validate(request);

            byte[] bytes = null;
            String contentType = null;
            String originalFilename = null;
            if (file != null && !file.isEmpty()) {
                bytes = file.getBytes();
                contentType = file.getContentType();
                originalFilename = file.getOriginalFilename();
            }

            updateService.updateCompanyMedia(id, request, bytes, contentType, originalFilename);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        };
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<Void>> deleteCompanyMedia(@PathVariable("id") @UUID String id) {
        return () -> {
            deleteService.deleteCompanyMedia(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        };
    }

    private <T> void validate(@Valid T dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException((java.util.Set<ConstraintViolation<?>>) (java.util.Set<?>) violations);
        }
    }
}