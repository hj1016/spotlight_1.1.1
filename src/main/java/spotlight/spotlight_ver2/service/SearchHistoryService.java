package spotlight.spotlight_ver2.service;

import spotlight.spotlight_ver2.exception.SearchHistoryException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchHistoryService {

    private final int MAX_HISTORY_SIZE = 10;
    private List<String> searchHistory = new LinkedList<>();

    /**
     * 검색 기록을 반환하는 메서드
     *
     * @return 검색 기록 리스트
     */
    public List<String> getSearchHistory() {
        return searchHistory;
    }

    /**
     * 검색 기록에 새로운 검색어를 추가하는 메서드
     *
     * @param searchTerm 추가할 검색어
     * @throws SearchHistoryException 검색 기록 추가 중 오류가 발생한 경우
     */
    public void addSearchHistory(String searchTerm) {
        try {
            // 중복된 검색어가 있으면 삭제
            searchHistory.remove(searchTerm);

            // 검색 기록의 크기가 최대 크기를 초과할 경우 첫 번째 항목을 삭제
            if (searchHistory.size() >= MAX_HISTORY_SIZE) {
                searchHistory.remove(0);
            }

            // 검색어를 기록에 추가
            searchHistory.add(searchTerm);
        } catch (Exception e) {
            throw new SearchHistoryException("검색 기록에 추가하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }
}