package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.comapymediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.comapymediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.services.CompanyMediaCreateService;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
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

    @Mock
    private ObjectStorageService objectStorageService;

    @Mock
    private GcsStorageProperties gcsProps;

    @InjectMocks
    private CompanyMediaCreateService profileCreateService;

    @Test
    void givenValidMetaAndBytes_whenUploadCreate_thenUploadsAndPersistsObjectName() {
        UUID generatedId = UUID.randomUUID();
        String companyId = UUID.randomUUID().toString();

        when(gcsProps.getUploadPrefix()).thenReturn("company-media/");

        CreateCompanyMediaMetaRequestDto meta = CreateCompanyMediaMetaRequestDto.builder()
                .mediaTitle("Test")
                .mediaDescription("Desc")
                .mediaType(MediaType.IMAGE)
                .companyId(companyId)
                .build();

        byte[] bytes = "hello".getBytes();
        String contentType = "image/png";
        String objectName = "company-media/company/" + companyId + "/x.png";

        doNothing().when(createValidator).validate(any(CreateCompanyMediaMetaRequestDto.class));
        when(objectStorageService.upload(anyString(), eq(bytes), eq(contentType))).thenReturn(objectName);

        CompanyMediaEntity savedEntity = CompanyMediaEntity.builder()
                .id(generatedId)
                .mediaTitle(meta.getMediaTitle())
                .mediaDescription(meta.getMediaDescription())
                .mediaType(meta.getMediaType().name())
                .mediaUrl(objectName)
                .companyId(UUID.fromString(companyId))
                .build();

        when(repository.save(any(CompanyMediaEntity.class))).thenReturn(savedEntity);

        CreateCompanyMediaResponseDto resp = profileCreateService.createCompanyMedia(
                meta,
                bytes,
                contentType,
                "x.png"
        );

        assertNotNull(resp);
        assertEquals(generatedId.toString(), resp.getId());

        verify(objectStorageService, times(1)).upload(anyString(), eq(bytes), eq(contentType));

        ArgumentCaptor<CompanyMediaEntity> entityCaptor = ArgumentCaptor.forClass(CompanyMediaEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());
        assertEquals(objectName, entityCaptor.getValue().getMediaUrl());
        assertEquals(UUID.fromString(companyId), entityCaptor.getValue().getCompanyId());
    }
}
