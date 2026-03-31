package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.domain.model.SearchRecord;

public interface SaveSearchPort {
    void save(SearchRecord searchRecord);
}