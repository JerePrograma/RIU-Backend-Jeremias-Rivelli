package com.riu.hotelsearch.adapter.in.rest;

import com.riu.hotelsearch.adapter.in.rest.dto.SearchRequestDto;
import com.riu.hotelsearch.adapter.in.rest.dto.SearchResponseDto;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.domain.model.Search;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final RegisterSearchUseCase registerSearchUseCase;

    public SearchController(RegisterSearchUseCase registerSearchUseCase) {
        this.registerSearchUseCase = registerSearchUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SearchResponseDto search(@Valid @RequestBody SearchRequestDto request) {
        validateDates(request);

        Search search = new Search(
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages()
        );

        String searchId = registerSearchUseCase.register(search);
        return new SearchResponseDto(searchId);
    }

    private void validateDates(SearchRequestDto request) {
        if (!request.checkIn().isBefore(request.checkOut())) {
            throw new IllegalArgumentException("checkIn must be before checkOut");
        }
    }
}