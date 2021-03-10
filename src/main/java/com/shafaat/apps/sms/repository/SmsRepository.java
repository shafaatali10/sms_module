package com.shafaat.apps.sms.repository;

import com.shafaat.apps.sms.domain.Sms;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Sms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsRepository extends JpaRepository<Sms, Long>, JpaSpecificationExecutor<Sms> {
}
