package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;

public interface PublishSearchEventPort {
    void publish(SearchMessage message);
}