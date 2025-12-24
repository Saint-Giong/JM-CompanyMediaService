package rmit.saintgiong.comapymediaservice.domain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.QueryCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;

import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
public class CompanyMediaController {

    private final CreateCompanyMediaInterface createService;

    private final UpdateCompanyMediaInterface updateService;

    private final QueryCompanyMediaInterface queryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Callable<ResponseEntity<CreateCompanyMediaResponseDto>> uploadCompanyMedia(
            @Valid @RequestPart("meta") String metaJson,
            @RequestPart("file") MultipartFile file
    ) {
        return () -> {
            CreateCompanyMediaMetaRequestDto meta = objectMapper.readValue(metaJson, CreateCompanyMediaMetaRequestDto.class);
            CreateCompanyMediaResponseDto response = createService.createCompanyMedia(
                    meta,
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );
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

    @GetMapping("/active")
    public Callable<ResponseEntity<QueryCompanyMediaResponseDto>> getActiveCompanyProfileImage(
            @RequestParam("companyId") String companyId) {
        return () -> {
            QueryCompanyMediaResponseDto response = queryService.getActiveCompanyProfileImage(companyId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PostMapping("/{id}/activate")
    public Callable<ResponseEntity<Void>> activateCompanyProfileImage(@PathVariable("id") @UUID String id) {
        return () -> {
            updateService.activateCompanyProfileImage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        };
    }
}