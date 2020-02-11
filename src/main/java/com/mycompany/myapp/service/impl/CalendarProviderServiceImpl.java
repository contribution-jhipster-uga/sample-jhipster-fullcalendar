package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CalendarProviderService;
import com.mycompany.myapp.domain.CalendarProvider;
import com.mycompany.myapp.repository.CalendarProviderRepository;
import com.mycompany.myapp.repository.search.CalendarProviderSearchRepository;
import com.mycompany.myapp.service.dto.CalendarProviderDTO;
import com.mycompany.myapp.service.mapper.CalendarProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CalendarProvider}.
 */
@Service
@Transactional
public class CalendarProviderServiceImpl implements CalendarProviderService {

    private final Logger log = LoggerFactory.getLogger(CalendarProviderServiceImpl.class);

    private final CalendarProviderRepository calendarProviderRepository;

    private final CalendarProviderMapper calendarProviderMapper;

    private final CalendarProviderSearchRepository calendarProviderSearchRepository;

    public CalendarProviderServiceImpl(CalendarProviderRepository calendarProviderRepository, CalendarProviderMapper calendarProviderMapper, CalendarProviderSearchRepository calendarProviderSearchRepository) {
        this.calendarProviderRepository = calendarProviderRepository;
        this.calendarProviderMapper = calendarProviderMapper;
        this.calendarProviderSearchRepository = calendarProviderSearchRepository;
    }

    /**
     * Save a calendarProvider.
     *
     * @param calendarProviderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CalendarProviderDTO save(CalendarProviderDTO calendarProviderDTO) {
        log.debug("Request to save CalendarProvider : {}", calendarProviderDTO);
        CalendarProvider calendarProvider = calendarProviderMapper.toEntity(calendarProviderDTO);
        calendarProvider = calendarProviderRepository.save(calendarProvider);
        CalendarProviderDTO result = calendarProviderMapper.toDto(calendarProvider);
        calendarProviderSearchRepository.save(calendarProvider);
        return result;
    }

    /**
     * Get all the calendarProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CalendarProviders");
        return calendarProviderRepository.findAll(pageable)
            .map(calendarProviderMapper::toDto);
    }

    /**
     * Get one calendarProvider by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarProviderDTO> findOne(Long id) {
        log.debug("Request to get CalendarProvider : {}", id);
        return calendarProviderRepository.findById(id)
            .map(calendarProviderMapper::toDto);
    }

    /**
     * Delete the calendarProvider by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CalendarProvider : {}", id);
        calendarProviderRepository.deleteById(id);
        calendarProviderSearchRepository.deleteById(id);
    }

    /**
     * Search for the calendarProvider corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CalendarProviderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CalendarProviders for query {}", query);
        return calendarProviderSearchRepository.search(queryStringQuery(query), pageable)
            .map(calendarProviderMapper::toDto);
    }
}
