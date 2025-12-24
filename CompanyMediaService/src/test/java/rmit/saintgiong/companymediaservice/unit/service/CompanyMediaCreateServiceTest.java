package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.comapymediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.comapymediaservice.domain.models.CompanyMedia;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.services.CompanyMediaCreateService;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaCreateValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyMediaCreateServiceTest {

    @Mock
    private CompanyMediaMapper mapper;

    @Mock
    private CompanyMediaRepository repository;

    @Mock
    private CompanyMediaCreateValidator createValidator;

    @InjectMocks
    private CompanyMediaCreateService profileCreateService;

    private CreateCompanyMediaRequestDto requestDto;

    private CompanyMedia companyMedia;

    @BeforeEach
    void setUp() {
        requestDto = CreateCompanyMediaRequestDto.builder()
                .mediaTitle("Test Media")
                .mediaDescription("Desc")
                .mediaType(MediaType.IMAGE)
                .mediaPath("http://example.com/media.png")
                .companyId(UUID.randomUUID().toString())
                .build();

        companyMedia = CompanyMedia.builder()
                .mediaTitle(requestDto.getMediaTitle())
                .mediaDescription(requestDto.getMediaDescription())
                .mediaType(requestDto.getMediaType())
                .mediaPath(requestDto.getMediaPath())
                .companyId(UUID.fromString(requestDto.getCompanyId()))
                .active(false)
                .build();
    }

    @Test
    void givenValidRequest_whenCreateService_thenCreateSuccessfully() {
        // Arrange
        UUID generatedId = UUID.randomUUID();

        CompanyMediaEntity toSave = CompanyMediaEntity.builder()
                .mediaTitle(companyMedia.getMediaTitle())
                .mediaDescription(companyMedia.getMediaDescription())
                .mediaType(companyMedia.getMediaType().name())
                .mediaUrl(companyMedia.getMediaPath())
                .companyId(companyMedia.getCompanyId())
                .active(false)
                .build();

        CompanyMediaEntity savedEntity = CompanyMediaEntity.builder()
                .id(generatedId)
                .mediaTitle(toSave.getMediaTitle())
                .mediaDescription(toSave.getMediaDescription())
                .mediaType(toSave.getMediaType())
                .mediaUrl(toSave.getMediaUrl())
                .companyId(toSave.getCompanyId())
                .active(toSave.isActive())
                .build();

        doNothing().when(createValidator).validate(requestDto);
        when(mapper.fromCreateCompanyProfileCommand(requestDto)).thenReturn(companyMedia);
        when(mapper.toEntity(companyMedia)).thenReturn(toSave);
        when(repository.save(toSave)).thenReturn(savedEntity);

        // Act
        CreateCompanyMediaResponseDto response = profileCreateService.createCompanyMedia(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(generatedId.toString(), response.getId());

        ArgumentCaptor<CompanyMediaEntity> captor = ArgumentCaptor.forClass(CompanyMediaEntity.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals(toSave, captor.getValue());
    }
}
