package com.shafaat.apps.sms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.shafaat.apps.sms.web.rest.TestUtil;

public class ConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Config.class);
        Config config1 = new Config();
        config1.setId(1L);
        Config config2 = new Config();
        config2.setId(config1.getId());
        assertThat(config1).isEqualTo(config2);
        config2.setId(2L);
        assertThat(config1).isNotEqualTo(config2);
        config1.setId(null);
        assertThat(config1).isNotEqualTo(config2);
    }
}
