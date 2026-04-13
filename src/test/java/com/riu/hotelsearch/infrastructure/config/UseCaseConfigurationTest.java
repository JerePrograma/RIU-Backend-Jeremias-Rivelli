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
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

class UseCaseConfigurationTest {

    private final UseCaseConfiguration config = new UseCaseConfiguration();

    @Test
    void shouldCreateSearchFingerprintCalculator() {
        SearchFingerprintCalculator bean = config.searchFingerprintCalculator();
        assertInstanceOf(DefaultSearchFingerprintCalculator.class, bean);
    }

    @Test
    void shouldCreateSearchIdGenerator() {
        SearchIdGenerator bean = config.searchIdGenerator();
        assertInstanceOf(UuidSearchIdGenerator.class, bean);
    }

    @Test
    void shouldCreateRegisterSearchUseCase() {
        PublishSearchEventPort publishPort = mock(PublishSearchEventPort.class);
        SearchIdGenerator idGenerator = mock(SearchIdGenerator.class);
        SearchFingerprintCalculator calculator = mock(SearchFingerprintCalculator.class);

        RegisterSearchUseCase bean = config.registerSearchUseCase(publishPort, idGenerator, calculator);

        assertInstanceOf(RegisterSearchService.class, bean);
    }

    @Test
    void shouldCreateCountSearchUseCase() {
        FindSearchPort findSearchPort = mock(FindSearchPort.class);
        GetSearchCountPort getSearchCountPort = mock(GetSearchCountPort.class);

        CountSearchUseCase bean = config.countSearchUseCase(findSearchPort, getSearchCountPort);

        assertInstanceOf(CountSearchService.class, bean);
    }

    @Test
    void shouldCreatePersistSearchService() {
        SaveSearchIfAbsentPort savePort = mock(SaveSearchIfAbsentPort.class);
        IncrementSearchCountPort incrementPort = mock(IncrementSearchCountPort.class);

        PersistSearchService bean = config.persistSearchService(savePort, incrementPort);

        assertInstanceOf(PersistSearchService.class, bean);
    }

    @Test
    void shouldCreatePersistSearchUseCase() {
        PersistSearchService persistSearchService = mock(PersistSearchService.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);

        PersistSearchUseCase bean = config.persistSearchUseCase(persistSearchService, transactionManager);

        assertInstanceOf(TransactionManagedPersistSearchUseCase.class, bean);
    }
}