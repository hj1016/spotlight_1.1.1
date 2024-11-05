package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:17:51+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class ExhibitionMapperImpl implements ExhibitionMapper {

    @Override
    public ExhibitionDTO toDTO(Exhibition exhibition) {
        if ( exhibition == null ) {
            return null;
        }

        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();

        exhibitionDTO.setUser( userToExhibitionUserDTO( exhibition.getUser() ) );
        exhibitionDTO.setFeed( mapFeedToExhibitionFeedDTO( exhibition.getFeed() ) );
        exhibitionDTO.setExhibitionId( exhibition.getExhibitionId() );
        exhibitionDTO.setLocation( exhibition.getLocation() );
        exhibitionDTO.setSchedule( exhibition.getSchedule() );
        exhibitionDTO.setTime( exhibition.getTime() );

        return exhibitionDTO;
    }

    @Override
    public Exhibition toEntity(ExhibitionDTO exhibitionDTO) {
        if ( exhibitionDTO == null ) {
            return null;
        }

        Exhibition exhibition = new Exhibition();

        exhibition.setUser( mapUserIdToUser( exhibitionDTO.getUser() ) );
        exhibition.setExhibitionId( exhibitionDTO.getExhibitionId() );
        exhibition.setLocation( exhibitionDTO.getLocation() );
        exhibition.setSchedule( exhibitionDTO.getSchedule() );
        exhibition.setTime( exhibitionDTO.getTime() );

        return exhibition;
    }

    protected ExhibitionDTO.ExhibitionUserDTO userToExhibitionUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        ExhibitionDTO.ExhibitionUserDTO exhibitionUserDTO = new ExhibitionDTO.ExhibitionUserDTO();

        exhibitionUserDTO.setId( user.getId() );

        return exhibitionUserDTO;
    }
}
