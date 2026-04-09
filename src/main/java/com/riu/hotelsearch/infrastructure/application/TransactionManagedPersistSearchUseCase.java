package com.riu.hotelsearch.infrastructure.application;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.application.service.PersistSearchService;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import org.springframework.transaction.support.TransactionTemplate;

public final class TransactionManagedPersistSearchUseCase implements PersistSearchUseCase {

    private final PersistSearchService delegate;
    private final TransactionTemplate transactionTemplate;

    public TransactionManagedPersistSearchUseCase(
            PersistSearchService delegate,
            TransactionTemplate transactionTemplate
    ) {
        this.delegate = delegate;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void persist(SearchRegisteredEvent event) {
        transactionTemplate.executeWithoutResult(status -> delegate.persist(event));
    }
}