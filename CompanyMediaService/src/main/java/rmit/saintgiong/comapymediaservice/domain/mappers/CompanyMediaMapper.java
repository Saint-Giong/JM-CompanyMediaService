package rmit.saintgiong.comapymediaservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rmit.saintgiong.comapymediaservice.domain.models.CompanyMedia;
import rmit.saintgiong.comapymediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.UpdateCompanyMediaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

@Mapper(componentModel = "spring")
public interface CompanyMediaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadAt", ignore = true)
    @Mapping(target = "active", constant = "false")
    CompanyMedia fromCreateCompanyProfileCommand(CreateCompanyMediaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    CompanyMedia fromUpdateCompanyProfileCommand(UpdateCompanyMediaRequestDto dto);

    @Mapping(target = "mediaUrl", source = "mediaPath")
    CompanyMediaEntity toEntity(CompanyMedia model);

    @Mapping(target = "mediaPath", source = "mediaUrl")
    QueryCompanyMediaResponseDto toQueryResponse(CompanyMediaEntity entity);
}
