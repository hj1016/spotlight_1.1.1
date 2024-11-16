package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.controller.ChatbotController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class IntentService {

    public String determineIntent(Map<String, String> intentDetails, String userInput) {
        String category = intentDetails.get("category");
        String keyword = intentDetails.get("keyword");
        String hashtags = intentDetails.get("hashtags");

        /*
        System.out.println("Extracted hashtags: " + hashtags);
        System.out.println("Extracted category: " + category);
        System.out.println("Extracted keyword: " + keyword);
         */

        if (!hashtags.isEmpty() && (userInput.contains("#") || userInput.contains("해시태그") || userInput.contains("태그"))) {
            return "hashtag_recommendation";
        }

        if (userInput.contains("카테고리") || userInput.contains("분야")) {
            if (!category.isEmpty()) {
                return "category_recommendation";
            }
        }

        if (!keyword.isEmpty() && (userInput.contains("인재") || userInput.contains("학생"))) {
            return "keyword_for_student_recommendation";
        }

        if (!keyword.isEmpty() && (userInput.contains("프로젝트") || userInput.contains("졸업작품") || userInput.contains("졸업 작품"))) {
            return "keyword_recommendation";
        }

        if (!category.isEmpty()) {
            return "category_recommendation";
        }

        if (!keyword.isEmpty()) {
            return "keyword_recommendation";
        }

        return "unknown";
    }

    private static final List<String> CATEGORIES = List.of(
            "언어", "문헌정보", "심리학", "역사", "문학", "종교", "철학", "경영", "경제", "광고/홍보", "행정",
            "국제학", "사회복지", "무역", "법학", "보건행정", "세무/회계", "신문방송", "정치외교", "지리학", "관광",
            "마케팅", "교육", "농업", "물리", "산림/원예", "생명과학", "수의학", "수학", "식품영양", "지구과학",
            "천문", "통계", "화학", "기계", "건축", "반도체", "산업", "신소재", "생명", "소프트웨어", "에너지",
            "재료/금속", "전기/전자", "정보통신", "정보보안", "컴퓨터", "토목", "환경", "화학공학", "보건", "약학",
            "의료공학", "의학", "임상병리", "치의학", "한의학", "공예", "무용", "애니메이션", "순수미술", "연극영화",
            "뷰티", "사진/영상", "산업 디자인", "시각 디자인", "실내 디자인", "패션 디자인", "실용음악", "음악",
            "조형", "체육"
    );

    public Map<String, String> extractIntentDetails(String userInput) {
        String category = extractCategory(userInput);
        String keyword = extractKeyword(userInput);
        List<String> hashtagsList = extractAllHashtags(userInput);

        // 빈 해시태그 제거
        List<String> filteredHashtags = hashtagsList.stream()
                .filter(tag -> !tag.isBlank()) // 공백 또는 빈 값 제거
                .collect(Collectors.toList());

        Map<String, String> details = new HashMap<>();
        details.put("category", category);
        details.put("keyword", keyword);
        details.put("hashtags", String.join(",", filteredHashtags));

        System.out.println("Filtered and joined hashtags: " + details.get("hashtags"));
        return details;
    }

    public String extractKeyword(String userInput) {
        String[] words = userInput.split(" ");
        for (String word : words) {
            if (!word.startsWith("#") && !ChatbotController.STOP_WORDS.contains(word)) {
                return word.replaceAll("[^a-zA-Z0-9가-힣]", "")
                        .replaceAll("(을|를|이|가|은|는)?$", "");
            }
        }
        return "";
    }

    public String extractCategory(String userInput) {
        for (String category : CATEGORIES) {
            if (userInput.contains(category)) {
                return category;
            }
        }
        return ""; // 일치하는 카테고리가 없는 경우 빈 문자열 반환
    }

    public List<String> extractAllHashtags(String userInput) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        Matcher matcher = pattern.matcher(userInput);

        while (matcher.find()) {
            String hashtag = matcher.group().trim();
            hashtags.add(hashtag);
        }

        // 빈 값 및 잘못된 값 필터링
        hashtags = hashtags.stream()
                .filter(tag -> !tag.isBlank())
                .collect(Collectors.toList());

        return hashtags;
    }
}