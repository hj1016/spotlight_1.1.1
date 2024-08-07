package spotlight.spotlight_ver2.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExhibitionTest {
    @Test
    public void testExhibitionGettersSetters() {
        Exhibition exhibition = new Exhibition();

        exhibition.setExhibitionId(1L);
        exhibition.setLocation("서울여자대학교 조형예술관");
        exhibition.setSchedule("2024-08-07 ~ 2024-08-10");
        exhibition.setTime("10:00 AM ~ 17:00 PM");

        User user = new User();
        user.setId(1L);
        exhibition.setUser(user);

        Feed feed = new Feed();
        feed.setFeedId(1L);
        exhibition.setFeed(feed);

        assertThat(exhibition.getExhibitionId()).isEqualTo(1L);
        assertThat(exhibition.getLocation()).isEqualTo("서울여자대학교 조형예술관");
        assertThat(exhibition.getSchedule()).isEqualTo("2024-08-07 ~ 2024-08-10");
        assertThat(exhibition.getTime()).isEqualTo("10:00 AM ~ 17:00 PM");
        assertThat(exhibition.getUser().getId()).isEqualTo(1L);
        assertThat(exhibition.getFeed().getFeedId()).isEqualTo(1L);
    }
}