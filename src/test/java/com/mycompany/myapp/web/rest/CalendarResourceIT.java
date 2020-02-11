package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FullCalendarApp;
import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.CalendarRepository;
import com.mycompany.myapp.repository.search.CalendarSearchRepository;
import com.mycompany.myapp.service.CalendarService;
import com.mycompany.myapp.service.dto.CalendarDTO;
import com.mycompany.myapp.service.mapper.CalendarMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.CalendarCriteria;
import com.mycompany.myapp.service.CalendarQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CalendarResource} REST controller.
 */
@SpringBootTest(classes = FullCalendarApp.class)
public class CalendarResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CalendarRepository calendarRepository;

    @Mock
    private CalendarRepository calendarRepositoryMock;

    @Autowired
    private CalendarMapper calendarMapper;

    @Mock
    private CalendarService calendarServiceMock;

    @Autowired
    private CalendarService calendarService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CalendarSearchRepositoryMockConfiguration
     */
    @Autowired
    private CalendarSearchRepository mockCalendarSearchRepository;

    @Autowired
    private CalendarQueryService calendarQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCalendarMockMvc;

    private Calendar calendar;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalendarResource calendarResource = new CalendarResource(calendarService, calendarQueryService);
        this.restCalendarMockMvc = MockMvcBuilders.standaloneSetup(calendarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createEntity(EntityManager em) {
        Calendar calendar = new Calendar()
            .uid(DEFAULT_UID)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return calendar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createUpdatedEntity(EntityManager em) {
        Calendar calendar = new Calendar()
            .uid(UPDATED_UID)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return calendar;
    }

    @BeforeEach
    public void initTest() {
        calendar = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalendar() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);
        restCalendarMockMvc.perform(post("/api/calendars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isCreated());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate + 1);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCalendar.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCalendar.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCalendar.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalendar.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testCalendar.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendar.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Calendar in Elasticsearch
        verify(mockCalendarSearchRepository, times(1)).save(testCalendar);
    }

    @Test
    @Transactional
    public void createCalendarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // Create the Calendar with an existing ID
        calendar.setId(1L);
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarMockMvc.perform(post("/api/calendars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate);

        // Validate the Calendar in Elasticsearch
        verify(mockCalendarSearchRepository, times(0)).save(calendar);
    }


    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarRepository.findAll().size();
        // set the field null
        calendar.setCreatedAt(null);

        // Create the Calendar, which fails.
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        restCalendarMockMvc.perform(post("/api/calendars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalendars() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList
        restCalendarMockMvc.perform(get("/api/calendars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCalendarsWithEagerRelationshipsIsEnabled() throws Exception {
        CalendarResource calendarResource = new CalendarResource(calendarServiceMock, calendarQueryService);
        when(calendarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCalendarMockMvc = MockMvcBuilders.standaloneSetup(calendarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCalendarMockMvc.perform(get("/api/calendars?eagerload=true"))
        .andExpect(status().isOk());

        verify(calendarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCalendarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        CalendarResource calendarResource = new CalendarResource(calendarServiceMock, calendarQueryService);
            when(calendarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCalendarMockMvc = MockMvcBuilders.standaloneSetup(calendarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCalendarMockMvc.perform(get("/api/calendars?eagerload=true"))
        .andExpect(status().isOk());

            verify(calendarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get the calendar
        restCalendarMockMvc.perform(get("/api/calendars/{id}", calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendar.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getCalendarsByIdFiltering() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        Long id = calendar.getId();

        defaultCalendarShouldBeFound("id.equals=" + id);
        defaultCalendarShouldNotBeFound("id.notEquals=" + id);

        defaultCalendarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalendarShouldNotBeFound("id.greaterThan=" + id);

        defaultCalendarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalendarShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCalendarsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uid equals to DEFAULT_UID
        defaultCalendarShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the calendarList where uid equals to UPDATED_UID
        defaultCalendarShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uid not equals to DEFAULT_UID
        defaultCalendarShouldNotBeFound("uid.notEquals=" + DEFAULT_UID);

        // Get all the calendarList where uid not equals to UPDATED_UID
        defaultCalendarShouldBeFound("uid.notEquals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uid in DEFAULT_UID or UPDATED_UID
        defaultCalendarShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the calendarList where uid equals to UPDATED_UID
        defaultCalendarShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uid is not null
        defaultCalendarShouldBeFound("uid.specified=true");

        // Get all the calendarList where uid is null
        defaultCalendarShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title equals to DEFAULT_TITLE
        defaultCalendarShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the calendarList where title equals to UPDATED_TITLE
        defaultCalendarShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title not equals to DEFAULT_TITLE
        defaultCalendarShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the calendarList where title not equals to UPDATED_TITLE
        defaultCalendarShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCalendarShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the calendarList where title equals to UPDATED_TITLE
        defaultCalendarShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title is not null
        defaultCalendarShouldBeFound("title.specified=true");

        // Get all the calendarList where title is null
        defaultCalendarShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarsByTitleContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title contains DEFAULT_TITLE
        defaultCalendarShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the calendarList where title contains UPDATED_TITLE
        defaultCalendarShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where title does not contain DEFAULT_TITLE
        defaultCalendarShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the calendarList where title does not contain UPDATED_TITLE
        defaultCalendarShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCalendarsBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle equals to DEFAULT_SUB_TITLE
        defaultCalendarShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the calendarList where subTitle equals to UPDATED_SUB_TITLE
        defaultCalendarShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsBySubTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle not equals to DEFAULT_SUB_TITLE
        defaultCalendarShouldNotBeFound("subTitle.notEquals=" + DEFAULT_SUB_TITLE);

        // Get all the calendarList where subTitle not equals to UPDATED_SUB_TITLE
        defaultCalendarShouldBeFound("subTitle.notEquals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultCalendarShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the calendarList where subTitle equals to UPDATED_SUB_TITLE
        defaultCalendarShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle is not null
        defaultCalendarShouldBeFound("subTitle.specified=true");

        // Get all the calendarList where subTitle is null
        defaultCalendarShouldNotBeFound("subTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarsBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle contains DEFAULT_SUB_TITLE
        defaultCalendarShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the calendarList where subTitle contains UPDATED_SUB_TITLE
        defaultCalendarShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarsBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultCalendarShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the calendarList where subTitle does not contain UPDATED_SUB_TITLE
        defaultCalendarShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }


    @Test
    @Transactional
    public void getAllCalendarsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description equals to DEFAULT_DESCRIPTION
        defaultCalendarShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the calendarList where description equals to UPDATED_DESCRIPTION
        defaultCalendarShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description not equals to DEFAULT_DESCRIPTION
        defaultCalendarShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the calendarList where description not equals to UPDATED_DESCRIPTION
        defaultCalendarShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCalendarShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the calendarList where description equals to UPDATED_DESCRIPTION
        defaultCalendarShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description is not null
        defaultCalendarShouldBeFound("description.specified=true");

        // Get all the calendarList where description is null
        defaultCalendarShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description contains DEFAULT_DESCRIPTION
        defaultCalendarShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the calendarList where description contains UPDATED_DESCRIPTION
        defaultCalendarShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where description does not contain DEFAULT_DESCRIPTION
        defaultCalendarShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the calendarList where description does not contain UPDATED_DESCRIPTION
        defaultCalendarShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCalendarsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where createdAt equals to DEFAULT_CREATED_AT
        defaultCalendarShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the calendarList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where createdAt not equals to DEFAULT_CREATED_AT
        defaultCalendarShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the calendarList where createdAt not equals to UPDATED_CREATED_AT
        defaultCalendarShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCalendarShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the calendarList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where createdAt is not null
        defaultCalendarShouldBeFound("createdAt.specified=true");

        // Get all the calendarList where createdAt is null
        defaultCalendarShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCalendarShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultCalendarShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultCalendarShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCalendarShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the calendarList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where updatedAt is not null
        defaultCalendarShouldBeFound("updatedAt.specified=true");

        // Get all the calendarList where updatedAt is null
        defaultCalendarShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarsByOwnedByIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        User ownedBy = UserResourceIT.createEntity(em);
        em.persist(ownedBy);
        em.flush();
        calendar.setOwnedBy(ownedBy);
        calendarRepository.saveAndFlush(calendar);
        Long ownedById = ownedBy.getId();

        // Get all the calendarList where ownedBy equals to ownedById
        defaultCalendarShouldBeFound("ownedById.equals=" + ownedById);

        // Get all the calendarList where ownedBy equals to ownedById + 1
        defaultCalendarShouldNotBeFound("ownedById.equals=" + (ownedById + 1));
    }


    @Test
    @Transactional
    public void getAllCalendarsBySharedWithIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        User sharedWith = UserResourceIT.createEntity(em);
        em.persist(sharedWith);
        em.flush();
        calendar.addSharedWith(sharedWith);
        calendarRepository.saveAndFlush(calendar);
        Long sharedWithId = sharedWith.getId();

        // Get all the calendarList where sharedWith equals to sharedWithId
        defaultCalendarShouldBeFound("sharedWithId.equals=" + sharedWithId);

        // Get all the calendarList where sharedWith equals to sharedWithId + 1
        defaultCalendarShouldNotBeFound("sharedWithId.equals=" + (sharedWithId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalendarShouldBeFound(String filter) throws Exception {
        restCalendarMockMvc.perform(get("/api/calendars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCalendarMockMvc.perform(get("/api/calendars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalendarShouldNotBeFound(String filter) throws Exception {
        restCalendarMockMvc.perform(get("/api/calendars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalendarMockMvc.perform(get("/api/calendars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCalendar() throws Exception {
        // Get the calendar
        restCalendarMockMvc.perform(get("/api/calendars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar
        Calendar updatedCalendar = calendarRepository.findById(calendar.getId()).get();
        // Disconnect from session so that the updates on updatedCalendar are not directly saved in db
        em.detach(updatedCalendar);
        updatedCalendar
            .uid(UPDATED_UID)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CalendarDTO calendarDTO = calendarMapper.toDto(updatedCalendar);

        restCalendarMockMvc.perform(put("/api/calendars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCalendar.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendar.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCalendar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalendar.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testCalendar.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCalendar.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Calendar in Elasticsearch
        verify(mockCalendarSearchRepository, times(1)).save(testCalendar);
    }

    @Test
    @Transactional
    public void updateNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc.perform(put("/api/calendars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calendar in Elasticsearch
        verify(mockCalendarSearchRepository, times(0)).save(calendar);
    }

    @Test
    @Transactional
    public void deleteCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeDelete = calendarRepository.findAll().size();

        // Delete the calendar
        restCalendarMockMvc.perform(delete("/api/calendars/{id}", calendar.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Calendar in Elasticsearch
        verify(mockCalendarSearchRepository, times(1)).deleteById(calendar.getId());
    }

    @Test
    @Transactional
    public void searchCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        when(mockCalendarSearchRepository.search(queryStringQuery("id:" + calendar.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(calendar), PageRequest.of(0, 1), 1));
        // Search the calendar
        restCalendarMockMvc.perform(get("/api/_search/calendars?query=id:" + calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
