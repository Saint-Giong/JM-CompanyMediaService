package rmit.saintgiong.mediaservice.domain.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmit.saintgiong.mediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.mediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.mediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.mediaapi.internal.services.DeleteCompanyMediaInterface;
import rmit.saintgiong.mediaapi.internal.services.QueryCompanyMediaInterface;
import rmit.saintgiong.mediaapi.internal.services.UpdateCompanyMediaInterface;

import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
public class CompanyMediaController {

    private final CreateCompanyMediaInterface createService;

    private final UpdateCompanyMediaInterface updateService;

    private final QueryCompanyMediaInterface queryService;

    private final DeleteCompanyMediaInterface deleteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<CreateCompanyMediaResponseDto>> createCompanyMedia(
            @Valid @RequestBody CreateCompanyMediaRequestDto meta
    ) {
        return () -> {
            CreateCompanyMediaResponseDto response = createService.createCompanyMedia(meta, null, null, null);
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Void>> updateCompanyMedia(
            @PathVariable("id") @UUID String id,
            @Valid @RequestBody UpdateCompanyMediaRequestDto request
    ) {
        return () -> {
            updateService.updateCompanyMedia(id, request, null, null, null);
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
}