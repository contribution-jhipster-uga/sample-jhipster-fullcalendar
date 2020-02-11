package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CalendarService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CalendarDTO;
import com.mycompany.myapp.service.dto.CalendarCriteria;
import com.mycompany.myapp.service.CalendarQueryService;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Calendar}.
 */
@RestController
@RequestMapping("/api")
public class CalendarResource {

    private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

    private static final String ENTITY_NAME = "calendar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarService calendarService;

    private final CalendarQueryService calendarQueryService;

    public CalendarResource(CalendarService calendarService, CalendarQueryService calendarQueryService) {
        this.calendarService = calendarService;
        this.calendarQueryService = calendarQueryService;
    }

    /**
     * {@code POST  /calendars} : Create a new calendar.
     *
     * @param calendarDTO the calendarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarDTO, or with status {@code 400 (Bad Request)} if the calendar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendars")
    public ResponseEntity<CalendarDTO> createCalendar(@Valid @RequestBody CalendarDTO calendarDTO) throws URISyntaxException {
        log.debug("REST request to save Calendar : {}", calendarDTO);
        if (calendarDTO.getId() != null) {
            throw new BadRequestAlertException("A new calendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarDTO result = calendarService.save(calendarDTO);
        return ResponseEntity.created(new URI("/api/calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendars} : Updates an existing calendar.
     *
     * @param calendarDTO the calendarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarDTO,
     * or with status {@code 400 (Bad Request)} if the calendarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendars")
    public ResponseEntity<CalendarDTO> updateCalendar(@Valid @RequestBody CalendarDTO calendarDTO) throws URISyntaxException {
        log.debug("REST request to update Calendar : {}", calendarDTO);
        if (calendarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalendarDTO result = calendarService.save(calendarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calendars} : get all the calendars.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendars in body.
     */
    @GetMapping("/calendars")
    public ResponseEntity<List<CalendarDTO>> getAllCalendars(CalendarCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Calendars by criteria: {}", criteria);
        Page<CalendarDTO> page = calendarQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calendars/count} : count all the calendars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/calendars/count")
    public ResponseEntity<Long> countCalendars(CalendarCriteria criteria) {
        log.debug("REST request to count Calendars by criteria: {}", criteria);
        return ResponseEntity.ok().body(calendarQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /calendars/:id} : get the "id" calendar.
     *
     * @param id the id of the calendarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendars/{id}")
    public ResponseEntity<CalendarDTO> getCalendar(@PathVariable Long id) {
        log.debug("REST request to get Calendar : {}", id);
        Optional<CalendarDTO> calendarDTO = calendarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calendarDTO);
    }

    /**
     * {@code DELETE  /calendars/:id} : delete the "id" calendar.
     *
     * @param id the id of the calendarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendars/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        log.debug("REST request to delete Calendar : {}", id);
        calendarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/calendars?query=:query} : search for the calendar corresponding
     * to the query.
     *
     * @param query the query of the calendar search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/calendars")
    public ResponseEntity<List<CalendarDTO>> searchCalendars(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Calendars for query {}", query);
        Page<CalendarDTO> page = calendarService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
