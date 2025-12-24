package rmit.saintgiong.comapymediaservice.domain.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.QueryCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;

import java.util.concurrent.Callable;

@RestController
@AllArgsConstructor
public class CompanyMediaController {

    private final CreateCompanyMediaInterface createService;

    private final UpdateCompanyMediaInterface updateService;

    private final QueryCompanyMediaInterface queryService;

    @PostMapping
    public Callable<ResponseEntity<CreateCompanyMediaResponseDto>> createCompanyMedia(@Valid @RequestBody CreateCompanyMediaRequestDto requestDto) {
        return () -> {
            CreateCompanyMediaResponseDto response = createService.createCompanyMedia(requestDto);
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