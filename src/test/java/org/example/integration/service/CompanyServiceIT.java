package org.example.integration.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CompanyReadDto;
import org.example.integration.BaseIT;
import org.example.service.CompanyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class CompanyServiceIT extends BaseIT {
    public static final Integer COMPANY_ID = 1;
    private final CompanyService companyService;

    @Test
    void findById() {
        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        Assertions.assertTrue(actualResult.isPresent());
        var expectedResult = new CompanyReadDto(COMPANY_ID, "Google");
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
