package com.riu.hotelsearch.infrastructure.config;

import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.application.service.CountSearchService;
import com.riu.hotelsearch.application.service.PersistSearchService;
import com.riu.hotelsearch.application.service.RegisterSearchService;
import com.riu.hotelsearch.domain.port.out.FindSearchPort;
import com.riu.hotelsearch.domain.port.out.GetSearchCountPort;
import com.riu.hotelsearch.domain.port.out.IncrementSearchCountPort;
import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.domain.port.out.SaveSearchIfAbsentPort;
import com.riu.hotelsearch.domain.port.out.SearchIdGenerator;
import com.riu.hotelsearch.domain.service.DefaultSearchFingerprintCalculator;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;
import com.riu.hotelsearch.infrastructure.application.TransactionManagedPersistSearchUseCase;
import com.riu.hotelsearch.infrastructure.support.UuidSearchIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public SearchFingerprintCalculator searchFingerprintCalculator() {
        return new DefaultSearchFingerprintCalculator();
    }

    @Bean
    public SearchIdGenerator searchIdGenerator() {
        return new UuidSearchIdGenerator();
    }

    @Bean
    public RegisterSearchUseCase registerSearchUseCase(
            PublishSearchEventPort publishSearchEventPort,
            SearchIdGenerator searchIdGenerator,
            SearchFingerprintCalculator searchFingerprintCalculator
    ) {
        return new RegisterSearchService(
                publishSearchEventPort,
                searchIdGenerator,
                searchFingerprintCalculator
        );
    }

    @Bean
    public CountSearchUseCase countSearchUseCase(
            FindSearchPort findSearchPort,
            GetSearchCountPort getSearchCountPort
    ) {
        return new CountSearchService(findSearchPort, getSearchCountPort);
    }

    @Bean
    public PersistSearchService persistSearchService(
            SaveSearchIfAbsentPort saveSearchIfAbsentPort,
            IncrementSearchCountPort incrementSearchCountPort
    ) {
        return new PersistSearchService(
                saveSearchIfAbsentPort,
                incrementSearchCountPort
        );
    }

    @Bean
    public PersistSearchUseCase persistSearchUseCase(
            PersistSearchService persistSearchService,
            PlatformTransactionManager transactionManager
    ) {
        return new TransactionManagedPersistSearchUseCase(
                persistSearchService,
                new TransactionTemplate(transactionManager)
        );
    }
}