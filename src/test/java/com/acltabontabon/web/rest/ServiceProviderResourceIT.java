package com.acltabontabon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acltabontabon.IntegrationTest;
import com.acltabontabon.domain.ServiceProvider;
import com.acltabontabon.domain.enumeration.ServiceType;
import com.acltabontabon.repository.ServiceProviderRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_MEASUREMENT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_MEASUREMENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final ServiceType DEFAULT_TYPE = ServiceType.ELECTRICITY;
    private static final ServiceType UPDATED_TYPE = ServiceType.WATER;

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/service-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceProviderMockMvc;

    private ServiceProvider serviceProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceProvider createEntity(EntityManager em) {
        ServiceProvider serviceProvider = new ServiceProvider()
            .name(DEFAULT_NAME)
            .unitMeasurement(DEFAULT_UNIT_MEASUREMENT)
            .notes(DEFAULT_NOTES)
            .rate(DEFAULT_RATE)
            .type(DEFAULT_TYPE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return serviceProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceProvider createUpdatedEntity(EntityManager em) {
        ServiceProvider serviceProvider = new ServiceProvider()
            .name(UPDATED_NAME)
            .unitMeasurement(UPDATED_UNIT_MEASUREMENT)
            .notes(UPDATED_NOTES)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        return serviceProvider;
    }

    @BeforeEach
    public void initTest() {
        serviceProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceProvider() throws Exception {
        int databaseSizeBeforeCreate = serviceProviderRepository.findAll().size();
        // Create the ServiceProvider
        restServiceProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceProvider.getUnitMeasurement()).isEqualTo(DEFAULT_UNIT_MEASUREMENT);
        assertThat(testServiceProvider.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testServiceProvider.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testServiceProvider.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceProvider.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    void createServiceProviderWithExistingId() throws Exception {
        // Create the ServiceProvider with an existing ID
        serviceProvider.setId(1L);

        int databaseSizeBeforeCreate = serviceProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceProviders() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviderList
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unitMeasurement").value(hasItem(DEFAULT_UNIT_MEASUREMENT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    void getServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get the serviceProvider
        restServiceProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unitMeasurement").value(DEFAULT_UNIT_MEASUREMENT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingServiceProvider() throws Exception {
        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Update the serviceProvider
        ServiceProvider updatedServiceProvider = serviceProviderRepository.findById(serviceProvider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceProvider are not directly saved in db
        em.detach(updatedServiceProvider);
        updatedServiceProvider
            .name(UPDATED_NAME)
            .unitMeasurement(UPDATED_UNIT_MEASUREMENT)
            .notes(UPDATED_NOTES)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceProvider.getUnitMeasurement()).isEqualTo(UPDATED_UNIT_MEASUREMENT);
        assertThat(testServiceProvider.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testServiceProvider.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testServiceProvider.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceProvider.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void putNonExistingServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceProviderWithPatch() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Update the serviceProvider using partial update
        ServiceProvider partialUpdatedServiceProvider = new ServiceProvider();
        partialUpdatedServiceProvider.setId(serviceProvider.getId());

        partialUpdatedServiceProvider.name(UPDATED_NAME).lastUpdate(UPDATED_LAST_UPDATE);

        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceProvider.getUnitMeasurement()).isEqualTo(DEFAULT_UNIT_MEASUREMENT);
        assertThat(testServiceProvider.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testServiceProvider.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testServiceProvider.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceProvider.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void fullUpdateServiceProviderWithPatch() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Update the serviceProvider using partial update
        ServiceProvider partialUpdatedServiceProvider = new ServiceProvider();
        partialUpdatedServiceProvider.setId(serviceProvider.getId());

        partialUpdatedServiceProvider
            .name(UPDATED_NAME)
            .unitMeasurement(UPDATED_UNIT_MEASUREMENT)
            .notes(UPDATED_NOTES)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
        ServiceProvider testServiceProvider = serviceProviderList.get(serviceProviderList.size() - 1);
        assertThat(testServiceProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceProvider.getUnitMeasurement()).isEqualTo(UPDATED_UNIT_MEASUREMENT);
        assertThat(testServiceProvider.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testServiceProvider.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testServiceProvider.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceProvider.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void patchNonExistingServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();
        serviceProvider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        int databaseSizeBeforeDelete = serviceProviderRepository.findAll().size();

        // Delete the serviceProvider
        restServiceProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceProvider> serviceProviderList = serviceProviderRepository.findAll();
        assertThat(serviceProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
