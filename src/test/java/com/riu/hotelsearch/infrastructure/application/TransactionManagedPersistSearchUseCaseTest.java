package com.riu.hotelsearch.infrastructure.application;

import com.riu.hotelsearch.application.service.PersistSearchService;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionManagedPersistSearchUseCaseTest {

    private final PersistSearchService delegate = mock(PersistSearchService.class);
    private final TransactionTemplate transactionTemplate = mock(TransactionTemplate.class);

    private final TransactionManagedPersistSearchUseCase useCase =
            new TransactionManagedPersistSearchUseCase(delegate, transactionTemplate);

    @Test
    void shouldExecutePersistInsideTransactionTemplate() {
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(mock(TransactionStatus.class));
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        SearchRegisteredEvent event = validEvent();

        useCase.persist(event);

        verify(transactionTemplate).executeWithoutResult(any());
        verify(delegate).persist(event);
    }

    @Test
    void shouldPropagateExceptionThrownByDelegateInsideTransaction() {
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(mock(TransactionStatus.class));
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        SearchRegisteredEvent event = validEvent();
        doThrow(new RuntimeException("boom")).when(delegate).persist(event);

        assertThrows(RuntimeException.class, () -> useCase.persist(event));

        verify(transactionTemplate).executeWithoutResult(any());
        verify(delegate).persist(event);
    }

    private SearchRegisteredEvent validEvent() {
        return new SearchRegisteredEvent(
                "id-1",
                "fp-1",
                "hotel-1",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );
    }
}