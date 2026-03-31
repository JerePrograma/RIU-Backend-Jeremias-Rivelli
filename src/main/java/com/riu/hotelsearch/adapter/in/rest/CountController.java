package com.riu.hotelsearch.adapter.in.rest;

import com.riu.hotelsearch.adapter.in.rest.dto.CountResponseDto;
import com.riu.hotelsearch.adapter.in.rest.dto.SearchDataDto;
import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.domain.model.SearchCount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountController {

    private final CountSearchUseCase countSearchUseCase;

    public CountController(CountSearchUseCase countSearchUseCase) {
        this.countSearchUseCase = countSearchUseCase;
    }

    @GetMapping("/count")
    public CountResponseDto count(@RequestParam String searchId) {
        SearchCount result = countSearchUseCase.count(searchId);

        return new CountResponseDto(
                result.searchId(),
                new SearchDataDto(
                        result.search().hotelId(),
                        result.search().checkIn(),
                        result.search().checkOut(),
                        result.search().ages()
                ),
                result.count()
        );
    }
}