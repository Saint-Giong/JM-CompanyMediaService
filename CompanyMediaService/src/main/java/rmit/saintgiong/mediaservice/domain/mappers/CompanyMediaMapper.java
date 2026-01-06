package rmit.saintgiong.mediaservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rmit.saintgiong.mediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

@Mapper(componentModel = "spring")
public interface CompanyMediaMapper {

    @Mapping(target = "mediaPath", source = "mediaUrl")
    QueryCompanyMediaResponseDto toQueryResponse(CompanyMediaEntity entity);
}
