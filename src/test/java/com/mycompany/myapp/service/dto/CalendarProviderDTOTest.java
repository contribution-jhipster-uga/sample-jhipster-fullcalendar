package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class CalendarProviderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarProviderDTO.class);
        CalendarProviderDTO calendarProviderDTO1 = new CalendarProviderDTO();
        calendarProviderDTO1.setId(1L);
        CalendarProviderDTO calendarProviderDTO2 = new CalendarProviderDTO();
        assertThat(calendarProviderDTO1).isNotEqualTo(calendarProviderDTO2);
        calendarProviderDTO2.setId(calendarProviderDTO1.getId());
        assertThat(calendarProviderDTO1).isEqualTo(calendarProviderDTO2);
        calendarProviderDTO2.setId(2L);
        assertThat(calendarProviderDTO1).isNotEqualTo(calendarProviderDTO2);
        calendarProviderDTO1.setId(null);
        assertThat(calendarProviderDTO1).isNotEqualTo(calendarProviderDTO2);
    }
}
