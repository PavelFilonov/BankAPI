package com.example.bankapi.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    @Value("${application.cores}")
    private Integer cores;

    @Override
    @Bean(name = "asyncExecutor")
    @NonNull
    public Executor getAsyncExecutor() {
        return newFixedThreadPool(cores);
    }

}
