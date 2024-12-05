package spotlight.spotlight_ver2.service;

import spotlight.spotlight_ver2.dto.*;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.mapper.FeedMapper;
import spotlight.spotlight_ver2.mapper.StudentMapper;
import spotlight.spotlight_ver2.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final HashtagRepository hashtagRepository;
    private final SearchHistoryService searchHistoryService;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final CategoryRepository categoryRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final StudentRepository studentRepository;
    private final FeedMapper feedMapper = FeedMapper.INSTANCE;
    private final StudentMapper studentMapper = StudentMapper.INSTANCE;

    @Autowired
    public FeedService(FeedRepository feedRepository, HashtagRepository hashtagRepository, SearchHistoryService searchHistoryService, UserRepository userRepository, ScrapRepository scrapRepository, CategoryRepository categoryRepository, ExhibitionRepository exhibitionRepository, ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository, StudentRepository studentRepository) {
        this.feedRepository = feedRepository;
        this.hashtagRepository = hashtagRepository;
        this.searchHistoryService = searchHistoryService;
        this.userRepository = userRepository;
        this.scrapRepository = scrapRepository;
        this.categoryRepository = categoryRepository;
        this.exhibitionRepository = exhibitionRepository;
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.studentRepository = studentRepository;
    }

    public FeedDTO createFeed(FeedDTO feedDTO) {
        if (feedDTO.getUser() == null || feedDTO.getUser().getId() == null) {
            throw new BadRequestException("사용자 정보가 필요합니다.");
        }

        User user = userRepository.findById(feedDTO.getUser().getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Feed feed = feedMapper.toEntity(feedDTO);
        validateFeed(feed);
        feed.setUser(user); // 연관된 User 설정
        Feed createdFeed = feedRepository.save(feed);

        return feedMapper.toDTO(createdFeed);
    }

    public FeedDTO getFeedById(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("피드를 찾을 수 없습니다."));

        FeedDTO feedDTO = new FeedDTO(feed);

        // 사용자 ID가 제공된 경우, 스크랩 상태를 확인
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
            boolean isScrapped = scrapRepository.existsByUserAndFeed(user, feed);
            feedDTO.setScrapped(isScrapped);
        } else {
            feedDTO.setScrapped(false); // 기본값
        }

        return feedDTO;
    }

    public FeedDTO updateFeed(Long id, FeedDTO feedDTO) {
        // 기존 Feed 가져오기
        Feed existingFeed = feedRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 사용자 권한 확인
        User user = userRepository.findById(feedDTO.getUser().getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        if (!existingFeed.getUser().equals(user)) {
            throw new UnauthorizedException("게시글을 수정할 권한이 없습니다.");
        }

        // 필드 업데이트
        existingFeed.setTitle(feedDTO.getTitle());
        existingFeed.setContent(feedDTO.getContent());
        existingFeed.setThumbnailImage(feedDTO.getThumbnailImage());
        existingFeed.setFeedImages(feedDTO.getFeedImages());
        existingFeed.setScrap(feedDTO.getScrap());
        existingFeed.setHitsUser(feedDTO.getHitsUser());
        existingFeed.setHitsRecruiter(feedDTO.getHitsRecruiter());

        // Category 업데이트
        if (feedDTO.getCategory() != null) {
            Category category = categoryRepository.findById(feedDTO.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("해당 카테고리를 찾을 수 없습니다."));
            existingFeed.setCategory(category);
        } else {
            existingFeed.setCategory(null);
        }

        // Exhibition 업데이트
        if (feedDTO.getExhibition() != null) {
            Exhibition exhibition = exhibitionRepository.findById(feedDTO.getExhibition().getExhibitionId())
                    .orElseThrow(() -> new NotFoundException("해당 전시 정보를 찾을 수 없습니다."));
            existingFeed.setExhibition(exhibition);
        } else {
            existingFeed.setExhibition(null);
        }

        // Project 및 ProjectRoles 업데이트
        if (feedDTO.getProject() != null) {
            Project project = projectRepository.findById(feedDTO.getProject().getId())
                    .orElseThrow(() -> new NotFoundException("해당 프로젝트를 찾을 수 없습니다."));
            existingFeed.setProject(project);

            // ProjectRoles 업데이트
            if (feedDTO.getProject().getProjectRoles() != null) {
                project.getProjectRoles().clear();
                for (ProjectRoleDTO roleDTO : feedDTO.getProject().getProjectRoles()) {
                    ProjectRole role = projectRoleRepository.findById(roleDTO.getId())
                            .orElseThrow(() -> new NotFoundException("해당 역할을 찾을 수 없습니다."));

                    // Student 객체를 userId를 통해 조회하여 키로 사용
                    Student student = studentRepository.findById(roleDTO.getUserId())
                            .orElseThrow(() -> new NotFoundException("해당 학생을 찾을 수 없습니다."));

                    project.getProjectRoles().put(student, role);
                }
            }

        } else {
            existingFeed.setProject(null);
        }

        // 필요한 유효성 검사
        validateFeed(existingFeed);

        // 업데이트된 기존 Feed 저장
        feedRepository.save(existingFeed);

        // 업데이트된 데이터 다시 조회하여 DTO로 변환
        Feed savedFeed = feedRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
        return feedMapper.toDTO(savedFeed);
    }

    public void deleteFeed(Long id, User userDTO) {
        Feed feed = feedRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 권한 검증
        if (!feed.getUser().equals(user)) {
            throw new UnauthorizedException("게시글을 삭제할 권한이 없습니다.");
        }

        feedRepository.delete(feed);
    }

    public Page<FeedDTO> getAllFeeds(Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findAll(pageable);
        return feedPage.map(feedMapper::toDTO);
    }


    private void validateFeed(Feed feed) {
        if (isNullOrEmpty(feed.getTitle())) {
            throw new BadRequestException("게시글 제목은 필수 입력 사항입니다.");
        }

        if (isNullOrEmpty(feed.getThumbnailImage())) {
            throw new BadRequestException("이미지 URL은 필수 입력 사항입니다.");
        }

        if (!isValidUrl(feed.getThumbnailImage())) {
            throw new BadRequestException("유효하지 않은 이미지 URL입니다. 올바른 URL을 입력하세요.");
        }

        if (isNullOrEmpty(feed.getContent())) {
            throw new BadRequestException("내용은 필수 입력 사항입니다.");
        }

        if (feed.getCategory() == null) {
            throw new BadRequestException("카테고리가 지정되지 않았습니다. 카테고리를 선택하세요.");
        }

        // 프로젝트 또는 전시 정보의 존재 확인
        if (feed.getProject() == null && feed.getExhibition() == null) {
            throw new BadRequestException("프로젝트 또는 전시 정보 중 하나는 필수입니다.");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /*
    private void validateFeed(Feed feed) {
        if (feed.getTitle() == null || feed.getTitle().isEmpty() ||
                feed.getThumbnailImage() == null || feed.getThumbnailImage().isEmpty() ||
                feed.getContent() == null || feed.getContent().isEmpty()) {
            throw new BadRequestException("게시글 제목, 이미지 URL, 내용은 필수 입력 사항입니다.");
        }

        if (!isValidUrl(feed.getThumbnailImage())) {
            throw new BadRequestException("유효하지 않은 이미지 URL입니다. 올바른 URL을 입력하세요.");
        }

        if (feed.getCategory() == null) {
            throw new BadRequestException("카테고리가 지정되지 않았습니다. 카테고리를 선택하세요.");
        }

        // Validate the presence of project or exhibition
        if (feed.getProject() == null && feed.getExhibition() == null) {
            throw new BadRequestException("프로젝트 또는 전시 정보 중 하나는 필수입니다.");
        }
    }

    private boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

     */

    public List<FeedDTO> searchFeedsByHashtag(String hashtag) {
        try {
            // 해시태그 검색
            List<Hashtag> hashtags = hashtagRepository.findByHashtagWithFeeds(hashtag);
            if (hashtags.isEmpty()) {
                throw new NotFoundException("해시태그를 찾을 수 없습니다: " + hashtag);
            }

            // 검색 기록에 해시태그 추가
            searchHistoryService.addSearchHistory(hashtag);

            // 각 Hashtag에 연결된 Feed를 수집하여 변환
            List<Feed> feeds = hashtags.stream()
                    .flatMap(h -> h.getFeeds().stream())
                    .collect(Collectors.toList());

            // 검색된 피드를 DTO로 변환하여 반환
            return feeds.stream()
                    .map(feedMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchHistoryException("피드를 검색하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public FeedHitsDTO incrementHits(Long feedId, User user) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("피드를 찾을 수 없습니다."));

        if (user.getRole() == Role.NORMAL || user.getRole() == Role.STUDENT) {
            feed.setHitsUser(feed.getHitsUser() + 1);
        } else if (user.getRole() == Role.RECRUITER) {
            feed.setHitsRecruiter(feed.getHitsRecruiter() + 1);
        }

        feedRepository.save(feed);

        FeedHitsDTO feedHitsDTO = new FeedHitsDTO();
        feedHitsDTO.setFeedId(feed.getFeedId());
        feedHitsDTO.setHitsUser(feed.getHitsUser());
        feedHitsDTO.setHitsRecruiter(feed.getHitsRecruiter());
        return feedHitsDTO;
    }

    public MemberDTO getProjectTeamMemberInfo(Long feedId, Long userId, User user) {
        // 피드 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("ID가 있는 피드를 찾을 수 없습니다: " + feedId));

        // 프로젝트 확인
        Project project = feed.getProject();
        if (project == null) {
            throw new NotFoundException("피드에 연결된 프로젝트를 찾을 수 없습니다.");
        }

        // 학생 확인
        Student student = project.getProjectRoles().values().stream()
                .filter(role -> role.getStudent() != null && role.getStudent().getUser().getId().equals(userId))
                .map(ProjectRole::getStudent)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("ID가 있는 팀원을 찾을 수 없습니다: " + userId));

        // 참여한 프로젝트 정보 수집
        List<MemberDTO.ProjectInfoDTO> projectInfoList = project.getFeeds().stream()
                .map(f -> {
                    ProjectRole role = project.getProjectRoles().values().stream()
                            .filter(r -> r.getStudent().getUser().getId().equals(userId))
                            .findFirst()
                            .orElse(null);

                    return new MemberDTO.ProjectInfoDTO(
                            f.getTitle(),                        // 프로젝트 제목
                            f.getThumbnailImage(),              // 썸네일 이미지
                            f.getCategory().getName(),          // 카테고리 이름
                            role != null ? role.getRole() : ""  // 학생의 역할
                    );
                })
                .collect(Collectors.toList());

        // 팀원 정보 DTO 생성
        return new MemberDTO(student, projectInfoList);
    }

    public boolean existsById(Long feedId) {
        return feedRepository.existsById(feedId);
    }

    public List<FeedDTO> getFeedsByUsername(String username) {
        List<Feed> feeds = feedRepository.findByUsername(username);
        return feeds.stream()
                .map(feedMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedDTO> getScrappedFeedsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        List<Feed> scrappedFeeds = feedRepository.findScrappedFeedsByUsername(user.getUsername());
        return scrappedFeeds.stream()
                .map(feedMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getScrappedStudentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));

        Long userId = user.getId();
        List<Student> scrappedStudents = scrapRepository.findScrappedStudentsByUserId(userId);
        return scrappedStudents.stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
}