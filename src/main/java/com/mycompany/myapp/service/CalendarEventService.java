package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CalendarEventDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CalendarEvent}.
 */
public interface CalendarEventService {

    /**
     * Save a calendarEvent.
     *
     * @param calendarEventDTO the entity to save.
     * @return the persisted entity.
     */
    CalendarEventDTO save(CalendarEventDTO calendarEventDTO);

    /**
     * Get all the calendarEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" calendarEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalendarEventDTO> findOne(Long id);

    /**
     * Delete the "id" calendarEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the calendarEvent corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarEventDTO> search(String query, Pageable pageable);
}
