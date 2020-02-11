package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FullCalendarApp;
import com.mycompany.myapp.domain.CalendarProvider;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.CalendarProviderRepository;
import com.mycompany.myapp.repository.search.CalendarProviderSearchRepository;
import com.mycompany.myapp.service.CalendarProviderService;
import com.mycompany.myapp.service.dto.CalendarProviderDTO;
import com.mycompany.myapp.service.mapper.CalendarProviderMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.CalendarProviderCriteria;
import com.mycompany.myapp.service.CalendarProviderQueryService;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.TypeCalendarProvider;
/**
 * Integration tests for the {@link CalendarProviderResource} REST controller.
 */
@SpringBootTest(classes = FullCalendarApp.class)
public class CalendarProviderResourceIT {

    private static final TypeCalendarProvider DEFAULT_PROVIDER = TypeCalendarProvider.GOOGLE;
    private static final TypeCalendarProvider UPDATED_PROVIDER = TypeCalendarProvider.APPLE;

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_CREDENTIAL = "AAAAAAAAAA";
    private static final String UPDATED_CREDENTIAL = "BBBBBBBBBB";

    private static final String DEFAULT_CREDENTIAL_EXTRA_1 = "AAAAAAAAAA";
    private static final String UPDATED_CREDENTIAL_EXTRA_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CREDENTIAL_EXTRA_2 = "AAAAAAAAAA";
    private static final String UPDATED_CREDENTIAL_EXTRA_2 = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CalendarProviderRepository calendarProviderRepository;

    @Autowired
    private CalendarProviderMapper calendarProviderMapper;

    @Autowired
    private CalendarProviderService calendarProviderService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CalendarProviderSearchRepositoryMockConfiguration
     */
    @Autowired
    private CalendarProviderSearchRepository mockCalendarProviderSearchRepository;

    @Autowired
    private CalendarProviderQueryService calendarProviderQueryService;

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

    private MockMvc restCalendarProviderMockMvc;

    private CalendarProvider calendarProvider;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalendarProviderResource calendarProviderResource = new CalendarProviderResource(calendarProviderService, calendarProviderQueryService);
        this.restCalendarProviderMockMvc = MockMvcBuilders.standaloneSetup(calendarProviderResource)
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
    public static CalendarProvider createEntity(EntityManager em) {
        CalendarProvider calendarProvider = new CalendarProvider()
            .provider(DEFAULT_PROVIDER)
            .identifier(DEFAULT_IDENTIFIER)
            .credential(DEFAULT_CREDENTIAL)
            .credentialExtra1(DEFAULT_CREDENTIAL_EXTRA_1)
            .credentialExtra2(DEFAULT_CREDENTIAL_EXTRA_2)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return calendarProvider;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarProvider createUpdatedEntity(EntityManager em) {
        CalendarProvider calendarProvider = new CalendarProvider()
            .provider(UPDATED_PROVIDER)
            .identifier(UPDATED_IDENTIFIER)
            .credential(UPDATED_CREDENTIAL)
            .credentialExtra1(UPDATED_CREDENTIAL_EXTRA_1)
            .credentialExtra2(UPDATED_CREDENTIAL_EXTRA_2)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return calendarProvider;
    }

    @BeforeEach
    public void initTest() {
        calendarProvider = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalendarProvider() throws Exception {
        int databaseSizeBeforeCreate = calendarProviderRepository.findAll().size();

        // Create the CalendarProvider
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);
        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isCreated());

        // Validate the CalendarProvider in the database
        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeCreate + 1);
        CalendarProvider testCalendarProvider = calendarProviderList.get(calendarProviderList.size() - 1);
        assertThat(testCalendarProvider.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testCalendarProvider.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testCalendarProvider.getCredential()).isEqualTo(DEFAULT_CREDENTIAL);
        assertThat(testCalendarProvider.getCredentialExtra1()).isEqualTo(DEFAULT_CREDENTIAL_EXTRA_1);
        assertThat(testCalendarProvider.getCredentialExtra2()).isEqualTo(DEFAULT_CREDENTIAL_EXTRA_2);
        assertThat(testCalendarProvider.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendarProvider.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the CalendarProvider in Elasticsearch
        verify(mockCalendarProviderSearchRepository, times(1)).save(testCalendarProvider);
    }

    @Test
    @Transactional
    public void createCalendarProviderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calendarProviderRepository.findAll().size();

        // Create the CalendarProvider with an existing ID
        calendarProvider.setId(1L);
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CalendarProvider in the database
        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeCreate);

        // Validate the CalendarProvider in Elasticsearch
        verify(mockCalendarProviderSearchRepository, times(0)).save(calendarProvider);
    }


    @Test
    @Transactional
    public void checkProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarProviderRepository.findAll().size();
        // set the field null
        calendarProvider.setProvider(null);

        // Create the CalendarProvider, which fails.
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarProviderRepository.findAll().size();
        // set the field null
        calendarProvider.setIdentifier(null);

        // Create the CalendarProvider, which fails.
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCredentialIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarProviderRepository.findAll().size();
        // set the field null
        calendarProvider.setCredential(null);

        // Create the CalendarProvider, which fails.
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarProviderRepository.findAll().size();
        // set the field null
        calendarProvider.setCreatedAt(null);

        // Create the CalendarProvider, which fails.
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        restCalendarProviderMockMvc.perform(post("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalendarProviders() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].credential").value(hasItem(DEFAULT_CREDENTIAL)))
            .andExpect(jsonPath("$.[*].credentialExtra1").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_1)))
            .andExpect(jsonPath("$.[*].credentialExtra2").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_2)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getCalendarProvider() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get the calendarProvider
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers/{id}", calendarProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendarProvider.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER))
            .andExpect(jsonPath("$.credential").value(DEFAULT_CREDENTIAL))
            .andExpect(jsonPath("$.credentialExtra1").value(DEFAULT_CREDENTIAL_EXTRA_1))
            .andExpect(jsonPath("$.credentialExtra2").value(DEFAULT_CREDENTIAL_EXTRA_2))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getCalendarProvidersByIdFiltering() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        Long id = calendarProvider.getId();

        defaultCalendarProviderShouldBeFound("id.equals=" + id);
        defaultCalendarProviderShouldNotBeFound("id.notEquals=" + id);

        defaultCalendarProviderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalendarProviderShouldNotBeFound("id.greaterThan=" + id);

        defaultCalendarProviderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalendarProviderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCalendarProvidersByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where provider equals to DEFAULT_PROVIDER
        defaultCalendarProviderShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the calendarProviderList where provider equals to UPDATED_PROVIDER
        defaultCalendarProviderShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByProviderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where provider not equals to DEFAULT_PROVIDER
        defaultCalendarProviderShouldNotBeFound("provider.notEquals=" + DEFAULT_PROVIDER);

        // Get all the calendarProviderList where provider not equals to UPDATED_PROVIDER
        defaultCalendarProviderShouldBeFound("provider.notEquals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultCalendarProviderShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the calendarProviderList where provider equals to UPDATED_PROVIDER
        defaultCalendarProviderShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where provider is not null
        defaultCalendarProviderShouldBeFound("provider.specified=true");

        // Get all the calendarProviderList where provider is null
        defaultCalendarProviderShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier equals to DEFAULT_IDENTIFIER
        defaultCalendarProviderShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the calendarProviderList where identifier equals to UPDATED_IDENTIFIER
        defaultCalendarProviderShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier not equals to DEFAULT_IDENTIFIER
        defaultCalendarProviderShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the calendarProviderList where identifier not equals to UPDATED_IDENTIFIER
        defaultCalendarProviderShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultCalendarProviderShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the calendarProviderList where identifier equals to UPDATED_IDENTIFIER
        defaultCalendarProviderShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier is not null
        defaultCalendarProviderShouldBeFound("identifier.specified=true");

        // Get all the calendarProviderList where identifier is null
        defaultCalendarProviderShouldNotBeFound("identifier.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier contains DEFAULT_IDENTIFIER
        defaultCalendarProviderShouldBeFound("identifier.contains=" + DEFAULT_IDENTIFIER);

        // Get all the calendarProviderList where identifier contains UPDATED_IDENTIFIER
        defaultCalendarProviderShouldNotBeFound("identifier.contains=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByIdentifierNotContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where identifier does not contain DEFAULT_IDENTIFIER
        defaultCalendarProviderShouldNotBeFound("identifier.doesNotContain=" + DEFAULT_IDENTIFIER);

        // Get all the calendarProviderList where identifier does not contain UPDATED_IDENTIFIER
        defaultCalendarProviderShouldBeFound("identifier.doesNotContain=" + UPDATED_IDENTIFIER);
    }


    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential equals to DEFAULT_CREDENTIAL
        defaultCalendarProviderShouldBeFound("credential.equals=" + DEFAULT_CREDENTIAL);

        // Get all the calendarProviderList where credential equals to UPDATED_CREDENTIAL
        defaultCalendarProviderShouldNotBeFound("credential.equals=" + UPDATED_CREDENTIAL);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential not equals to DEFAULT_CREDENTIAL
        defaultCalendarProviderShouldNotBeFound("credential.notEquals=" + DEFAULT_CREDENTIAL);

        // Get all the calendarProviderList where credential not equals to UPDATED_CREDENTIAL
        defaultCalendarProviderShouldBeFound("credential.notEquals=" + UPDATED_CREDENTIAL);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialIsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential in DEFAULT_CREDENTIAL or UPDATED_CREDENTIAL
        defaultCalendarProviderShouldBeFound("credential.in=" + DEFAULT_CREDENTIAL + "," + UPDATED_CREDENTIAL);

        // Get all the calendarProviderList where credential equals to UPDATED_CREDENTIAL
        defaultCalendarProviderShouldNotBeFound("credential.in=" + UPDATED_CREDENTIAL);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential is not null
        defaultCalendarProviderShouldBeFound("credential.specified=true");

        // Get all the calendarProviderList where credential is null
        defaultCalendarProviderShouldNotBeFound("credential.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential contains DEFAULT_CREDENTIAL
        defaultCalendarProviderShouldBeFound("credential.contains=" + DEFAULT_CREDENTIAL);

        // Get all the calendarProviderList where credential contains UPDATED_CREDENTIAL
        defaultCalendarProviderShouldNotBeFound("credential.contains=" + UPDATED_CREDENTIAL);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialNotContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credential does not contain DEFAULT_CREDENTIAL
        defaultCalendarProviderShouldNotBeFound("credential.doesNotContain=" + DEFAULT_CREDENTIAL);

        // Get all the calendarProviderList where credential does not contain UPDATED_CREDENTIAL
        defaultCalendarProviderShouldBeFound("credential.doesNotContain=" + UPDATED_CREDENTIAL);
    }


    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 equals to DEFAULT_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldBeFound("credentialExtra1.equals=" + DEFAULT_CREDENTIAL_EXTRA_1);

        // Get all the calendarProviderList where credentialExtra1 equals to UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.equals=" + UPDATED_CREDENTIAL_EXTRA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 not equals to DEFAULT_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.notEquals=" + DEFAULT_CREDENTIAL_EXTRA_1);

        // Get all the calendarProviderList where credentialExtra1 not equals to UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldBeFound("credentialExtra1.notEquals=" + UPDATED_CREDENTIAL_EXTRA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 in DEFAULT_CREDENTIAL_EXTRA_1 or UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldBeFound("credentialExtra1.in=" + DEFAULT_CREDENTIAL_EXTRA_1 + "," + UPDATED_CREDENTIAL_EXTRA_1);

        // Get all the calendarProviderList where credentialExtra1 equals to UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.in=" + UPDATED_CREDENTIAL_EXTRA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 is not null
        defaultCalendarProviderShouldBeFound("credentialExtra1.specified=true");

        // Get all the calendarProviderList where credentialExtra1 is null
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1ContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 contains DEFAULT_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldBeFound("credentialExtra1.contains=" + DEFAULT_CREDENTIAL_EXTRA_1);

        // Get all the calendarProviderList where credentialExtra1 contains UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.contains=" + UPDATED_CREDENTIAL_EXTRA_1);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra1 does not contain DEFAULT_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldNotBeFound("credentialExtra1.doesNotContain=" + DEFAULT_CREDENTIAL_EXTRA_1);

        // Get all the calendarProviderList where credentialExtra1 does not contain UPDATED_CREDENTIAL_EXTRA_1
        defaultCalendarProviderShouldBeFound("credentialExtra1.doesNotContain=" + UPDATED_CREDENTIAL_EXTRA_1);
    }


    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 equals to DEFAULT_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldBeFound("credentialExtra2.equals=" + DEFAULT_CREDENTIAL_EXTRA_2);

        // Get all the calendarProviderList where credentialExtra2 equals to UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.equals=" + UPDATED_CREDENTIAL_EXTRA_2);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 not equals to DEFAULT_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.notEquals=" + DEFAULT_CREDENTIAL_EXTRA_2);

        // Get all the calendarProviderList where credentialExtra2 not equals to UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldBeFound("credentialExtra2.notEquals=" + UPDATED_CREDENTIAL_EXTRA_2);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 in DEFAULT_CREDENTIAL_EXTRA_2 or UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldBeFound("credentialExtra2.in=" + DEFAULT_CREDENTIAL_EXTRA_2 + "," + UPDATED_CREDENTIAL_EXTRA_2);

        // Get all the calendarProviderList where credentialExtra2 equals to UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.in=" + UPDATED_CREDENTIAL_EXTRA_2);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 is not null
        defaultCalendarProviderShouldBeFound("credentialExtra2.specified=true");

        // Get all the calendarProviderList where credentialExtra2 is null
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.specified=false");
    }
                @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2ContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 contains DEFAULT_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldBeFound("credentialExtra2.contains=" + DEFAULT_CREDENTIAL_EXTRA_2);

        // Get all the calendarProviderList where credentialExtra2 contains UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.contains=" + UPDATED_CREDENTIAL_EXTRA_2);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCredentialExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where credentialExtra2 does not contain DEFAULT_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldNotBeFound("credentialExtra2.doesNotContain=" + DEFAULT_CREDENTIAL_EXTRA_2);

        // Get all the calendarProviderList where credentialExtra2 does not contain UPDATED_CREDENTIAL_EXTRA_2
        defaultCalendarProviderShouldBeFound("credentialExtra2.doesNotContain=" + UPDATED_CREDENTIAL_EXTRA_2);
    }


    @Test
    @Transactional
    public void getAllCalendarProvidersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where createdAt equals to DEFAULT_CREATED_AT
        defaultCalendarProviderShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the calendarProviderList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarProviderShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where createdAt not equals to DEFAULT_CREATED_AT
        defaultCalendarProviderShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the calendarProviderList where createdAt not equals to UPDATED_CREATED_AT
        defaultCalendarProviderShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCalendarProviderShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the calendarProviderList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarProviderShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where createdAt is not null
        defaultCalendarProviderShouldBeFound("createdAt.specified=true");

        // Get all the calendarProviderList where createdAt is null
        defaultCalendarProviderShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCalendarProviderShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarProviderList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarProviderShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultCalendarProviderShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarProviderList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultCalendarProviderShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCalendarProviderShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the calendarProviderList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarProviderShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        // Get all the calendarProviderList where updatedAt is not null
        defaultCalendarProviderShouldBeFound("updatedAt.specified=true");

        // Get all the calendarProviderList where updatedAt is null
        defaultCalendarProviderShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCalendarProvidersByOwnedByIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);
        User ownedBy = UserResourceIT.createEntity(em);
        em.persist(ownedBy);
        em.flush();
        calendarProvider.setOwnedBy(ownedBy);
        calendarProviderRepository.saveAndFlush(calendarProvider);
        Long ownedById = ownedBy.getId();

        // Get all the calendarProviderList where ownedBy equals to ownedById
        defaultCalendarProviderShouldBeFound("ownedById.equals=" + ownedById);

        // Get all the calendarProviderList where ownedBy equals to ownedById + 1
        defaultCalendarProviderShouldNotBeFound("ownedById.equals=" + (ownedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalendarProviderShouldBeFound(String filter) throws Exception {
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].credential").value(hasItem(DEFAULT_CREDENTIAL)))
            .andExpect(jsonPath("$.[*].credentialExtra1").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_1)))
            .andExpect(jsonPath("$.[*].credentialExtra2").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_2)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalendarProviderShouldNotBeFound(String filter) throws Exception {
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCalendarProvider() throws Exception {
        // Get the calendarProvider
        restCalendarProviderMockMvc.perform(get("/api/calendar-providers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendarProvider() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        int databaseSizeBeforeUpdate = calendarProviderRepository.findAll().size();

        // Update the calendarProvider
        CalendarProvider updatedCalendarProvider = calendarProviderRepository.findById(calendarProvider.getId()).get();
        // Disconnect from session so that the updates on updatedCalendarProvider are not directly saved in db
        em.detach(updatedCalendarProvider);
        updatedCalendarProvider
            .provider(UPDATED_PROVIDER)
            .identifier(UPDATED_IDENTIFIER)
            .credential(UPDATED_CREDENTIAL)
            .credentialExtra1(UPDATED_CREDENTIAL_EXTRA_1)
            .credentialExtra2(UPDATED_CREDENTIAL_EXTRA_2)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(updatedCalendarProvider);

        restCalendarProviderMockMvc.perform(put("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isOk());

        // Validate the CalendarProvider in the database
        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeUpdate);
        CalendarProvider testCalendarProvider = calendarProviderList.get(calendarProviderList.size() - 1);
        assertThat(testCalendarProvider.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testCalendarProvider.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testCalendarProvider.getCredential()).isEqualTo(UPDATED_CREDENTIAL);
        assertThat(testCalendarProvider.getCredentialExtra1()).isEqualTo(UPDATED_CREDENTIAL_EXTRA_1);
        assertThat(testCalendarProvider.getCredentialExtra2()).isEqualTo(UPDATED_CREDENTIAL_EXTRA_2);
        assertThat(testCalendarProvider.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCalendarProvider.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the CalendarProvider in Elasticsearch
        verify(mockCalendarProviderSearchRepository, times(1)).save(testCalendarProvider);
    }

    @Test
    @Transactional
    public void updateNonExistingCalendarProvider() throws Exception {
        int databaseSizeBeforeUpdate = calendarProviderRepository.findAll().size();

        // Create the CalendarProvider
        CalendarProviderDTO calendarProviderDTO = calendarProviderMapper.toDto(calendarProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarProviderMockMvc.perform(put("/api/calendar-providers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(calendarProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CalendarProvider in the database
        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CalendarProvider in Elasticsearch
        verify(mockCalendarProviderSearchRepository, times(0)).save(calendarProvider);
    }

    @Test
    @Transactional
    public void deleteCalendarProvider() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);

        int databaseSizeBeforeDelete = calendarProviderRepository.findAll().size();

        // Delete the calendarProvider
        restCalendarProviderMockMvc.perform(delete("/api/calendar-providers/{id}", calendarProvider.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalendarProvider> calendarProviderList = calendarProviderRepository.findAll();
        assertThat(calendarProviderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CalendarProvider in Elasticsearch
        verify(mockCalendarProviderSearchRepository, times(1)).deleteById(calendarProvider.getId());
    }

    @Test
    @Transactional
    public void searchCalendarProvider() throws Exception {
        // Initialize the database
        calendarProviderRepository.saveAndFlush(calendarProvider);
        when(mockCalendarProviderSearchRepository.search(queryStringQuery("id:" + calendarProvider.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(calendarProvider), PageRequest.of(0, 1), 1));
        // Search the calendarProvider
        restCalendarProviderMockMvc.perform(get("/api/_search/calendar-providers?query=id:" + calendarProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].credential").value(hasItem(DEFAULT_CREDENTIAL)))
            .andExpect(jsonPath("$.[*].credentialExtra1").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_1)))
            .andExpect(jsonPath("$.[*].credentialExtra2").value(hasItem(DEFAULT_CREDENTIAL_EXTRA_2)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
