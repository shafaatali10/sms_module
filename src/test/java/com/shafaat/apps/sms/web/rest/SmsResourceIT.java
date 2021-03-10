package com.shafaat.apps.sms.web.rest;

import com.shafaat.apps.sms.SmsModuleApp;
import com.shafaat.apps.sms.domain.Sms;
import com.shafaat.apps.sms.repository.SmsRepository;
import com.shafaat.apps.sms.service.SmsService;
import com.shafaat.apps.sms.service.dto.SmsCriteria;
import com.shafaat.apps.sms.service.SmsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SmsResource} REST controller.
 */
@SpringBootTest(classes = SmsModuleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SmsResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PINNED = false;
    private static final Boolean UPDATED_IS_PINNED = true;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsQueryService smsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSmsMockMvc;

    private Sms sms;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sms createEntity(EntityManager em) {
        Sms sms = new Sms()
            .status(DEFAULT_STATUS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .phoneName(DEFAULT_PHONE_NAME)
            .dateTime(DEFAULT_DATE_TIME)
            .message(DEFAULT_MESSAGE)
            .isPinned(DEFAULT_IS_PINNED);
        return sms;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sms createUpdatedEntity(EntityManager em) {
        Sms sms = new Sms()
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneName(UPDATED_PHONE_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .message(UPDATED_MESSAGE)
            .isPinned(UPDATED_IS_PINNED);
        return sms;
    }

    @BeforeEach
    public void initTest() {
        sms = createEntity(em);
    }

    @Test
    @Transactional
    public void createSms() throws Exception {
        int databaseSizeBeforeCreate = smsRepository.findAll().size();
        // Create the Sms
        restSmsMockMvc.perform(post("/api/sms").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sms)))
            .andExpect(status().isCreated());

        // Validate the Sms in the database
        List<Sms> smsList = smsRepository.findAll();
        assertThat(smsList).hasSize(databaseSizeBeforeCreate + 1);
        Sms testSms = smsList.get(smsList.size() - 1);
        assertThat(testSms.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSms.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSms.getPhoneName()).isEqualTo(DEFAULT_PHONE_NAME);
        assertThat(testSms.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testSms.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSms.isIsPinned()).isEqualTo(DEFAULT_IS_PINNED);
    }

    @Test
    @Transactional
    public void createSmsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsRepository.findAll().size();

        // Create the Sms with an existing ID
        sms.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsMockMvc.perform(post("/api/sms").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sms)))
            .andExpect(status().isBadRequest());

        // Validate the Sms in the database
        List<Sms> smsList = smsRepository.findAll();
        assertThat(smsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSms() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList
        restSmsMockMvc.perform(get("/api/sms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sms.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneName").value(hasItem(DEFAULT_PHONE_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].isPinned").value(hasItem(DEFAULT_IS_PINNED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSms() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get the sms
        restSmsMockMvc.perform(get("/api/sms/{id}", sms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sms.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.phoneName").value(DEFAULT_PHONE_NAME))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.isPinned").value(DEFAULT_IS_PINNED.booleanValue()));
    }


    @Test
    @Transactional
    public void getSmsByIdFiltering() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        Long id = sms.getId();

        defaultSmsShouldBeFound("id.equals=" + id);
        defaultSmsShouldNotBeFound("id.notEquals=" + id);

        defaultSmsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSmsShouldNotBeFound("id.greaterThan=" + id);

        defaultSmsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSmsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSmsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status equals to DEFAULT_STATUS
        defaultSmsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the smsList where status equals to UPDATED_STATUS
        defaultSmsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSmsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status not equals to DEFAULT_STATUS
        defaultSmsShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the smsList where status not equals to UPDATED_STATUS
        defaultSmsShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSmsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSmsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the smsList where status equals to UPDATED_STATUS
        defaultSmsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSmsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status is not null
        defaultSmsShouldBeFound("status.specified=true");

        // Get all the smsList where status is null
        defaultSmsShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllSmsByStatusContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status contains DEFAULT_STATUS
        defaultSmsShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the smsList where status contains UPDATED_STATUS
        defaultSmsShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSmsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where status does not contain DEFAULT_STATUS
        defaultSmsShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the smsList where status does not contain UPDATED_STATUS
        defaultSmsShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllSmsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSmsShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the smsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSmsShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultSmsShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the smsList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultSmsShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSmsShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the smsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSmsShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber is not null
        defaultSmsShouldBeFound("phoneNumber.specified=true");

        // Get all the smsList where phoneNumber is null
        defaultSmsShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllSmsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultSmsShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the smsList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultSmsShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultSmsShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the smsList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultSmsShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllSmsByPhoneNameIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName equals to DEFAULT_PHONE_NAME
        defaultSmsShouldBeFound("phoneName.equals=" + DEFAULT_PHONE_NAME);

        // Get all the smsList where phoneName equals to UPDATED_PHONE_NAME
        defaultSmsShouldNotBeFound("phoneName.equals=" + UPDATED_PHONE_NAME);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName not equals to DEFAULT_PHONE_NAME
        defaultSmsShouldNotBeFound("phoneName.notEquals=" + DEFAULT_PHONE_NAME);

        // Get all the smsList where phoneName not equals to UPDATED_PHONE_NAME
        defaultSmsShouldBeFound("phoneName.notEquals=" + UPDATED_PHONE_NAME);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNameIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName in DEFAULT_PHONE_NAME or UPDATED_PHONE_NAME
        defaultSmsShouldBeFound("phoneName.in=" + DEFAULT_PHONE_NAME + "," + UPDATED_PHONE_NAME);

        // Get all the smsList where phoneName equals to UPDATED_PHONE_NAME
        defaultSmsShouldNotBeFound("phoneName.in=" + UPDATED_PHONE_NAME);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName is not null
        defaultSmsShouldBeFound("phoneName.specified=true");

        // Get all the smsList where phoneName is null
        defaultSmsShouldNotBeFound("phoneName.specified=false");
    }
                @Test
    @Transactional
    public void getAllSmsByPhoneNameContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName contains DEFAULT_PHONE_NAME
        defaultSmsShouldBeFound("phoneName.contains=" + DEFAULT_PHONE_NAME);

        // Get all the smsList where phoneName contains UPDATED_PHONE_NAME
        defaultSmsShouldNotBeFound("phoneName.contains=" + UPDATED_PHONE_NAME);
    }

    @Test
    @Transactional
    public void getAllSmsByPhoneNameNotContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where phoneName does not contain DEFAULT_PHONE_NAME
        defaultSmsShouldNotBeFound("phoneName.doesNotContain=" + DEFAULT_PHONE_NAME);

        // Get all the smsList where phoneName does not contain UPDATED_PHONE_NAME
        defaultSmsShouldBeFound("phoneName.doesNotContain=" + UPDATED_PHONE_NAME);
    }


    @Test
    @Transactional
    public void getAllSmsByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where dateTime equals to DEFAULT_DATE_TIME
        defaultSmsShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the smsList where dateTime equals to UPDATED_DATE_TIME
        defaultSmsShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllSmsByDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where dateTime not equals to DEFAULT_DATE_TIME
        defaultSmsShouldNotBeFound("dateTime.notEquals=" + DEFAULT_DATE_TIME);

        // Get all the smsList where dateTime not equals to UPDATED_DATE_TIME
        defaultSmsShouldBeFound("dateTime.notEquals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllSmsByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultSmsShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the smsList where dateTime equals to UPDATED_DATE_TIME
        defaultSmsShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllSmsByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where dateTime is not null
        defaultSmsShouldBeFound("dateTime.specified=true");

        // Get all the smsList where dateTime is null
        defaultSmsShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllSmsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message equals to DEFAULT_MESSAGE
        defaultSmsShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the smsList where message equals to UPDATED_MESSAGE
        defaultSmsShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSmsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message not equals to DEFAULT_MESSAGE
        defaultSmsShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the smsList where message not equals to UPDATED_MESSAGE
        defaultSmsShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSmsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultSmsShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the smsList where message equals to UPDATED_MESSAGE
        defaultSmsShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSmsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message is not null
        defaultSmsShouldBeFound("message.specified=true");

        // Get all the smsList where message is null
        defaultSmsShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllSmsByMessageContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message contains DEFAULT_MESSAGE
        defaultSmsShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the smsList where message contains UPDATED_MESSAGE
        defaultSmsShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSmsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where message does not contain DEFAULT_MESSAGE
        defaultSmsShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the smsList where message does not contain UPDATED_MESSAGE
        defaultSmsShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllSmsByIsPinnedIsEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where isPinned equals to DEFAULT_IS_PINNED
        defaultSmsShouldBeFound("isPinned.equals=" + DEFAULT_IS_PINNED);

        // Get all the smsList where isPinned equals to UPDATED_IS_PINNED
        defaultSmsShouldNotBeFound("isPinned.equals=" + UPDATED_IS_PINNED);
    }

    @Test
    @Transactional
    public void getAllSmsByIsPinnedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where isPinned not equals to DEFAULT_IS_PINNED
        defaultSmsShouldNotBeFound("isPinned.notEquals=" + DEFAULT_IS_PINNED);

        // Get all the smsList where isPinned not equals to UPDATED_IS_PINNED
        defaultSmsShouldBeFound("isPinned.notEquals=" + UPDATED_IS_PINNED);
    }

    @Test
    @Transactional
    public void getAllSmsByIsPinnedIsInShouldWork() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where isPinned in DEFAULT_IS_PINNED or UPDATED_IS_PINNED
        defaultSmsShouldBeFound("isPinned.in=" + DEFAULT_IS_PINNED + "," + UPDATED_IS_PINNED);

        // Get all the smsList where isPinned equals to UPDATED_IS_PINNED
        defaultSmsShouldNotBeFound("isPinned.in=" + UPDATED_IS_PINNED);
    }

    @Test
    @Transactional
    public void getAllSmsByIsPinnedIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsRepository.saveAndFlush(sms);

        // Get all the smsList where isPinned is not null
        defaultSmsShouldBeFound("isPinned.specified=true");

        // Get all the smsList where isPinned is null
        defaultSmsShouldNotBeFound("isPinned.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsShouldBeFound(String filter) throws Exception {
        restSmsMockMvc.perform(get("/api/sms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sms.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneName").value(hasItem(DEFAULT_PHONE_NAME)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].isPinned").value(hasItem(DEFAULT_IS_PINNED.booleanValue())));

        // Check, that the count call also returns 1
        restSmsMockMvc.perform(get("/api/sms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsShouldNotBeFound(String filter) throws Exception {
        restSmsMockMvc.perform(get("/api/sms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsMockMvc.perform(get("/api/sms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSms() throws Exception {
        // Get the sms
        restSmsMockMvc.perform(get("/api/sms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSms() throws Exception {
        // Initialize the database
        smsService.save(sms);

        int databaseSizeBeforeUpdate = smsRepository.findAll().size();

        // Update the sms
        Sms updatedSms = smsRepository.findById(sms.getId()).get();
        // Disconnect from session so that the updates on updatedSms are not directly saved in db
        em.detach(updatedSms);
        updatedSms
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneName(UPDATED_PHONE_NAME)
            .dateTime(UPDATED_DATE_TIME)
            .message(UPDATED_MESSAGE)
            .isPinned(UPDATED_IS_PINNED);

        restSmsMockMvc.perform(put("/api/sms").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSms)))
            .andExpect(status().isOk());

        // Validate the Sms in the database
        List<Sms> smsList = smsRepository.findAll();
        assertThat(smsList).hasSize(databaseSizeBeforeUpdate);
        Sms testSms = smsList.get(smsList.size() - 1);
        assertThat(testSms.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSms.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSms.getPhoneName()).isEqualTo(UPDATED_PHONE_NAME);
        assertThat(testSms.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testSms.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSms.isIsPinned()).isEqualTo(UPDATED_IS_PINNED);
    }

    @Test
    @Transactional
    public void updateNonExistingSms() throws Exception {
        int databaseSizeBeforeUpdate = smsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsMockMvc.perform(put("/api/sms").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sms)))
            .andExpect(status().isBadRequest());

        // Validate the Sms in the database
        List<Sms> smsList = smsRepository.findAll();
        assertThat(smsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSms() throws Exception {
        // Initialize the database
        smsService.save(sms);

        int databaseSizeBeforeDelete = smsRepository.findAll().size();

        // Delete the sms
        restSmsMockMvc.perform(delete("/api/sms/{id}", sms.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sms> smsList = smsRepository.findAll();
        assertThat(smsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
