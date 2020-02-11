package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CalendarProvider;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CalendarProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarProviderRepository extends JpaRepository<CalendarProvider, Long>, JpaSpecificationExecutor<CalendarProvider> {

    @Query("select calendarProvider from CalendarProvider calendarProvider where calendarProvider.ownedBy.login = ?#{principal.username}")
    List<CalendarProvider> findByOwnedByIsCurrentUser();

}
