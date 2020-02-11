package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CalendarService;
import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.repository.CalendarRepository;
import com.mycompany.myapp.repository.search.CalendarSearchRepository;
import com.mycompany.myapp.service.dto.CalendarDTO;
import com.mycompany.myapp.service.mapper.CalendarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Calendar}.
 */
@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {

    private final Logger log = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private final CalendarRepository calendarRepository;

    private final CalendarMapper calendarMapper;

    private final CalendarSearchRepository calendarSearchRepository;

    public CalendarServiceImpl(CalendarRepository calendarRepository, CalendarMapper calendarMapper, CalendarSearchRepository calendarSearchRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
        this.calendarSearchRepository = calendarSearchRepository;
    }

    /**
     * Save a calendar.
     *
     * @param calendarDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CalendarDTO save(CalendarDTO calendarDTO) {
        log.debug("Request to save Calendar : {}", calendarDTO);
        Calendar calendar = calendarMapper.toEntity(calendarDTO);
        calendar = calendarRepository.save(calendar);
        CalendarDTO result = calendarMapper.toDto(calendar);
        calendarSearchRepository.save(calendar);
        return result;
    }

    /**
     * Get all the calendars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Calendars");
        return calendarRepository.findAll(pageable)
            .map(calendarMapper::toDto);
    }

    /**
     * Get all the calendars with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CalendarDTO> findAllWithEagerRelationships(Pageable pageable) {
        return calendarRepository.findAllWithEagerRelationships(pageable).map(calendarMapper::toDto);
    }

    /**
     * Get one calendar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarDTO> findOne(Long id) {
        log.debug("Request to get Calendar : {}", id);
        return calendarRepository.findOneWithEagerRelationships(id)
            .map(calendarMapper::toDto);
    }

    /**
     * Delete the calendar by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calendar : {}", id);
        calendarRepository.deleteById(id);
        calendarSearchRepository.deleteById(id);
    }

    /**
     * Search for the calendar corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Calendars for query {}", query);
        return calendarSearchRepository.search(queryStringQuery(query), pageable)
            .map(calendarMapper::toDto);
    }
}
