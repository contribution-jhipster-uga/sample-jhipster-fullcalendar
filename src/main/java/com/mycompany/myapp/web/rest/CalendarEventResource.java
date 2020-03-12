package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CalendarEventService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CalendarEventDTO;
import com.mycompany.myapp.service.dto.CalendarEventCriteria;
import com.mycompany.myapp.service.CalendarEventQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.MapTimeZoneCache;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CalendarEvent}.
 */
@RestController
@RequestMapping("/api")
public class CalendarEventResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);

    private static final String ENTITY_NAME = "calendarEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarEventService calendarEventService;

    private final CalendarEventQueryService calendarEventQueryService;

    public CalendarEventResource(CalendarEventService calendarEventService, CalendarEventQueryService calendarEventQueryService) {
        this.calendarEventService = calendarEventService;
        this.calendarEventQueryService = calendarEventQueryService;
    }

    /**
     * {@code POST  /calendar-events} : Create a new calendarEvent.
     *
     * @param calendarEventDTO the calendarEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarEventDTO, or with status {@code 400 (Bad Request)} if the calendarEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendar-events")
    public ResponseEntity<CalendarEventDTO> createCalendarEvent(@Valid @RequestBody CalendarEventDTO calendarEventDTO) throws URISyntaxException {
        log.debug("REST request to save CalendarEvent : {}", calendarEventDTO);
        if (calendarEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new calendarEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarEventDTO result = calendarEventService.save(calendarEventDTO);
        return ResponseEntity.created(new URI("/api/calendar-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendar-events} : Updates an existing calendarEvent.
     *
     * @param calendarEventDTO the calendarEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarEventDTO,
     * or with status {@code 400 (Bad Request)} if the calendarEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendar-events")
    public ResponseEntity<CalendarEventDTO> updateCalendarEvent(@Valid @RequestBody CalendarEventDTO calendarEventDTO) throws URISyntaxException {
        log.debug("REST request to update CalendarEvent : {}", calendarEventDTO);
        if (calendarEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalendarEventDTO result = calendarEventService.save(calendarEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calendar-events} : get all the calendarEvents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendarEvents in body.
     */
    @GetMapping("/calendar-events")
    public ResponseEntity<List<CalendarEventDTO>> getAllCalendarEvents(CalendarEventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CalendarEvents by criteria: {}", criteria);
        Page<CalendarEventDTO> page = calendarEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calendar-events/count} : count all the calendarEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/calendar-events/count")
    public ResponseEntity<Long> countCalendarEvents(CalendarEventCriteria criteria) {
        log.debug("REST request to count CalendarEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(calendarEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /calendar-events/:id} : get the "id" calendarEvent.
     *
     * @param id the id of the calendarEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendar-events/{id}")
    public ResponseEntity<CalendarEventDTO> getCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to get CalendarEvent : {}", id);
        Optional<CalendarEventDTO> calendarEventDTO = calendarEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calendarEventDTO);
    }

    /**
     * {@code DELETE  /calendar-events/:id} : delete the "id" calendarEvent.
     *
     * @param id the id of the calendarEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendar-events/{id}")
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to delete CalendarEvent : {}", id);
        calendarEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/calendar-events?query=:query} : search for the calendarEvent corresponding
     * to the query.
     *
     * @param query the query of the calendarEvent search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/calendar-events")
    public ResponseEntity<List<CalendarEventDTO>> searchCalendarEvents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CalendarEvents for query {}", query);
        Page<CalendarEventDTO> page = calendarEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
