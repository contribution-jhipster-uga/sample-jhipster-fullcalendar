package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CalendarProviderDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CalendarProvider}.
 */
public interface CalendarProviderService {

    /**
     * Save a calendarProvider.
     *
     * @param calendarProviderDTO the entity to save.
     * @return the persisted entity.
     */
    CalendarProviderDTO save(CalendarProviderDTO calendarProviderDTO);

    /**
     * Get all the calendarProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarProviderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" calendarProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalendarProviderDTO> findOne(Long id);

    /**
     * Delete the "id" calendarProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the calendarProvider corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarProviderDTO> search(String query, Pageable pageable);
}
