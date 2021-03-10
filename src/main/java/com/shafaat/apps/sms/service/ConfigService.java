package com.shafaat.apps.sms.service;

import com.shafaat.apps.sms.domain.Config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Config}.
 */
public interface ConfigService {

    /**
     * Save a config.
     *
     * @param config the entity to save.
     * @return the persisted entity.
     */
    Config save(Config config);

    /**
     * Get all the configs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Config> findAll(Pageable pageable);


    /**
     * Get the "id" config.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Config> findOne(Long id);

    /**
     * Delete the "id" config.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
