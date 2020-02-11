package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CalendarProviderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalendarProvider} and its DTO {@link CalendarProviderDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CalendarProviderMapper extends EntityMapper<CalendarProviderDTO, CalendarProvider> {

    @Mapping(source = "ownedBy.id", target = "ownedById")
    @Mapping(source = "ownedBy.login", target = "ownedByLogin")
    CalendarProviderDTO toDto(CalendarProvider calendarProvider);

    @Mapping(source = "ownedById", target = "ownedBy")
    CalendarProvider toEntity(CalendarProviderDTO calendarProviderDTO);

    default CalendarProvider fromId(Long id) {
        if (id == null) {
            return null;
        }
        CalendarProvider calendarProvider = new CalendarProvider();
        calendarProvider.setId(id);
        return calendarProvider;
    }
}
