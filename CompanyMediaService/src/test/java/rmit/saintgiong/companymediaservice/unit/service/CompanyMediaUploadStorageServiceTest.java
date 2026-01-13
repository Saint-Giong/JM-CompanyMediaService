package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.mediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.mediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.mediaservice.domain.services.external.storage.CompanyMediaUploadStorageService;
import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyMediaUploadStorageServiceTest {

    @Mock
    private ObjectStorageService objectStorageService;

    @Mock
    private GcsStorageProperties gcsProps;

    @InjectMocks
    private CompanyMediaUploadStorageService uploadStorageService;

    @Test
    void givenValidInput_whenUpload_thenReturnsSuccessAndUrl() {
        String companyId = UUID.randomUUID().toString();
        byte[] bytes = "hello".getBytes();
        String contentType = "image/png";

        when(gcsProps.getUploadPrefix()).thenReturn("company-media/");
        when(gcsProps.getSignedUrlTtlMinutes()).thenReturn(30L);
        when(objectStorageService.upload(anyString(), eq(bytes), eq(contentType)))
                .thenReturn("company-media/company/" + companyId + "/x.png");
        try {
            when(objectStorageService.signUrl(anyString(), any(Duration.class)))
                    .thenReturn(new URL("https://storage.googleapis.com/signed-url"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UploadStorageResponseDto resp = uploadStorageService.uploadCompanyMedia(companyId, bytes, contentType, "x.png");

        assertNotNull(resp);
        assertTrue(resp.isSuccess());
        assertEquals("https://storage.googleapis.com/signed-url", resp.getUrl());
        verify(objectStorageService, times(1)).upload(anyString(), eq(bytes), eq(contentType));
        verify(objectStorageService, times(1)).signUrl(anyString(), any(Duration.class));
    }

    @Test
    void givenEmptyBytes_whenUpload_thenReturnsNotSuccessAndDoesNotUpload() {
        String companyId = UUID.randomUUID().toString();

        UploadStorageResponseDto resp = uploadStorageService.uploadCompanyMedia(companyId, new byte[0], "image/png", "x.png");

        assertNotNull(resp);
        assertFalse(resp.isSuccess());
        assertNull(resp.getUrl());
        verifyNoInteractions(objectStorageService);
    }

    @Test
    void givenInvalidCompanyId_whenUpload_thenReturnsNotSuccess() {
        UploadStorageResponseDto resp = uploadStorageService.uploadCompanyMedia("not-a-uuid", "hello".getBytes(), "image/png", "x.png");

        assertNotNull(resp);
        assertFalse(resp.isSuccess());
        assertNull(resp.getUrl());
        verifyNoInteractions(objectStorageService);
    }
}
