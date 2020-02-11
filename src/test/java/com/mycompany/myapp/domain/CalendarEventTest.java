package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class CalendarEventTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarEvent.class);
        CalendarEvent calendarEvent1 = new CalendarEvent();
        calendarEvent1.setId(1L);
        CalendarEvent calendarEvent2 = new CalendarEvent();
        calendarEvent2.setId(calendarEvent1.getId());
        assertThat(calendarEvent1).isEqualTo(calendarEvent2);
        calendarEvent2.setId(2L);
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);
        calendarEvent1.setId(null);
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);
    }
}
