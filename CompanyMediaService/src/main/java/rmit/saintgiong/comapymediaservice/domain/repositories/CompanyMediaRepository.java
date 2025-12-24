package rmit.saintgiong.comapymediaservice.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyMediaRepository extends JpaRepository<CompanyMediaEntity, UUID> {

    List<CompanyMediaEntity> findAllByCompanyId(UUID companyId);

    Optional<CompanyMediaEntity> findFirstByCompanyIdAndActiveTrue(UUID companyId);

    @Modifying
    @Query("update CompanyMediaEntity m set m.active = false where m.companyId = :companyId and m.active = true")
    void deactivateAllByCompanyId(@Param("companyId") UUID companyId);
}
