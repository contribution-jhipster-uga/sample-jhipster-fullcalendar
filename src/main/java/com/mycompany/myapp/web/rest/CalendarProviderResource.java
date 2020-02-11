package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CalendarProviderService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CalendarProviderDTO;
import com.mycompany.myapp.service.dto.CalendarProviderCriteria;
import com.mycompany.myapp.service.CalendarProviderQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.CalendarProvider}.
 */
@RestController
@RequestMapping("/api")
public class CalendarProviderResource {

    private final Logger log = LoggerFactory.getLogger(CalendarProviderResource.class);

    private static final String ENTITY_NAME = "calendarProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarProviderService calendarProviderService;

    private final CalendarProviderQueryService calendarProviderQueryService;

    public CalendarProviderResource(CalendarProviderService calendarProviderService, CalendarProviderQueryService calendarProviderQueryService) {
        this.calendarProviderService = calendarProviderService;
        this.calendarProviderQueryService = calendarProviderQueryService;
    }

    /**
     * {@code POST  /calendar-providers} : Create a new calendarProvider.
     *
     * @param calendarProviderDTO the calendarProviderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarProviderDTO, or with status {@code 400 (Bad Request)} if the calendarProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendar-providers")
    public ResponseEntity<CalendarProviderDTO> createCalendarProvider(@Valid @RequestBody CalendarProviderDTO calendarProviderDTO) throws URISyntaxException {
        log.debug("REST request to save CalendarProvider : {}", calendarProviderDTO);
        if (calendarProviderDTO.getId() != null) {
            throw new BadRequestAlertException("A new calendarProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarProviderDTO result = calendarProviderService.save(calendarProviderDTO);
        return ResponseEntity.created(new URI("/api/calendar-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendar-providers} : Updates an existing calendarProvider.
     *
     * @param calendarProviderDTO the calendarProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarProviderDTO,
     * or with status {@code 400 (Bad Request)} if the calendarProviderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendar-providers")
    public ResponseEntity<CalendarProviderDTO> updateCalendarProvider(@Valid @RequestBody CalendarProviderDTO calendarProviderDTO) throws URISyntaxException {
        log.debug("REST request to update CalendarProvider : {}", calendarProviderDTO);
        if (calendarProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalendarProviderDTO result = calendarProviderService.save(calendarProviderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarProviderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calendar-providers} : get all the calendarProviders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendarProviders in body.
     */
    @GetMapping("/calendar-providers")
    public ResponseEntity<List<CalendarProviderDTO>> getAllCalendarProviders(CalendarProviderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CalendarProviders by criteria: {}", criteria);
        Page<CalendarProviderDTO> page = calendarProviderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calendar-providers/count} : count all the calendarProviders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/calendar-providers/count")
    public ResponseEntity<Long> countCalendarProviders(CalendarProviderCriteria criteria) {
        log.debug("REST request to count CalendarProviders by criteria: {}", criteria);
        return ResponseEntity.ok().body(calendarProviderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /calendar-providers/:id} : get the "id" calendarProvider.
     *
     * @param id the id of the calendarProviderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarProviderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendar-providers/{id}")
    public ResponseEntity<CalendarProviderDTO> getCalendarProvider(@PathVariable Long id) {
        log.debug("REST request to get CalendarProvider : {}", id);
        Optional<CalendarProviderDTO> calendarProviderDTO = calendarProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calendarProviderDTO);
    }

    /**
     * {@code DELETE  /calendar-providers/:id} : delete the "id" calendarProvider.
     *
     * @param id the id of the calendarProviderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendar-providers/{id}")
    public ResponseEntity<Void> deleteCalendarProvider(@PathVariable Long id) {
        log.debug("REST request to delete CalendarProvider : {}", id);
        calendarProviderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/calendar-providers?query=:query} : search for the calendarProvider corresponding
     * to the query.
     *
     * @param query the query of the calendarProvider search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/calendar-providers")
    public ResponseEntity<List<CalendarProviderDTO>> searchCalendarProviders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CalendarProviders for query {}", query);
        Page<CalendarProviderDTO> page = calendarProviderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
