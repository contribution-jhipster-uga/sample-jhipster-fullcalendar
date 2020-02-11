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

import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CalendarRepository;
import com.mycompany.myapp.repository.search.CalendarSearchRepository;
import com.mycompany.myapp.service.dto.CalendarCriteria;
import com.mycompany.myapp.service.dto.CalendarDTO;
import com.mycompany.myapp.service.mapper.CalendarMapper;

/**
 * Service for executing complex queries for {@link Calendar} entities in the database.
 * The main input is a {@link CalendarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CalendarDTO} or a {@link Page} of {@link CalendarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CalendarQueryService extends QueryService<Calendar> {

    private final Logger log = LoggerFactory.getLogger(CalendarQueryService.class);

    private final CalendarRepository calendarRepository;

    private final CalendarMapper calendarMapper;

    private final CalendarSearchRepository calendarSearchRepository;

    public CalendarQueryService(CalendarRepository calendarRepository, CalendarMapper calendarMapper, CalendarSearchRepository calendarSearchRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
        this.calendarSearchRepository = calendarSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CalendarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CalendarDTO> findByCriteria(CalendarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarMapper.toDto(calendarRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CalendarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CalendarDTO> findByCriteria(CalendarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarRepository.findAll(specification, page)
            .map(calendarMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CalendarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarRepository.count(specification);
    }

    /**
     * Function to convert {@link CalendarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Calendar> createSpecification(CalendarCriteria criteria) {
        Specification<Calendar> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Calendar_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildSpecification(criteria.getUid(), Calendar_.uid));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Calendar_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), Calendar_.subTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Calendar_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Calendar_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Calendar_.updatedAt));
            }
            if (criteria.getOwnedById() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnedById(),
                    root -> root.join(Calendar_.ownedBy, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getSharedWithId() != null) {
                specification = specification.and(buildSpecification(criteria.getSharedWithId(),
                    root -> root.join(Calendar_.sharedWiths, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
