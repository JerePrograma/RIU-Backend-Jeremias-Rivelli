package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.domain.model.Search;

public interface RegisterSearchUseCase {
    String register(Search search);
}