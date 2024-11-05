package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.Proposal;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:17:51+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class ProposalMapperImpl implements ProposalMapper {

    @Override
    public ProposalDTO toDTO(Proposal proposal) {
        if ( proposal == null ) {
            return null;
        }

        ProposalDTO proposalDTO = new ProposalDTO();

        proposalDTO.setRecruiter( mapRecruiterToDTO( proposal.getRecruiter() ) );
        proposalDTO.setStudent( mapStudentToDTO( proposal.getStudent() ) );
        proposalDTO.setProposalId( proposal.getProposalId() );
        proposalDTO.setJob( proposal.getJob() );
        proposalDTO.setContact( proposal.getContact() );
        proposalDTO.setDescription( proposal.getDescription() );
        proposalDTO.setCreatedDate( proposal.getCreatedDate() );
        proposalDTO.setStatus( proposal.getStatus() );

        return proposalDTO;
    }

    @Override
    public Proposal toEntity(ProposalDTO proposalDTO) {
        if ( proposalDTO == null ) {
            return null;
        }

        Proposal proposal = new Proposal();

        proposal.setRecruiter( mapDTOToRecruiter( proposalDTO.getRecruiter() ) );
        proposal.setStudent( mapDTOToStudent( proposalDTO.getStudent() ) );
        proposal.setJob( proposalDTO.getJob() );
        proposal.setContact( proposalDTO.getContact() );
        proposal.setDescription( proposalDTO.getDescription() );
        proposal.setCreatedDate( proposalDTO.getCreatedDate() );
        proposal.setProposalId( proposalDTO.getProposalId() );
        proposal.setStatus( proposalDTO.getStatus() );

        return proposal;
    }
}
