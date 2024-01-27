package com.dogfight.dogfight.config.aop;

import com.dogfight.dogfight.common.trace.logtrace.LogTrace;
import com.dogfight.dogfight.config.aop.aspect.LogTraceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
