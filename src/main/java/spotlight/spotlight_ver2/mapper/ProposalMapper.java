package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.Proposal;

@Mapper
public interface ProposalMapper {
    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    ProposalDTO toDTO(Proposal proposal);
    Proposal toEntity(ProposalDTO proposalDTO);
}