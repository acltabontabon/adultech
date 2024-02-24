package com.acltabontabon.web.rest;

import com.acltabontabon.domain.ServiceProvider;
import com.acltabontabon.repository.ServiceProviderRepository;
import com.acltabontabon.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.acltabontabon.domain.ServiceProvider}.
 */
@RestController
@RequestMapping("/api/service-providers")
@Transactional
public class ServiceProviderResource {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderResource.class);

    private static final String ENTITY_NAME = "serviceProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceProviderRepository serviceProviderRepository;

    public ServiceProviderResource(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    /**
     * {@code POST  /service-providers} : Create a new serviceProvider.
     *
     * @param serviceProvider the serviceProvider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceProvider, or with status {@code 400 (Bad Request)} if the serviceProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceProvider> createServiceProvider(@RequestBody ServiceProvider serviceProvider) throws URISyntaxException {
        log.debug("REST request to save ServiceProvider : {}", serviceProvider);
        if (serviceProvider.getId() != null) {
            throw new BadRequestAlertException("A new serviceProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceProvider result = serviceProviderRepository.save(serviceProvider);
        return ResponseEntity
            .created(new URI("/api/service-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-providers/:id} : Updates an existing serviceProvider.
     *
     * @param id the id of the serviceProvider to save.
     * @param serviceProvider the serviceProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceProvider,
     * or with status {@code 400 (Bad Request)} if the serviceProvider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceProvider> updateServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceProvider serviceProvider
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceProvider : {}, {}", id, serviceProvider);
        if (serviceProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceProvider result = serviceProviderRepository.save(serviceProvider);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceProvider.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-providers/:id} : Partial updates given fields of an existing serviceProvider, field will ignore if it is null
     *
     * @param id the id of the serviceProvider to save.
     * @param serviceProvider the serviceProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceProvider,
     * or with status {@code 400 (Bad Request)} if the serviceProvider is not valid,
     * or with status {@code 404 (Not Found)} if the serviceProvider is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceProvider> partialUpdateServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceProvider serviceProvider
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceProvider partially : {}, {}", id, serviceProvider);
        if (serviceProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceProvider> result = serviceProviderRepository
            .findById(serviceProvider.getId())
            .map(existingServiceProvider -> {
                if (serviceProvider.getName() != null) {
                    existingServiceProvider.setName(serviceProvider.getName());
                }
                if (serviceProvider.getUnitMeasurement() != null) {
                    existingServiceProvider.setUnitMeasurement(serviceProvider.getUnitMeasurement());
                }
                if (serviceProvider.getNotes() != null) {
                    existingServiceProvider.setNotes(serviceProvider.getNotes());
                }
                if (serviceProvider.getRate() != null) {
                    existingServiceProvider.setRate(serviceProvider.getRate());
                }
                if (serviceProvider.getType() != null) {
                    existingServiceProvider.setType(serviceProvider.getType());
                }
                if (serviceProvider.getLastUpdate() != null) {
                    existingServiceProvider.setLastUpdate(serviceProvider.getLastUpdate());
                }

                return existingServiceProvider;
            })
            .map(serviceProviderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceProvider.getId().toString())
        );
    }

    /**
     * {@code GET  /service-providers} : get all the serviceProviders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceProviders in body.
     */
    @GetMapping("")
    public List<ServiceProvider> getAllServiceProviders() {
        log.debug("REST request to get all ServiceProviders");
        return serviceProviderRepository.findAll();
    }

    /**
     * {@code GET  /service-providers/:id} : get the "id" serviceProvider.
     *
     * @param id the id of the serviceProvider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceProvider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getServiceProvider(@PathVariable("id") Long id) {
        log.debug("REST request to get ServiceProvider : {}", id);
        Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceProvider);
    }

    /**
     * {@code DELETE  /service-providers/:id} : delete the "id" serviceProvider.
     *
     * @param id the id of the serviceProvider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable("id") Long id) {
        log.debug("REST request to delete ServiceProvider : {}", id);
        serviceProviderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
