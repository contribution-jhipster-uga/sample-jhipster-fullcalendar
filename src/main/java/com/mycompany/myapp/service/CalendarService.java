package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CalendarDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Calendar}.
 */
public interface CalendarService {

    /**
     * Save a calendar.
     *
     * @param calendarDTO the entity to save.
     * @return the persisted entity.
     */
    CalendarDTO save(CalendarDTO calendarDTO);

    /**
     * Get all the calendars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarDTO> findAll(Pageable pageable);

    /**
     * Get all the calendars with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<CalendarDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" calendar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalendarDTO> findOne(Long id);

    /**
     * Delete the "id" calendar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the calendar corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarDTO> search(String query, Pageable pageable);
}
