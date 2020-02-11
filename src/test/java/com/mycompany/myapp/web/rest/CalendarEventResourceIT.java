package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FullCalendarApp;
import com.mycompany.myapp.domain.CalendarEvent;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Calendar;
import com.mycompany.myapp.repository.CalendarEventRepository;
import com.mycompany.myapp.repository.search.CalendarEventSearchRepository;
import com.mycompany.myapp.service.CalendarEventService;
import com.mycompany.myapp.service.dto.CalendarEventDTO;
import com.mycompany.myapp.service.mapper.CalendarEventMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.CalendarEventCriteria;
import com.mycompany.myapp.service.CalendarEventQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import com.mycompany.myapp.domain.enumeration.TypeCalendarEventStatus;
/**
 * Integration tests for the {@link CalendarEventResource} REST controller.
 */
@SpringBootTest(classes = FullCalendarApp.class)
public class CalendarEventResourceIT {

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

    private static final TypeCalendarEventStatus DEFAULT_STATUS = TypeCalendarEventStatus.TENTATIVE;
    private static final TypeCalendarEventStatus UPDATED_STATUS = TypeCalendarEventStatus.CONFIRMED;

    private static final Integer DEFAULT_PRIORITY = 0;
    private static final Integer UPDATED_PRIORITY = 1;
    private static final Integer SMALLER_PRIORITY = 0 - 1;

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "";
    private static final String UPDATED_LOCATION = "B";

    private static final String DEFAULT_CSS_THEME = "AAAAAAAAAA";
    private static final String UPDATED_CSS_THEME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OPENING_HOURS = "AAAAAAAAAA";
    private static final String UPDATED_OPENING_HOURS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_SHA_1 = "4d114dc0c5debee92ed93f5ad3142fa219015be8";
    private static final String UPDATED_IMAGE_SHA_1 = "b240c27a46ce4cba8851508d41a7e156e6f44dba";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_SHA_1 = "5c6dd3b314dbad48abef507ba7349af52aae6ebd";
    private static final String UPDATED_THUMBNAIL_SHA_1 = "29fbca050a0bdd170bde50b5ba2db11bab1604b5";

    private static final byte[] DEFAULT_ICAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICAL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICAL_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private CalendarEventMapper calendarEventMapper;

    @Autowired
    private CalendarEventService calendarEventService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CalendarEventSearchRepositoryMockConfiguration
     */
    @Autowired
    private CalendarEventSearchRepository mockCalendarEventSearchRepository;

    @Autowired
    private CalendarEventQueryService calendarEventQueryService;

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

    private MockMvc restCalendarEventMockMvc;

    private CalendarEvent calendarEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalendarEventResource calendarEventResource = new CalendarEventResource(calendarEventService, calendarEventQueryService);
        this.restCalendarEventMockMvc = MockMvcBuilders.standaloneSetup(calendarEventResource)
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
    public static CalendarEvent createEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .uid(DEFAULT_UID)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .place(DEFAULT_PLACE)
            .location(DEFAULT_LOCATION)
            .cssTheme(DEFAULT_CSS_THEME)
            .url(DEFAULT_URL)
            .isPublic(DEFAULT_IS_PUBLIC)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .openingHours(DEFAULT_OPENING_HOURS)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .imageSha1(DEFAULT_IMAGE_SHA_1)
            .imageUrl(DEFAULT_IMAGE_URL)
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailSha1(DEFAULT_THUMBNAIL_SHA_1)
            .ical(DEFAULT_ICAL)
            .icalContentType(DEFAULT_ICAL_CONTENT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return calendarEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createUpdatedEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .uid(UPDATED_UID)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .place(UPDATED_PLACE)
            .location(UPDATED_LOCATION)
            .cssTheme(UPDATED_CSS_THEME)
            .url(UPDATED_URL)
            .isPublic(UPDATED_IS_PUBLIC)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .openingHours(UPDATED_OPENING_HOURS)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageSha1(UPDATED_IMAGE_SHA_1)
            .imageUrl(UPDATED_IMAGE_URL)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailSha1(UPDATED_THUMBNAIL_SHA_1)
            .ical(UPDATED_ICAL)
            .icalContentType(UPDATED_ICAL_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return calendarEvent;
    }

    @BeforeEach
    public void initTest() {
        calendarEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalendarEvent() throws Exception {
        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);
        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isCreated());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate + 1);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCalendarEvent.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalendarEvent.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCalendarEvent.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testCalendarEvent.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testCalendarEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCalendarEvent.getCssTheme()).isEqualTo(DEFAULT_CSS_THEME);
        assertThat(testCalendarEvent.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCalendarEvent.isIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCalendarEvent.getOpeningHours()).isEqualTo(DEFAULT_OPENING_HOURS);
        assertThat(testCalendarEvent.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCalendarEvent.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCalendarEvent.getImageSha1()).isEqualTo(DEFAULT_IMAGE_SHA_1);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testCalendarEvent.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testCalendarEvent.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testCalendarEvent.getThumbnailSha1()).isEqualTo(DEFAULT_THUMBNAIL_SHA_1);
        assertThat(testCalendarEvent.getIcal()).isEqualTo(DEFAULT_ICAL);
        assertThat(testCalendarEvent.getIcalContentType()).isEqualTo(DEFAULT_ICAL_CONTENT_TYPE);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the CalendarEvent in Elasticsearch
        verify(mockCalendarEventSearchRepository, times(1)).save(testCalendarEvent);
    }

    @Test
    @Transactional
    public void createCalendarEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();

        // Create the CalendarEvent with an existing ID
        calendarEvent.setId(1L);
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate);

        // Validate the CalendarEvent in Elasticsearch
        verify(mockCalendarEventSearchRepository, times(0)).save(calendarEvent);
    }


    @Test
    @Transactional
    public void checkIsPublicIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setIsPublic(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setStartDate(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setCreatedAt(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalendarEvents() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList
        restCalendarEventMockMvc.perform(get("/api/calendar-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].cssTheme").value(hasItem(DEFAULT_CSS_THEME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingHours").value(hasItem(DEFAULT_OPENING_HOURS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSha1").value(hasItem(DEFAULT_IMAGE_SHA_1)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailSha1").value(hasItem(DEFAULT_THUMBNAIL_SHA_1)))
            .andExpect(jsonPath("$.[*].icalContentType").value(hasItem(DEFAULT_ICAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ical").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICAL))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get("/api/calendar-events/{id}", calendarEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendarEvent.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.cssTheme").value(DEFAULT_CSS_THEME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.openingHours").value(DEFAULT_OPENING_HOURS))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imageSha1").value(DEFAULT_IMAGE_SHA_1))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailSha1").value(DEFAULT_THUMBNAIL_SHA_1))
            .andExpect(jsonPath("$.icalContentType").value(DEFAULT_ICAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.ical").value(Base64Utils.encodeToString(DEFAULT_ICAL)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getCalendarEventsByIdFiltering() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        Long id = calendarEvent.getId();

        defaultCalendarEventShouldBeFound("id.equals=" + id);
        defaultCalendarEventShouldNotBeFound("id.notEquals=" + id);

        defaultCalendarEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalendarEventShouldNotBeFound("id.greaterThan=" + id);

        defaultCalendarEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalendarEventShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where uid equals to DEFAULT_UID
        defaultCalendarEventShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the calendarEventList where uid equals to UPDATED_UID
        defaultCalendarEventShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where uid not equals to DEFAULT_UID
        defaultCalendarEventShouldNotBeFound("uid.notEquals=" + DEFAULT_UID);

        // Get all the calendarEventList where uid not equals to UPDATED_UID
        defaultCalendarEventShouldBeFound("uid.notEquals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where uid in DEFAULT_UID or UPDATED_UID
        defaultCalendarEventShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the calendarEventList where uid equals to UPDATED_UID
        defaultCalendarEventShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where uid is not null
        defaultCalendarEventShouldBeFound("uid.specified=true");

        // Get all the calendarEventList where uid is null
        defaultCalendarEventShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title equals to DEFAULT_TITLE
        defaultCalendarEventShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the calendarEventList where title equals to UPDATED_TITLE
        defaultCalendarEventShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title not equals to DEFAULT_TITLE
        defaultCalendarEventShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the calendarEventList where title not equals to UPDATED_TITLE
        defaultCalendarEventShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCalendarEventShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the calendarEventList where title equals to UPDATED_TITLE
        defaultCalendarEventShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title is not null
        defaultCalendarEventShouldBeFound("title.specified=true");

        // Get all the calendarEventList where title is null
        defaultCalendarEventShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByTitleContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title contains DEFAULT_TITLE
        defaultCalendarEventShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the calendarEventList where title contains UPDATED_TITLE
        defaultCalendarEventShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where title does not contain DEFAULT_TITLE
        defaultCalendarEventShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the calendarEventList where title does not contain UPDATED_TITLE
        defaultCalendarEventShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle equals to DEFAULT_SUB_TITLE
        defaultCalendarEventShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the calendarEventList where subTitle equals to UPDATED_SUB_TITLE
        defaultCalendarEventShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle not equals to DEFAULT_SUB_TITLE
        defaultCalendarEventShouldNotBeFound("subTitle.notEquals=" + DEFAULT_SUB_TITLE);

        // Get all the calendarEventList where subTitle not equals to UPDATED_SUB_TITLE
        defaultCalendarEventShouldBeFound("subTitle.notEquals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultCalendarEventShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the calendarEventList where subTitle equals to UPDATED_SUB_TITLE
        defaultCalendarEventShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle is not null
        defaultCalendarEventShouldBeFound("subTitle.specified=true");

        // Get all the calendarEventList where subTitle is null
        defaultCalendarEventShouldNotBeFound("subTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle contains DEFAULT_SUB_TITLE
        defaultCalendarEventShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the calendarEventList where subTitle contains UPDATED_SUB_TITLE
        defaultCalendarEventShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultCalendarEventShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the calendarEventList where subTitle does not contain UPDATED_SUB_TITLE
        defaultCalendarEventShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description equals to DEFAULT_DESCRIPTION
        defaultCalendarEventShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the calendarEventList where description equals to UPDATED_DESCRIPTION
        defaultCalendarEventShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description not equals to DEFAULT_DESCRIPTION
        defaultCalendarEventShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the calendarEventList where description not equals to UPDATED_DESCRIPTION
        defaultCalendarEventShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCalendarEventShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the calendarEventList where description equals to UPDATED_DESCRIPTION
        defaultCalendarEventShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description is not null
        defaultCalendarEventShouldBeFound("description.specified=true");

        // Get all the calendarEventList where description is null
        defaultCalendarEventShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description contains DEFAULT_DESCRIPTION
        defaultCalendarEventShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the calendarEventList where description contains UPDATED_DESCRIPTION
        defaultCalendarEventShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where description does not contain DEFAULT_DESCRIPTION
        defaultCalendarEventShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the calendarEventList where description does not contain UPDATED_DESCRIPTION
        defaultCalendarEventShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where status equals to DEFAULT_STATUS
        defaultCalendarEventShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the calendarEventList where status equals to UPDATED_STATUS
        defaultCalendarEventShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where status not equals to DEFAULT_STATUS
        defaultCalendarEventShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the calendarEventList where status not equals to UPDATED_STATUS
        defaultCalendarEventShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCalendarEventShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the calendarEventList where status equals to UPDATED_STATUS
        defaultCalendarEventShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where status is not null
        defaultCalendarEventShouldBeFound("status.specified=true");

        // Get all the calendarEventList where status is null
        defaultCalendarEventShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority equals to DEFAULT_PRIORITY
        defaultCalendarEventShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority equals to UPDATED_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority not equals to DEFAULT_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority not equals to UPDATED_PRIORITY
        defaultCalendarEventShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultCalendarEventShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the calendarEventList where priority equals to UPDATED_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority is not null
        defaultCalendarEventShouldBeFound("priority.specified=true");

        // Get all the calendarEventList where priority is null
        defaultCalendarEventShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority is greater than or equal to DEFAULT_PRIORITY
        defaultCalendarEventShouldBeFound("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority is greater than or equal to UPDATED_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.greaterThanOrEqual=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority is less than or equal to DEFAULT_PRIORITY
        defaultCalendarEventShouldBeFound("priority.lessThanOrEqual=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority is less than or equal to SMALLER_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority is less than DEFAULT_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.lessThan=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority is less than UPDATED_PRIORITY
        defaultCalendarEventShouldBeFound("priority.lessThan=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where priority is greater than DEFAULT_PRIORITY
        defaultCalendarEventShouldNotBeFound("priority.greaterThan=" + DEFAULT_PRIORITY);

        // Get all the calendarEventList where priority is greater than SMALLER_PRIORITY
        defaultCalendarEventShouldBeFound("priority.greaterThan=" + SMALLER_PRIORITY);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place equals to DEFAULT_PLACE
        defaultCalendarEventShouldBeFound("place.equals=" + DEFAULT_PLACE);

        // Get all the calendarEventList where place equals to UPDATED_PLACE
        defaultCalendarEventShouldNotBeFound("place.equals=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPlaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place not equals to DEFAULT_PLACE
        defaultCalendarEventShouldNotBeFound("place.notEquals=" + DEFAULT_PLACE);

        // Get all the calendarEventList where place not equals to UPDATED_PLACE
        defaultCalendarEventShouldBeFound("place.notEquals=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place in DEFAULT_PLACE or UPDATED_PLACE
        defaultCalendarEventShouldBeFound("place.in=" + DEFAULT_PLACE + "," + UPDATED_PLACE);

        // Get all the calendarEventList where place equals to UPDATED_PLACE
        defaultCalendarEventShouldNotBeFound("place.in=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place is not null
        defaultCalendarEventShouldBeFound("place.specified=true");

        // Get all the calendarEventList where place is null
        defaultCalendarEventShouldNotBeFound("place.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByPlaceContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place contains DEFAULT_PLACE
        defaultCalendarEventShouldBeFound("place.contains=" + DEFAULT_PLACE);

        // Get all the calendarEventList where place contains UPDATED_PLACE
        defaultCalendarEventShouldNotBeFound("place.contains=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where place does not contain DEFAULT_PLACE
        defaultCalendarEventShouldNotBeFound("place.doesNotContain=" + DEFAULT_PLACE);

        // Get all the calendarEventList where place does not contain UPDATED_PLACE
        defaultCalendarEventShouldBeFound("place.doesNotContain=" + UPDATED_PLACE);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location equals to DEFAULT_LOCATION
        defaultCalendarEventShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the calendarEventList where location equals to UPDATED_LOCATION
        defaultCalendarEventShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location not equals to DEFAULT_LOCATION
        defaultCalendarEventShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the calendarEventList where location not equals to UPDATED_LOCATION
        defaultCalendarEventShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultCalendarEventShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the calendarEventList where location equals to UPDATED_LOCATION
        defaultCalendarEventShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location is not null
        defaultCalendarEventShouldBeFound("location.specified=true");

        // Get all the calendarEventList where location is null
        defaultCalendarEventShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByLocationContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location contains DEFAULT_LOCATION
        defaultCalendarEventShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the calendarEventList where location contains UPDATED_LOCATION
        defaultCalendarEventShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where location does not contain DEFAULT_LOCATION
        defaultCalendarEventShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the calendarEventList where location does not contain UPDATED_LOCATION
        defaultCalendarEventShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme equals to DEFAULT_CSS_THEME
        defaultCalendarEventShouldBeFound("cssTheme.equals=" + DEFAULT_CSS_THEME);

        // Get all the calendarEventList where cssTheme equals to UPDATED_CSS_THEME
        defaultCalendarEventShouldNotBeFound("cssTheme.equals=" + UPDATED_CSS_THEME);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme not equals to DEFAULT_CSS_THEME
        defaultCalendarEventShouldNotBeFound("cssTheme.notEquals=" + DEFAULT_CSS_THEME);

        // Get all the calendarEventList where cssTheme not equals to UPDATED_CSS_THEME
        defaultCalendarEventShouldBeFound("cssTheme.notEquals=" + UPDATED_CSS_THEME);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme in DEFAULT_CSS_THEME or UPDATED_CSS_THEME
        defaultCalendarEventShouldBeFound("cssTheme.in=" + DEFAULT_CSS_THEME + "," + UPDATED_CSS_THEME);

        // Get all the calendarEventList where cssTheme equals to UPDATED_CSS_THEME
        defaultCalendarEventShouldNotBeFound("cssTheme.in=" + UPDATED_CSS_THEME);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme is not null
        defaultCalendarEventShouldBeFound("cssTheme.specified=true");

        // Get all the calendarEventList where cssTheme is null
        defaultCalendarEventShouldNotBeFound("cssTheme.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme contains DEFAULT_CSS_THEME
        defaultCalendarEventShouldBeFound("cssTheme.contains=" + DEFAULT_CSS_THEME);

        // Get all the calendarEventList where cssTheme contains UPDATED_CSS_THEME
        defaultCalendarEventShouldNotBeFound("cssTheme.contains=" + UPDATED_CSS_THEME);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCssThemeNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where cssTheme does not contain DEFAULT_CSS_THEME
        defaultCalendarEventShouldNotBeFound("cssTheme.doesNotContain=" + DEFAULT_CSS_THEME);

        // Get all the calendarEventList where cssTheme does not contain UPDATED_CSS_THEME
        defaultCalendarEventShouldBeFound("cssTheme.doesNotContain=" + UPDATED_CSS_THEME);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url equals to DEFAULT_URL
        defaultCalendarEventShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the calendarEventList where url equals to UPDATED_URL
        defaultCalendarEventShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url not equals to DEFAULT_URL
        defaultCalendarEventShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the calendarEventList where url not equals to UPDATED_URL
        defaultCalendarEventShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url in DEFAULT_URL or UPDATED_URL
        defaultCalendarEventShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the calendarEventList where url equals to UPDATED_URL
        defaultCalendarEventShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url is not null
        defaultCalendarEventShouldBeFound("url.specified=true");

        // Get all the calendarEventList where url is null
        defaultCalendarEventShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByUrlContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url contains DEFAULT_URL
        defaultCalendarEventShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the calendarEventList where url contains UPDATED_URL
        defaultCalendarEventShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where url does not contain DEFAULT_URL
        defaultCalendarEventShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the calendarEventList where url does not contain UPDATED_URL
        defaultCalendarEventShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where isPublic equals to DEFAULT_IS_PUBLIC
        defaultCalendarEventShouldBeFound("isPublic.equals=" + DEFAULT_IS_PUBLIC);

        // Get all the calendarEventList where isPublic equals to UPDATED_IS_PUBLIC
        defaultCalendarEventShouldNotBeFound("isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByIsPublicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where isPublic not equals to DEFAULT_IS_PUBLIC
        defaultCalendarEventShouldNotBeFound("isPublic.notEquals=" + DEFAULT_IS_PUBLIC);

        // Get all the calendarEventList where isPublic not equals to UPDATED_IS_PUBLIC
        defaultCalendarEventShouldBeFound("isPublic.notEquals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where isPublic in DEFAULT_IS_PUBLIC or UPDATED_IS_PUBLIC
        defaultCalendarEventShouldBeFound("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC);

        // Get all the calendarEventList where isPublic equals to UPDATED_IS_PUBLIC
        defaultCalendarEventShouldNotBeFound("isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where isPublic is not null
        defaultCalendarEventShouldBeFound("isPublic.specified=true");

        // Get all the calendarEventList where isPublic is null
        defaultCalendarEventShouldNotBeFound("isPublic.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where startDate equals to DEFAULT_START_DATE
        defaultCalendarEventShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the calendarEventList where startDate equals to UPDATED_START_DATE
        defaultCalendarEventShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where startDate not equals to DEFAULT_START_DATE
        defaultCalendarEventShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the calendarEventList where startDate not equals to UPDATED_START_DATE
        defaultCalendarEventShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCalendarEventShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the calendarEventList where startDate equals to UPDATED_START_DATE
        defaultCalendarEventShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where startDate is not null
        defaultCalendarEventShouldBeFound("startDate.specified=true");

        // Get all the calendarEventList where startDate is null
        defaultCalendarEventShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where endDate equals to DEFAULT_END_DATE
        defaultCalendarEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the calendarEventList where endDate equals to UPDATED_END_DATE
        defaultCalendarEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where endDate not equals to DEFAULT_END_DATE
        defaultCalendarEventShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the calendarEventList where endDate not equals to UPDATED_END_DATE
        defaultCalendarEventShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultCalendarEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the calendarEventList where endDate equals to UPDATED_END_DATE
        defaultCalendarEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where endDate is not null
        defaultCalendarEventShouldBeFound("endDate.specified=true");

        // Get all the calendarEventList where endDate is null
        defaultCalendarEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours equals to DEFAULT_OPENING_HOURS
        defaultCalendarEventShouldBeFound("openingHours.equals=" + DEFAULT_OPENING_HOURS);

        // Get all the calendarEventList where openingHours equals to UPDATED_OPENING_HOURS
        defaultCalendarEventShouldNotBeFound("openingHours.equals=" + UPDATED_OPENING_HOURS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours not equals to DEFAULT_OPENING_HOURS
        defaultCalendarEventShouldNotBeFound("openingHours.notEquals=" + DEFAULT_OPENING_HOURS);

        // Get all the calendarEventList where openingHours not equals to UPDATED_OPENING_HOURS
        defaultCalendarEventShouldBeFound("openingHours.notEquals=" + UPDATED_OPENING_HOURS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours in DEFAULT_OPENING_HOURS or UPDATED_OPENING_HOURS
        defaultCalendarEventShouldBeFound("openingHours.in=" + DEFAULT_OPENING_HOURS + "," + UPDATED_OPENING_HOURS);

        // Get all the calendarEventList where openingHours equals to UPDATED_OPENING_HOURS
        defaultCalendarEventShouldNotBeFound("openingHours.in=" + UPDATED_OPENING_HOURS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours is not null
        defaultCalendarEventShouldBeFound("openingHours.specified=true");

        // Get all the calendarEventList where openingHours is null
        defaultCalendarEventShouldNotBeFound("openingHours.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours contains DEFAULT_OPENING_HOURS
        defaultCalendarEventShouldBeFound("openingHours.contains=" + DEFAULT_OPENING_HOURS);

        // Get all the calendarEventList where openingHours contains UPDATED_OPENING_HOURS
        defaultCalendarEventShouldNotBeFound("openingHours.contains=" + UPDATED_OPENING_HOURS);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByOpeningHoursNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where openingHours does not contain DEFAULT_OPENING_HOURS
        defaultCalendarEventShouldNotBeFound("openingHours.doesNotContain=" + DEFAULT_OPENING_HOURS);

        // Get all the calendarEventList where openingHours does not contain UPDATED_OPENING_HOURS
        defaultCalendarEventShouldBeFound("openingHours.doesNotContain=" + UPDATED_OPENING_HOURS);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1IsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 equals to DEFAULT_IMAGE_SHA_1
        defaultCalendarEventShouldBeFound("imageSha1.equals=" + DEFAULT_IMAGE_SHA_1);

        // Get all the calendarEventList where imageSha1 equals to UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldNotBeFound("imageSha1.equals=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 not equals to DEFAULT_IMAGE_SHA_1
        defaultCalendarEventShouldNotBeFound("imageSha1.notEquals=" + DEFAULT_IMAGE_SHA_1);

        // Get all the calendarEventList where imageSha1 not equals to UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldBeFound("imageSha1.notEquals=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1IsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 in DEFAULT_IMAGE_SHA_1 or UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldBeFound("imageSha1.in=" + DEFAULT_IMAGE_SHA_1 + "," + UPDATED_IMAGE_SHA_1);

        // Get all the calendarEventList where imageSha1 equals to UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldNotBeFound("imageSha1.in=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1IsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 is not null
        defaultCalendarEventShouldBeFound("imageSha1.specified=true");

        // Get all the calendarEventList where imageSha1 is null
        defaultCalendarEventShouldNotBeFound("imageSha1.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1ContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 contains DEFAULT_IMAGE_SHA_1
        defaultCalendarEventShouldBeFound("imageSha1.contains=" + DEFAULT_IMAGE_SHA_1);

        // Get all the calendarEventList where imageSha1 contains UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldNotBeFound("imageSha1.contains=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageSha1NotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageSha1 does not contain DEFAULT_IMAGE_SHA_1
        defaultCalendarEventShouldNotBeFound("imageSha1.doesNotContain=" + DEFAULT_IMAGE_SHA_1);

        // Get all the calendarEventList where imageSha1 does not contain UPDATED_IMAGE_SHA_1
        defaultCalendarEventShouldBeFound("imageSha1.doesNotContain=" + UPDATED_IMAGE_SHA_1);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultCalendarEventShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the calendarEventList where imageUrl equals to UPDATED_IMAGE_URL
        defaultCalendarEventShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultCalendarEventShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the calendarEventList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultCalendarEventShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultCalendarEventShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the calendarEventList where imageUrl equals to UPDATED_IMAGE_URL
        defaultCalendarEventShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl is not null
        defaultCalendarEventShouldBeFound("imageUrl.specified=true");

        // Get all the calendarEventList where imageUrl is null
        defaultCalendarEventShouldNotBeFound("imageUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl contains DEFAULT_IMAGE_URL
        defaultCalendarEventShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the calendarEventList where imageUrl contains UPDATED_IMAGE_URL
        defaultCalendarEventShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultCalendarEventShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the calendarEventList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultCalendarEventShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1IsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 equals to DEFAULT_THUMBNAIL_SHA_1
        defaultCalendarEventShouldBeFound("thumbnailSha1.equals=" + DEFAULT_THUMBNAIL_SHA_1);

        // Get all the calendarEventList where thumbnailSha1 equals to UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.equals=" + UPDATED_THUMBNAIL_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 not equals to DEFAULT_THUMBNAIL_SHA_1
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.notEquals=" + DEFAULT_THUMBNAIL_SHA_1);

        // Get all the calendarEventList where thumbnailSha1 not equals to UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldBeFound("thumbnailSha1.notEquals=" + UPDATED_THUMBNAIL_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1IsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 in DEFAULT_THUMBNAIL_SHA_1 or UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldBeFound("thumbnailSha1.in=" + DEFAULT_THUMBNAIL_SHA_1 + "," + UPDATED_THUMBNAIL_SHA_1);

        // Get all the calendarEventList where thumbnailSha1 equals to UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.in=" + UPDATED_THUMBNAIL_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1IsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 is not null
        defaultCalendarEventShouldBeFound("thumbnailSha1.specified=true");

        // Get all the calendarEventList where thumbnailSha1 is null
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1ContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 contains DEFAULT_THUMBNAIL_SHA_1
        defaultCalendarEventShouldBeFound("thumbnailSha1.contains=" + DEFAULT_THUMBNAIL_SHA_1);

        // Get all the calendarEventList where thumbnailSha1 contains UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.contains=" + UPDATED_THUMBNAIL_SHA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByThumbnailSha1NotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where thumbnailSha1 does not contain DEFAULT_THUMBNAIL_SHA_1
        defaultCalendarEventShouldNotBeFound("thumbnailSha1.doesNotContain=" + DEFAULT_THUMBNAIL_SHA_1);

        // Get all the calendarEventList where thumbnailSha1 does not contain UPDATED_THUMBNAIL_SHA_1
        defaultCalendarEventShouldBeFound("thumbnailSha1.doesNotContain=" + UPDATED_THUMBNAIL_SHA_1);
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt equals to DEFAULT_CREATED_AT
        defaultCalendarEventShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the calendarEventList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarEventShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt not equals to DEFAULT_CREATED_AT
        defaultCalendarEventShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the calendarEventList where createdAt not equals to UPDATED_CREATED_AT
        defaultCalendarEventShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCalendarEventShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the calendarEventList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarEventShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt is not null
        defaultCalendarEventShouldBeFound("createdAt.specified=true");

        // Get all the calendarEventList where createdAt is null
        defaultCalendarEventShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCalendarEventShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarEventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarEventShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultCalendarEventShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarEventList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultCalendarEventShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCalendarEventShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the calendarEventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarEventShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt is not null
        defaultCalendarEventShouldBeFound("updatedAt.specified=true");

        // Get all the calendarEventList where updatedAt is null
        defaultCalendarEventShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarEventsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);
        User createdBy = UserResourceIT.createEntity(em);
        em.persist(createdBy);
        em.flush();
        calendarEvent.setCreatedBy(createdBy);
        calendarEventRepository.saveAndFlush(calendarEvent);
        Long createdById = createdBy.getId();

        // Get all the calendarEventList where createdBy equals to createdById
        defaultCalendarEventShouldBeFound("createdById.equals=" + createdById);

        // Get all the calendarEventList where createdBy equals to createdById + 1
        defaultCalendarEventShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }


    @Test
    @Transactional
    public void getAllCalendarEventsByCalendarIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);
        Calendar calendar = CalendarResourceIT.createEntity(em);
        em.persist(calendar);
        em.flush();
        calendarEvent.setCalendar(calendar);
        calendarEventRepository.saveAndFlush(calendarEvent);
        Long calendarId = calendar.getId();

        // Get all the calendarEventList where calendar equals to calendarId
        defaultCalendarEventShouldBeFound("calendarId.equals=" + calendarId);

        // Get all the calendarEventList where calendar equals to calendarId + 1
        defaultCalendarEventShouldNotBeFound("calendarId.equals=" + (calendarId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalendarEventShouldBeFound(String filter) throws Exception {
        restCalendarEventMockMvc.perform(get("/api/calendar-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].cssTheme").value(hasItem(DEFAULT_CSS_THEME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingHours").value(hasItem(DEFAULT_OPENING_HOURS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSha1").value(hasItem(DEFAULT_IMAGE_SHA_1)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailSha1").value(hasItem(DEFAULT_THUMBNAIL_SHA_1)))
            .andExpect(jsonPath("$.[*].icalContentType").value(hasItem(DEFAULT_ICAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ical").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICAL))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCalendarEventMockMvc.perform(get("/api/calendar-events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalendarEventShouldNotBeFound(String filter) throws Exception {
        restCalendarEventMockMvc.perform(get("/api/calendar-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalendarEventMockMvc.perform(get("/api/calendar-events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCalendarEvent() throws Exception {
        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get("/api/calendar-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent
        CalendarEvent updatedCalendarEvent = calendarEventRepository.findById(calendarEvent.getId()).get();
        // Disconnect from session so that the updates on updatedCalendarEvent are not directly saved in db
        em.detach(updatedCalendarEvent);
        updatedCalendarEvent
            .uid(UPDATED_UID)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .place(UPDATED_PLACE)
            .location(UPDATED_LOCATION)
            .cssTheme(UPDATED_CSS_THEME)
            .url(UPDATED_URL)
            .isPublic(UPDATED_IS_PUBLIC)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .openingHours(UPDATED_OPENING_HOURS)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageSha1(UPDATED_IMAGE_SHA_1)
            .imageUrl(UPDATED_IMAGE_URL)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailSha1(UPDATED_THUMBNAIL_SHA_1)
            .ical(UPDATED_ICAL)
            .icalContentType(UPDATED_ICAL_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(updatedCalendarEvent);

        restCalendarEventMockMvc.perform(put("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendarEvent.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalendarEvent.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCalendarEvent.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testCalendarEvent.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testCalendarEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCalendarEvent.getCssTheme()).isEqualTo(UPDATED_CSS_THEME);
        assertThat(testCalendarEvent.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testCalendarEvent.isIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCalendarEvent.getOpeningHours()).isEqualTo(UPDATED_OPENING_HOURS);
        assertThat(testCalendarEvent.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCalendarEvent.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCalendarEvent.getImageSha1()).isEqualTo(UPDATED_IMAGE_SHA_1);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCalendarEvent.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testCalendarEvent.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testCalendarEvent.getThumbnailSha1()).isEqualTo(UPDATED_THUMBNAIL_SHA_1);
        assertThat(testCalendarEvent.getIcal()).isEqualTo(UPDATED_ICAL);
        assertThat(testCalendarEvent.getIcalContentType()).isEqualTo(UPDATED_ICAL_CONTENT_TYPE);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the CalendarEvent in Elasticsearch
        verify(mockCalendarEventSearchRepository, times(1)).save(testCalendarEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc.perform(put("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CalendarEvent in Elasticsearch
        verify(mockCalendarEventSearchRepository, times(0)).save(calendarEvent);
    }

    @Test
    @Transactional
    public void deleteCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeDelete = calendarEventRepository.findAll().size();

        // Delete the calendarEvent
        restCalendarEventMockMvc.perform(delete("/api/calendar-events/{id}", calendarEvent.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CalendarEvent in Elasticsearch
        verify(mockCalendarEventSearchRepository, times(1)).deleteById(calendarEvent.getId());
    }

    @Test
    @Transactional
    public void searchCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);
        when(mockCalendarEventSearchRepository.search(queryStringQuery("id:" + calendarEvent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(calendarEvent), PageRequest.of(0, 1), 1));
        // Search the calendarEvent
        restCalendarEventMockMvc.perform(get("/api/_search/calendar-events?query=id:" + calendarEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].cssTheme").value(hasItem(DEFAULT_CSS_THEME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingHours").value(hasItem(DEFAULT_OPENING_HOURS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSha1").value(hasItem(DEFAULT_IMAGE_SHA_1)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailSha1").value(hasItem(DEFAULT_THUMBNAIL_SHA_1)))
            .andExpect(jsonPath("$.[*].icalContentType").value(hasItem(DEFAULT_ICAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ical").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICAL))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
