package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class CalendarProviderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarProvider.class);
        CalendarProvider calendarProvider1 = new CalendarProvider();
        calendarProvider1.setId(1L);
        CalendarProvider calendarProvider2 = new CalendarProvider();
        calendarProvider2.setId(calendarProvider1.getId());
        assertThat(calendarProvider1).isEqualTo(calendarProvider2);
        calendarProvider2.setId(2L);
        assertThat(calendarProvider1).isNotEqualTo(calendarProvider2);
        calendarProvider1.setId(null);
        assertThat(calendarProvider1).isNotEqualTo(calendarProvider2);
    }
}
