package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.mediaservice.common.exception.domain.DomainException;
import rmit.saintgiong.mediaservice.domain.repositories.CompanyMediaRepository;
import rmit.saintgiong.mediaservice.domain.services.internal.CompanyMediaDeleteService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static rmit.saintgiong.mediaapi.internal.common.type.DomainCode.RESOURCE_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class CompanyMediaDeleteServiceTest {

    @Mock
    private CompanyMediaRepository repository;

    @InjectMocks
    private CompanyMediaDeleteService deleteService;

    @Test
    void givenExistingId_whenDelete_thenDeleteById() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        deleteService.deleteCompanyMedia(id.toString());

        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void givenMissingId_whenDelete_thenThrowNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        DomainException ex = assertThrows(DomainException.class, () -> deleteService.deleteCompanyMedia(id.toString()));

        assertEquals(RESOURCE_NOT_FOUND, ex.getDomainCode());
        verify(repository, times(1)).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void givenInvalidUuidString_whenDelete_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> deleteService.deleteCompanyMedia("not-a-uuid"));
        verifyNoInteractions(repository);
    }
}
