package com.shafaat.apps.sms.repository;

import com.shafaat.apps.sms.domain.Config;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<Config> findByConfigName(String configName);

}
