package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarProviderMapperTest {

    private CalendarProviderMapper calendarProviderMapper;

    @BeforeEach
    public void setUp() {
        calendarProviderMapper = new CalendarProviderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(calendarProviderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(calendarProviderMapper.fromId(null)).isNull();
    }
}
