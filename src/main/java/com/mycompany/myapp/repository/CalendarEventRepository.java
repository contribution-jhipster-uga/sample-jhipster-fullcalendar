package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CalendarEvent;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CalendarEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long>, JpaSpecificationExecutor<CalendarEvent> {

    @Query("select calendarEvent from CalendarEvent calendarEvent where calendarEvent.createdBy.login = ?#{principal.username}")
    List<CalendarEvent> findByCreatedByIsCurrentUser();

}
