package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.domain.model.SearchCount;

public interface CountSearchUseCase {
    SearchCount count(String searchId);
}