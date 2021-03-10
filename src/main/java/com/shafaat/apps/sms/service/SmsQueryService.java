package com.shafaat.apps.sms.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.shafaat.apps.sms.domain.Sms;
import com.shafaat.apps.sms.domain.*; // for static metamodels
import com.shafaat.apps.sms.repository.SmsRepository;
import com.shafaat.apps.sms.service.dto.SmsCriteria;

/**
 * Service for executing complex queries for {@link Sms} entities in the database.
 * The main input is a {@link SmsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sms} or a {@link Page} of {@link Sms} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SmsQueryService extends QueryService<Sms> {

    private final Logger log = LoggerFactory.getLogger(SmsQueryService.class);

    private final SmsRepository smsRepository;

    public SmsQueryService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    /**
     * Return a {@link List} of {@link Sms} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sms> findByCriteria(SmsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sms> specification = createSpecification(criteria);
        return smsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sms} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sms> findByCriteria(SmsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sms> specification = createSpecification(criteria);
        return smsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SmsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sms> specification = createSpecification(criteria);
        return smsRepository.count(specification);
    }

    /**
     * Function to convert {@link SmsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sms> createSpecification(SmsCriteria criteria) {
        Specification<Sms> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sms_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Sms_.status));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Sms_.phoneNumber));
            }
            if (criteria.getPhoneName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneName(), Sms_.phoneName));
            }
            if (criteria.getDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTime(), Sms_.dateTime));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Sms_.message));
            }
            if (criteria.getIsPinned() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPinned(), Sms_.isPinned));
            }
        }
        return specification;
    }
}
