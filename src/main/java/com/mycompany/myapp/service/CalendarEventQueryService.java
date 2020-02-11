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

import com.mycompany.myapp.domain.CalendarEvent;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CalendarEventRepository;
import com.mycompany.myapp.repository.search.CalendarEventSearchRepository;
import com.mycompany.myapp.service.dto.CalendarEventCriteria;
import com.mycompany.myapp.service.dto.CalendarEventDTO;
import com.mycompany.myapp.service.mapper.CalendarEventMapper;

/**
 * Service for executing complex queries for {@link CalendarEvent} entities in the database.
 * The main input is a {@link CalendarEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CalendarEventDTO} or a {@link Page} of {@link CalendarEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CalendarEventQueryService extends QueryService<CalendarEvent> {

    private final Logger log = LoggerFactory.getLogger(CalendarEventQueryService.class);

    private final CalendarEventRepository calendarEventRepository;

    private final CalendarEventMapper calendarEventMapper;

    private final CalendarEventSearchRepository calendarEventSearchRepository;

    public CalendarEventQueryService(CalendarEventRepository calendarEventRepository, CalendarEventMapper calendarEventMapper, CalendarEventSearchRepository calendarEventSearchRepository) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventMapper = calendarEventMapper;
        this.calendarEventSearchRepository = calendarEventSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CalendarEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CalendarEventDTO> findByCriteria(CalendarEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventMapper.toDto(calendarEventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CalendarEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CalendarEventDTO> findByCriteria(CalendarEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventRepository.findAll(specification, page)
            .map(calendarEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CalendarEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventRepository.count(specification);
    }

    /**
     * Function to convert {@link CalendarEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CalendarEvent> createSpecification(CalendarEventCriteria criteria) {
        Specification<CalendarEvent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CalendarEvent_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildSpecification(criteria.getUid(), CalendarEvent_.uid));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), CalendarEvent_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), CalendarEvent_.subTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CalendarEvent_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), CalendarEvent_.status));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriority(), CalendarEvent_.priority));
            }
            if (criteria.getPlace() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlace(), CalendarEvent_.place));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), CalendarEvent_.location));
            }
            if (criteria.getCssTheme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCssTheme(), CalendarEvent_.cssTheme));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), CalendarEvent_.url));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), CalendarEvent_.isPublic));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), CalendarEvent_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), CalendarEvent_.endDate));
            }
            if (criteria.getOpeningHours() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpeningHours(), CalendarEvent_.openingHours));
            }
            if (criteria.getImageSha1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageSha1(), CalendarEvent_.imageSha1));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), CalendarEvent_.imageUrl));
            }
            if (criteria.getThumbnailSha1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailSha1(), CalendarEvent_.thumbnailSha1));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CalendarEvent_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CalendarEvent_.updatedAt));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(CalendarEvent_.createdBy, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCalendarId() != null) {
                specification = specification.and(buildSpecification(criteria.getCalendarId(),
                    root -> root.join(CalendarEvent_.calendar, JoinType.LEFT).get(Calendar_.id)));
            }
        }
        return specification;
    }
}
