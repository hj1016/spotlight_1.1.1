package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.controller.ChatbotController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentService {

    public String determineIntent(String userInput) {
        List<String> hashtags = extractAllHashtags(userInput);

        // 1. "카테고리" 또는 "분야"가 있는 경우 category_recommendation 우선 처리
        if ((userInput.contains("카테고리") || userInput.contains("분야")) && hashtags.isEmpty()) {
            return "category_recommendation";
        }

        // 2. "인재", "학생"을 포함한 경우 keyword_for_student_recommendation 우선 처리
        if (userInput.contains("인재") || userInput.contains("학생")) {
            if (!extractKeyword(userInput).isEmpty()) {
                return "keyword_for_student_recommendation";
            } else if (!hashtags.isEmpty()) {
                return "hashtag_recommendation";
            } else {
                return "category_recommendation";  // 기본적으로 카테고리 추천
            }
        }

        // 3. "프로젝트", "추천", "졸업 작품"을 포함한 경우 keyword_recommendation 처리
        if (userInput.contains("프로젝트") || userInput.contains("추천") || userInput.contains("졸업 작품")) {
            if (!extractKeyword(userInput).isEmpty()) {
                return "keyword_recommendation";
            }
        }

        // 4. 해시태그를 기반으로 hashtag_recommendation 처리
        if (userInput.contains("#") || userInput.contains("해시태그") || userInput.contains("태그")) {
            if (!hashtags.isEmpty()) {
                return "hashtag_recommendation";
            }
        }

        return "unknown";
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