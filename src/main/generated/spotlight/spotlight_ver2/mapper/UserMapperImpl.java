package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.UserDTO;
import spotlight.spotlight_ver2.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T00:39:28+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setStudent( mapStudentIfRoleMatches( user.getStudent() ) );
        userDTO.setRecruiter( mapRecruiterIfRoleMatches( user.getRecruiter() ) );
        userDTO.setHashtags( mapHashtagListToDTOList( user.getHashtags() ) );
        userDTO.setFeeds( mapFeedListToDTOList( user.getFeeds() ) );
        userDTO.setScraps( mapScrapListToDTOList( user.getScraps() ) );
        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setName( user.getName() );
        userDTO.setProfileImage( user.getProfileImage() );
        userDTO.setRole( user.getRole() );
        userDTO.setCreatedDate( user.getCreatedDate() );
        userDTO.setUpdatedDate( user.getUpdatedDate() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setFeeds( mapDTOListToFeedList( userDTO.getFeeds() ) );
        user.setHashtags( mapDTOListToHashtagList( userDTO.getHashtags() ) );
        user.setScraps( mapDTOListToScrapList( userDTO.getScraps() ) );
        user.setId( userDTO.getId() );
        user.setUsername( userDTO.getUsername() );
        user.setEmail( userDTO.getEmail() );
        user.setName( userDTO.getName() );
        user.setRole( userDTO.getRole() );
        user.setProfileImage( userDTO.getProfileImage() );
        user.setCreatedDate( userDTO.getCreatedDate() );
        user.setUpdatedDate( userDTO.getUpdatedDate() );

        return user;
    }
}
