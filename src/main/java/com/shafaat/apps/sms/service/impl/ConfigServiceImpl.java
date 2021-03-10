package com.shafaat.apps.sms.service.impl;

import com.shafaat.apps.sms.service.ConfigService;
import com.shafaat.apps.sms.domain.Config;
import com.shafaat.apps.sms.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Config}.
 */
@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);

    private final ConfigRepository configRepository;

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Config save(Config config) {
        log.debug("Request to save Config : {}", config);
        return configRepository.save(config);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Config> findAll(Pageable pageable) {
        log.debug("Request to get all Configs");
        return configRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Config> findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        configRepository.deleteById(id);
    }
}
