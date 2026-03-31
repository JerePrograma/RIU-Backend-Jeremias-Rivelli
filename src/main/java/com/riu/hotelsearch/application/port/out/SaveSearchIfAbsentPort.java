package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.domain.model.SearchRecord;

public interface SaveSearchIfAbsentPort {
    boolean saveIfAbsent(SearchRecord record);
}