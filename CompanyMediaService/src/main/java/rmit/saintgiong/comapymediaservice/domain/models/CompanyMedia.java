package rmit.saintgiong.comapymediaservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmit.saintgiong.companymediaapi.internal.common.type.MediaType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyMedia {
    private UUID id;

    private String mediaTitle;

    private String mediaDescription;

    private MediaType mediaType;

    private String mediaPath;

    private LocalDateTime uploadAt;

    private UUID companyId;

    private boolean active;
}
