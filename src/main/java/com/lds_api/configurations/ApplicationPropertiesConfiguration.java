package com.lds_api.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("lds-configs")
public class ApplicationPropertiesConfiguration {

}
