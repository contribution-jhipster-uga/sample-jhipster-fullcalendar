package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CalendarEventService;
import com.mycompany.myapp.domain.CalendarEvent;
import com.mycompany.myapp.repository.CalendarEventRepository;
import com.mycompany.myapp.repository.search.CalendarEventSearchRepository;
import com.mycompany.myapp.service.dto.CalendarEventDTO;
import com.mycompany.myapp.service.mapper.CalendarEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CalendarEvent}.
 */
@Service
@Transactional
public class CalendarEventServiceImpl implements CalendarEventService {

    private final Logger log = LoggerFactory.getLogger(CalendarEventServiceImpl.class);

    private final CalendarEventRepository calendarEventRepository;

    private final CalendarEventMapper calendarEventMapper;

    private final CalendarEventSearchRepository calendarEventSearchRepository;

    public CalendarEventServiceImpl(CalendarEventRepository calendarEventRepository, CalendarEventMapper calendarEventMapper, CalendarEventSearchRepository calendarEventSearchRepository) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventMapper = calendarEventMapper;
        this.calendarEventSearchRepository = calendarEventSearchRepository;
    }

    /**
     * Save a calendarEvent.
     *
     * @param calendarEventDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CalendarEventDTO save(CalendarEventDTO calendarEventDTO) {
        log.debug("Request to save CalendarEvent : {}", calendarEventDTO);
        CalendarEvent calendarEvent = calendarEventMapper.toEntity(calendarEventDTO);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        CalendarEventDTO result = calendarEventMapper.toDto(calendarEvent);
        calendarEventSearchRepository.save(calendarEvent);
        return result;
    }

    /**
     * Get all the calendarEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CalendarEvents");
        return calendarEventRepository.findAll(pageable)
            .map(calendarEventMapper::toDto);
    }

    /**
     * Get one calendarEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarEventDTO> findOne(Long id) {
        log.debug("Request to get CalendarEvent : {}", id);
        return calendarEventRepository.findById(id)
            .map(calendarEventMapper::toDto);
    }

    /**
     * Delete the calendarEvent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CalendarEvent : {}", id);
        calendarEventRepository.deleteById(id);
        calendarEventSearchRepository.deleteById(id);
    }

    /**
     * Search for the calendarEvent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CalendarEvents for query {}", query);
        return calendarEventSearchRepository.search(queryStringQuery(query), pageable)
            .map(calendarEventMapper::toDto);
    }
}
