package com.shafaat.apps.sms.web.rest;

import com.shafaat.apps.sms.domain.Sms;
import com.shafaat.apps.sms.service.SmsService;
import com.shafaat.apps.sms.web.rest.errors.BadRequestAlertException;
import com.shafaat.apps.sms.service.dto.SmsCriteria;
import com.shafaat.apps.sms.service.SmsQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.shafaat.apps.sms.domain.Sms}.
 */
@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    private static final String ENTITY_NAME = "sms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsService smsService;

    private final SmsQueryService smsQueryService;

    public SmsResource(SmsService smsService, SmsQueryService smsQueryService) {
        this.smsService = smsService;
        this.smsQueryService = smsQueryService;
    }

    /**
     * {@code POST  /sms} : Create a new sms.
     *
     * @param sms the sms to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sms, or with status {@code 400 (Bad Request)} if the sms has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms")
    public ResponseEntity<Sms> createSms(@RequestBody Sms sms) throws URISyntaxException {
        log.debug("REST request to save Sms : {}", sms);
        if (sms.getId() != null) {
            throw new BadRequestAlertException("A new sms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sms result = smsService.save(sms);
        return ResponseEntity.created(new URI("/api/sms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms} : Updates an existing sms.
     *
     * @param sms the sms to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sms,
     * or with status {@code 400 (Bad Request)} if the sms is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sms couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms")
    public ResponseEntity<Sms> updateSms(@RequestBody Sms sms) throws URISyntaxException {
        log.debug("REST request to update Sms : {}", sms);
        if (sms.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sms result = smsService.save(sms);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sms.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sms} : get all the sms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sms in body.
     */
    @GetMapping("/sms")
    public ResponseEntity<List<Sms>> getAllSms(SmsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sms by criteria: {}", criteria);
        Page<Sms> page = smsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sms/count} : count all the sms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sms/count")
    public ResponseEntity<Long> countSms(SmsCriteria criteria) {
        log.debug("REST request to count Sms by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms/:id} : get the "id" sms.
     *
     * @param id the id of the sms to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sms, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms/{id}")
    public ResponseEntity<Sms> getSms(@PathVariable Long id) {
        log.debug("REST request to get Sms : {}", id);
        Optional<Sms> sms = smsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sms);
    }

    /**
     * {@code DELETE  /sms/:id} : delete the "id" sms.
     *
     * @param id the id of the sms to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms/{id}")
    public ResponseEntity<Void> deleteSms(@PathVariable Long id) {
        log.debug("REST request to delete Sms : {}", id);
        smsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/sms-scan")
    public ResponseEntity<String> processIncomingSms() {
        log.debug("REST request to scan incoming SMS: {}");
        smsService.scanIncomingSms();
        return ResponseEntity.ok().body("Scanning successful");
    }
}
