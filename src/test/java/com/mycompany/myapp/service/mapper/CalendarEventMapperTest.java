package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarEventMapperTest {

    private CalendarEventMapper calendarEventMapper;

    @BeforeEach
    public void setUp() {
        calendarEventMapper = new CalendarEventMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(calendarEventMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(calendarEventMapper.fromId(null)).isNull();
    }
}
