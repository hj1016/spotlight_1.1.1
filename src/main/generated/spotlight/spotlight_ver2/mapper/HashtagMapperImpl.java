package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.HashtagDTO;
import spotlight.spotlight_ver2.entity.Hashtag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:17:51+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class HashtagMapperImpl implements HashtagMapper {

    @Override
    public HashtagDTO toDTO(Hashtag hashtag) {
        if ( hashtag == null ) {
            return null;
        }

        HashtagDTO hashtagDTO = new HashtagDTO();

        hashtagDTO.setFeeds( mapFeedsToDTOs( hashtag.getFeeds() ) );
        hashtagDTO.setUser( mapUserToHashtagUserDTO( hashtag.getUser() ) );
        hashtagDTO.setId( hashtag.getId() );
        hashtagDTO.setHashtag( hashtag.getHashtag() );
        hashtagDTO.setCreatedDate( hashtag.getCreatedDate() );

        return hashtagDTO;
    }

    @Override
    public Hashtag toEntity(HashtagDTO hashtagDTO) {
        if ( hashtagDTO == null ) {
            return null;
        }

        Hashtag hashtag = new Hashtag();

        hashtag.setId( hashtagDTO.getId() );
        hashtag.setHashtag( hashtagDTO.getHashtag() );
        hashtag.setCreatedDate( hashtagDTO.getCreatedDate() );

        return hashtag;
    }
}
