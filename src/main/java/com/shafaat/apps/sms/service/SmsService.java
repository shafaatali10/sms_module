package com.shafaat.apps.sms.service;

import com.shafaat.apps.sms.domain.Sms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Sms}.
 */
public interface SmsService {

    /**
     * Save a sms.
     *
     * @param sms the entity to save.
     * @return the persisted entity.
     */
    Sms save(Sms sms);

    /**
     * Get all the sms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sms> findAll(Pageable pageable);


    /**
     * Get the "id" sms.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sms> findOne(Long id);

    /**
     * Delete the "id" sms.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void scanIncomingSms();
}
