package com.urban.urban_move.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing configuration for tracking entity creation and modification.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
