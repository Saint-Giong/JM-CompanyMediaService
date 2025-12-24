package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.services.CompanyMediaQueryService;
import rmit.saintgiong.comapymediaservice.domain.validators.CompanyMediaBaseValidator;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;


@ExtendWith(MockitoExtension.class)
class CompanyMediaQueryServiceTest {

    @Mock
    private CompanyMediaMapper mapper;

    @Mock
    private CompanyMediaBaseValidator<Void> baseValidator;

    @Mock
    private CompanyMediaRepository repository;

    @InjectMocks
    private CompanyMediaQueryService queryService;

    private UUID existingId;
    private UUID companyId;
    private CompanyMediaEntity existingEntity;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        existingEntity = CompanyMediaEntity.builder()
                .id(existingId)
                .mediaTitle("Name")
                .mediaDescription("Desc")
                .mediaType("IMAGE")
                .mediaUrl("http://example.com/media.png")
                .companyId(companyId)
                .active(false)
                .build();
    }

    @Test
    void givenExistingId_whenGet_thenReturnDto() {
        QueryCompanyMediaResponseDto dto = QueryCompanyMediaResponseDto.builder()
                .id(existingId.toString())
                .mediaTitle(existingEntity.getMediaTitle())
                .mediaDescription(existingEntity.getMediaDescription())
                .mediaType(existingEntity.getMediaType())
                .mediaPath(existingEntity.getMediaUrl())
                .companyId(existingEntity.getCompanyId().toString())
                .active(existingEntity.isActive())
                .build();

        when(baseValidator.assertExistsById(existingId)).thenReturn(existingEntity);
        when(mapper.toQueryResponse(existingEntity)).thenReturn(dto);

        QueryCompanyMediaResponseDto result = queryService.getCompanyMedia(existingId.toString());

        assertNotNull(result);
        assertEquals(existingId.toString(), result.getId());
        assertEquals(existingEntity.getMediaTitle(), result.getMediaTitle());

        verify(baseValidator, times(1)).assertExistsById(existingId);
        verify(mapper, times(1)).toQueryResponse(existingEntity);
    }

    @Test
    void givenInvalidUuidString_whenGet_thenThrowIllegalArgumentException() {
        String bad = "not-a-uuid";
        assertThrows(IllegalArgumentException.class, () -> queryService.getCompanyMedia(bad));
        verifyNoInteractions(baseValidator);
        verifyNoInteractions(mapper);
    }

    @Test
    void givenNoActiveMedia_whenGetActive_thenThrowDomainException() {
        when(repository.findFirstByCompanyIdAndActiveTrue(companyId)).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> queryService.getActiveCompanyProfileImage(companyId.toString()));
        assertEquals(RESOURCE_NOT_FOUND, ex.getDomainCode());

        verify(repository, times(1)).findFirstByCompanyIdAndActiveTrue(companyId);
        verifyNoInteractions(mapper);
    }
}

