package com.atomz.sawonz.global.seed;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SeedProperties.class)
public class SeedConfig { }