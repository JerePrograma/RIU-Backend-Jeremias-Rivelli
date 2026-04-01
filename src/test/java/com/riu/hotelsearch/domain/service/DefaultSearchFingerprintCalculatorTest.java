package com.riu.hotelsearch.domain.service;

import com.riu.hotelsearch.domain.model.Search;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSearchFingerprintCalculatorTest {

    private final DefaultSearchFingerprintCalculator calculator = new DefaultSearchFingerprintCalculator();

    @Test
    void shouldReturnSameFingerprintForEquivalentSearch() {
        Search search1 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));
        Search search2 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));

        String fp1 = calculator.calculate(search1);
        String fp2 = calculator.calculate(search2);

        assertEquals(fp1, fp2);
        assertEquals(64, fp1.length());
        assertTrue(fp1.matches("[0-9a-f]{64}"));
    }

    @Test
    void shouldChangeFingerprintWhenHotelIdChanges() {
        Search search1 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29));
        Search search2 = new Search("9999xYz", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29));

        assertNotEquals(calculator.calculate(search1), calculator.calculate(search2));
    }

    @Test
    void shouldChangeFingerprintWhenDatesChange() {
        Search search1 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29));
        Search search2 = new Search("1234aBc", LocalDate.of(2023, 12, 30), LocalDate.of(2023, 12, 31), List.of(30, 29));
        Search search3 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2024, 1, 1), List.of(30, 29));

        String fp1 = calculator.calculate(search1);

        assertNotEquals(fp1, calculator.calculate(search2));
        assertNotEquals(fp1, calculator.calculate(search3));
    }

    @Test
    void shouldChangeFingerprintWhenAgesChange() {
        Search search1 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));
        Search search2 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 4));

        assertNotEquals(calculator.calculate(search1), calculator.calculate(search2));
    }

    @Test
    void shouldRespectAgeOrderWhenCalculatingFingerprint() {
        Search search1 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));
        Search search2 = new Search("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(3, 1, 29, 30));

        assertNotEquals(calculator.calculate(search1), calculator.calculate(search2));
    }
}
