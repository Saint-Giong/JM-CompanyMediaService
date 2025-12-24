package rmit.saintgiong.comapymediaservice.domain.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "company_media")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String mediaTitle;

    private String mediaDescription;

    @Column(nullable = false)
    private String mediaType;

    @Column(nullable = false)
    private String mediaUrl;

    @Column(nullable = false)
    private UUID companyId;

    /**
     * Marks the currently-used company profile image.
     * Business rule: at most one record per company should have active=true.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = false;
}
