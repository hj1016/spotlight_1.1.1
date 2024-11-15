package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.controller.ChatbotController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentService {

    public String determineIntent(Map<String, String> intentDetails, String userInput) {
        String category = intentDetails.get("category");
        String keyword = intentDetails.get("keyword");
        String hashtags = intentDetails.get("hashtags");

        // 1. 사용자 입력에 "카테고리", "분야" 라는 단어가 포함된 경우 → 카테고리 추천
        if (userInput.contains("카테고리") || userInput.contains("분야")) {
            if (!category.isEmpty()) {
                return "category_recommendation";
            }
        }

        // 2. 키워드와 "인재" 또는 "학생" 언급된 경우 → keyword_for_student_recommendation
        if (!keyword.isEmpty() && (userInput.contains("인재") || userInput.contains("학생"))) {
            return "keyword_for_student_recommendation";
        }

        // 3. "#" 또는 "태그", "해시태그" 포함된 경우 → hashtag_recommendation
        if (!hashtags.isEmpty() &&
                (userInput.contains("#") || userInput.contains("태그") || userInput.contains("해시태그"))) {
            return "hashtag_recommendation";
        }

        // 4. "프로젝트", "졸업 작품" 포함된 경우 → keyword_recommendation
        if (!keyword.isEmpty() &&
                (userInput.contains("프로젝트") || userInput.contains("졸업작품") || userInput.contains("졸업 작품"))) {
            return "keyword_recommendation";
        }

        // 5. 카테고리가 추출되었을 경우
        if (!category.isEmpty()) {
            return "category_recommendation";
        }

        // 6. 키워드가 추출되었을 경우
        if (!keyword.isEmpty()) {
            return "keyword_recommendation";
        }

        return "unknown"; // 아무것도 매칭되지 않을 경우
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
        List<String> hashtags = extractAllHashtags(userInput);

        Map<String, String> details = new HashMap<>();
        details.put("category", category); // 추출된 카테고리
        details.put("keyword", keyword);   // 추출된 키워드
        details.put("hashtags", String.join(",", hashtags)); // 해시태그 목록 (필요 시)

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
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(userInput);
        while (matcher.find()) {
            hashtags.add(matcher.group(1)
                    .replaceAll("[^a-zA-Z0-9가-힣]", "")
                    .replaceAll("(을|를|이|가|은|는)?$", ""));
        }
        return hashtags;
    }

}