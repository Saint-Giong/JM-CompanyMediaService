package rmit.saintgiong.mediaservice.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmit.saintgiong.mediaservice.domain.repositories.entities.CompanyMediaEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyMediaRepository extends JpaRepository<CompanyMediaEntity, UUID> {

    List<CompanyMediaEntity> findAllByCompanyId(UUID companyId);
}
