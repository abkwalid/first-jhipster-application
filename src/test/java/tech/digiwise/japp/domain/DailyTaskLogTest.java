package tech.digiwise.japp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.digiwise.japp.web.rest.TestUtil;

class DailyTaskLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyTaskLog.class);
        DailyTaskLog dailyTaskLog1 = new DailyTaskLog();
        dailyTaskLog1.setId(1L);
        DailyTaskLog dailyTaskLog2 = new DailyTaskLog();
        dailyTaskLog2.setId(dailyTaskLog1.getId());
        assertThat(dailyTaskLog1).isEqualTo(dailyTaskLog2);
        dailyTaskLog2.setId(2L);
        assertThat(dailyTaskLog1).isNotEqualTo(dailyTaskLog2);
        dailyTaskLog1.setId(null);
        assertThat(dailyTaskLog1).isNotEqualTo(dailyTaskLog2);
    }
}
