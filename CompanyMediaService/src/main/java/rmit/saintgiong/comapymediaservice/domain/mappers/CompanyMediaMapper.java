package rmit.saintgiong.comapymediaservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rmit.saintgiong.comapymediaservice.domain.models.CompanyMedia;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

@Mapper(componentModel = "spring")
public interface CompanyMediaMapper {

    @Mapping(target = "mediaPath", source = "mediaUrl")
    QueryCompanyMediaResponseDto toQueryResponse(CompanyMediaEntity entity);
}
