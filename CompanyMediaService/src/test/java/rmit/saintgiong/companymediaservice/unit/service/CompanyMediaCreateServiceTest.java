package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.services.CompanyMediaCreateService;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyMediaCreateServiceTest {

    @Mock
    private CompanyMediaRepository repository;

    @Mock
    private CompanyMediaCreateValidator createValidator;

    @InjectMocks
    private CompanyMediaCreateService profileCreateService;

    @Test
    void givenValidMetaWithMediaUrl_whenCreate_thenPersistsMediaUrl() {
        UUID generatedId = UUID.randomUUID();
        String companyId = UUID.randomUUID().toString();

        CreateCompanyMediaRequestDto meta = CreateCompanyMediaRequestDto.builder()
                .mediaTitle("Test")
                .mediaDescription("Desc")
                .mediaType(MediaType.IMAGE)
                .mediaUrl("company-media/company/" + companyId + "/x.png")
                .companyId(companyId)
                .build();

        CompanyMediaEntity savedEntity = CompanyMediaEntity.builder()
                .id(generatedId)
                .mediaTitle(meta.getMediaTitle())
                .mediaDescription(meta.getMediaDescription())
                .mediaType(meta.getMediaType().name())
                .mediaUrl(meta.getMediaUrl())
                .companyId(UUID.fromString(companyId))
                .build();

        when(repository.save(any(CompanyMediaEntity.class))).thenReturn(savedEntity);

        CreateCompanyMediaResponseDto resp = profileCreateService.createCompanyMedia(
                meta,
                null,
                null,
                null
        );

        assertNotNull(resp);
        assertEquals(generatedId.toString(), resp.getId());

        verify(createValidator, times(1)).validate(any(CreateCompanyMediaRequestDto.class));

        ArgumentCaptor<CompanyMediaEntity> entityCaptor = ArgumentCaptor.forClass(CompanyMediaEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());
        assertEquals(meta.getMediaUrl(), entityCaptor.getValue().getMediaUrl());
        assertEquals(UUID.fromString(companyId), entityCaptor.getValue().getCompanyId());
    }
}
