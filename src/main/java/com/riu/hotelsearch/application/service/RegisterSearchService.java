package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.application.support.SearchIdGenerator;
import com.riu.hotelsearch.domain.model.Search;
import org.springframework.stereotype.Service;

@Service
public class RegisterSearchService implements RegisterSearchUseCase {

    private final PublishSearchEventPort publishSearchEventPort;
    private final SearchIdGenerator searchIdGenerator;

    public RegisterSearchService(
            PublishSearchEventPort publishSearchEventPort,
            SearchIdGenerator searchIdGenerator
    ) {
        this.publishSearchEventPort = publishSearchEventPort;
        this.searchIdGenerator = searchIdGenerator;
    }

    @Override
    public String register(Search search) {
        String searchId = searchIdGenerator.nextId();

        SearchMessage message = new SearchMessage(
                searchId,
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        publishSearchEventPort.publish(message);
        return searchId;
    }
}