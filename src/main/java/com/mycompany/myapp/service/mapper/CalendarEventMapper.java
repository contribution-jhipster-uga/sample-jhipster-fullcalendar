package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CalendarEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalendarEvent} and its DTO {@link CalendarEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CalendarMapper.class})
public interface CalendarEventMapper extends EntityMapper<CalendarEventDTO, CalendarEvent> {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "calendar.id", target = "calendarId")
    @Mapping(source = "calendar.title", target = "calendarTitle")
    CalendarEventDTO toDto(CalendarEvent calendarEvent);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "calendarId", target = "calendar")
    CalendarEvent toEntity(CalendarEventDTO calendarEventDTO);

    default CalendarEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setId(id);
        return calendarEvent;
    }
}
