package spotlight.spotlight_ver2.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Project;
import spotlight.spotlight_ver2.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T00:39:28+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class FeedMapperImpl implements FeedMapper {

    @Override
    public FeedDTO toDTO(Feed feed) {
        if ( feed == null ) {
            return null;
        }

        FeedDTO feedDTO = new FeedDTO();

        feedDTO.setProject( mapProjectToFeedProjectDTO( feed.getProject() ) );
        feedDTO.setUser( mapUserToFeedUserDTO( feed.getUser() ) );
        feedDTO.setHashtags( mapHashtagSetToDTOSet( feed.getHashtags() ) );
        feedDTO.setCategory( mapCategoryToFeedCategoryDTO( feed.getCategory() ) );
        feedDTO.setExhibition( mapExhibitionToFeedExhibitionDTO( feed.getExhibition() ) );
        feedDTO.setFeedId( feed.getFeedId() );
        feedDTO.setTitle( feed.getTitle() );
        feedDTO.setThumbnailImage( feed.getThumbnailImage() );
        List<String> list = feed.getFeedImages();
        if ( list != null ) {
            feedDTO.setFeedImages( new ArrayList<String>( list ) );
        }
        feedDTO.setContent( feed.getContent() );
        feedDTO.setScrap( feed.getScrap() );
        feedDTO.setHitsUser( feed.getHitsUser() );
        feedDTO.setHitsRecruiter( feed.getHitsRecruiter() );
        feedDTO.setCreatedDate( feed.getCreatedDate() );
        feedDTO.setModifiedDate( feed.getModifiedDate() );

        return feedDTO;
    }

    @Override
    public Feed toEntity(FeedDTO feedDTO) {
        if ( feedDTO == null ) {
            return null;
        }

        Feed feed = new Feed();

        feed.setHashtags( mapDTOSetToHashtagSet( feedDTO.getHashtags() ) );
        feed.setFeedId( feedDTO.getFeedId() );
        feed.setTitle( feedDTO.getTitle() );
        List<String> list = feedDTO.getFeedImages();
        if ( list != null ) {
            feed.setFeedImages( new ArrayList<String>( list ) );
        }
        feed.setThumbnailImage( feedDTO.getThumbnailImage() );
        feed.setContent( feedDTO.getContent() );
        feed.setScrap( feedDTO.getScrap() );
        feed.setHitsUser( feedDTO.getHitsUser() );
        feed.setHitsRecruiter( feedDTO.getHitsRecruiter() );
        feed.setCategory( feedCategoryDTOToCategory( feedDTO.getCategory() ) );
        feed.setCreatedDate( feedDTO.getCreatedDate() );
        feed.setModifiedDate( feedDTO.getModifiedDate() );
        feed.setUser( feedUserDTOToUser( feedDTO.getUser() ) );
        feed.setExhibition( feedExhibitionDTOToExhibition( feedDTO.getExhibition() ) );
        feed.setProject( feedProjectDTOToProject( feedDTO.getProject() ) );

        return feed;
    }

    protected Category feedCategoryDTOToCategory(FeedDTO.FeedCategoryDTO feedCategoryDTO) {
        if ( feedCategoryDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( feedCategoryDTO.getId() );
        category.setName( feedCategoryDTO.getName() );

        return category;
    }

    protected User feedUserDTOToUser(FeedDTO.FeedUserDTO feedUserDTO) {
        if ( feedUserDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( feedUserDTO.getId() );
        user.setName( feedUserDTO.getName() );

        return user;
    }

    protected Exhibition feedExhibitionDTOToExhibition(FeedDTO.FeedExhibitionDTO feedExhibitionDTO) {
        if ( feedExhibitionDTO == null ) {
            return null;
        }

        Exhibition exhibition = new Exhibition();

        exhibition.setExhibitionId( feedExhibitionDTO.getExhibitionId() );
        exhibition.setLocation( feedExhibitionDTO.getLocation() );
        exhibition.setSchedule( feedExhibitionDTO.getSchedule() );
        exhibition.setTime( feedExhibitionDTO.getTime() );

        return exhibition;
    }

    protected Project feedProjectDTOToProject(FeedDTO.FeedProjectDTO feedProjectDTO) {
        if ( feedProjectDTO == null ) {
            return null;
        }

        Project project = new Project();

        project.setId( feedProjectDTO.getId() );
        project.setName( feedProjectDTO.getName() );

        return project;
    }
}
