package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CalendarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Calendar} and its DTO {@link CalendarDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CalendarMapper extends EntityMapper<CalendarDTO, Calendar> {

    @Mapping(source = "ownedBy.id", target = "ownedById")
    @Mapping(source = "ownedBy.login", target = "ownedByLogin")
    CalendarDTO toDto(Calendar calendar);

    @Mapping(source = "ownedById", target = "ownedBy")
    @Mapping(target = "removeSharedWith", ignore = true)
    Calendar toEntity(CalendarDTO calendarDTO);

    default Calendar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Calendar calendar = new Calendar();
        calendar.setId(id);
        return calendar;
    }
}
