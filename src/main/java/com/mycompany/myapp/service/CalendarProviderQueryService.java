package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.CalendarProvider;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CalendarProviderRepository;
import com.mycompany.myapp.repository.search.CalendarProviderSearchRepository;
import com.mycompany.myapp.service.dto.CalendarProviderCriteria;
import com.mycompany.myapp.service.dto.CalendarProviderDTO;
import com.mycompany.myapp.service.mapper.CalendarProviderMapper;

/**
 * Service for executing complex queries for {@link CalendarProvider} entities in the database.
 * The main input is a {@link CalendarProviderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CalendarProviderDTO} or a {@link Page} of {@link CalendarProviderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CalendarProviderQueryService extends QueryService<CalendarProvider> {

    private final Logger log = LoggerFactory.getLogger(CalendarProviderQueryService.class);

    private final CalendarProviderRepository calendarProviderRepository;

    private final CalendarProviderMapper calendarProviderMapper;

    private final CalendarProviderSearchRepository calendarProviderSearchRepository;

    public CalendarProviderQueryService(CalendarProviderRepository calendarProviderRepository, CalendarProviderMapper calendarProviderMapper, CalendarProviderSearchRepository calendarProviderSearchRepository) {
        this.calendarProviderRepository = calendarProviderRepository;
        this.calendarProviderMapper = calendarProviderMapper;
        this.calendarProviderSearchRepository = calendarProviderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CalendarProviderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CalendarProviderDTO> findByCriteria(CalendarProviderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CalendarProvider> specification = createSpecification(criteria);
        return calendarProviderMapper.toDto(calendarProviderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CalendarProviderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CalendarProviderDTO> findByCriteria(CalendarProviderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CalendarProvider> specification = createSpecification(criteria);
        return calendarProviderRepository.findAll(specification, page)
            .map(calendarProviderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CalendarProviderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CalendarProvider> specification = createSpecification(criteria);
        return calendarProviderRepository.count(specification);
    }

    /**
     * Function to convert {@link CalendarProviderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CalendarProvider> createSpecification(CalendarProviderCriteria criteria) {
        Specification<CalendarProvider> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CalendarProvider_.id));
            }
            if (criteria.getProvider() != null) {
                specification = specification.and(buildSpecification(criteria.getProvider(), CalendarProvider_.provider));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentifier(), CalendarProvider_.identifier));
            }
            if (criteria.getCredential() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCredential(), CalendarProvider_.credential));
            }
            if (criteria.getCredentialExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCredentialExtra1(), CalendarProvider_.credentialExtra1));
            }
            if (criteria.getCredentialExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCredentialExtra2(), CalendarProvider_.credentialExtra2));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CalendarProvider_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CalendarProvider_.updatedAt));
            }
            if (criteria.getOwnedById() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnedById(),
                    root -> root.join(CalendarProvider_.ownedBy, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
