package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.comapymediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.comapymediaservice.domain.services.CompanyMediaUpdateService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static rmit.saintgiong.companymediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class CompanyMediaUpdateServiceTest {

    @Mock
    private CompanyMediaRepository repository;

    @InjectMocks
    private CompanyMediaUpdateService updateService;

    private UUID mediaId;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        mediaId = UUID.randomUUID();
        companyId = UUID.randomUUID();
    }

    @Test
    void givenExistingId_whenUpdateWithNoUpload_thenUpdateMetaOnly() {
        // Arrange
        CompanyMediaEntity entity = CompanyMediaEntity.builder()
                .id(mediaId)
                .companyId(companyId)
                .mediaTitle("Old")
                .mediaDescription("Old desc")
                .mediaType("IMAGE")
                .mediaUrl("company-media/company/" + companyId + "/obj.png")
                .build();

        when(repository.findById(mediaId)).thenReturn(Optional.of(entity));
        when(repository.save(any(CompanyMediaEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var request = rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto.builder()
                .mediaTitle("New")
                .mediaDescription("New desc")
                .mediaType(null) // optional
                .build();

        // Act
        updateService.updateCompanyMedia(mediaId.toString(), request, null, null, null);

        // Assert
        verify(repository, times(1)).findById(mediaId);
        verify(repository, times(1)).save(argThat(saved ->
                saved.getId().equals(mediaId)
                        && saved.getMediaTitle().equals("New")
                        && saved.getMediaDescription().equals("New desc")
                        && saved.getMediaUrl() == null
        ));
    }

    @Test
    void givenNonExistingId_whenUpdateService_thenThrowDomainException() {
        // Arrange
        UUID nonExisting = UUID.randomUUID();
        when(repository.findById(nonExisting)).thenReturn(Optional.empty());

        var request = rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto.builder()
                .mediaTitle("New")
                .mediaDescription("New desc")
                .mediaType(null)
                .build();

        // Act
        DomainException ex = assertThrows(DomainException.class, () ->
                updateService.updateCompanyMedia(nonExisting.toString(), request, null, null, null));

        // Assert
        assertEquals(RESOURCE_NOT_FOUND, ex.getDomainCode());
        verify(repository, times(1)).findById(nonExisting);
        verify(repository, never()).save(any());
    }
}
