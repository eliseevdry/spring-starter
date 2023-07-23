package org.example.service;

import org.example.database.entity.Company;
import org.example.database.repository.CompanyRepository;
import org.example.dto.CompanyReadDto;
import org.example.listener.entity.EntityEvent;
import org.example.mapper.CompanyReadMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    public static final Integer COMPANY_ID = 1;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private CompanyReadMapper companyReadMapper;
    @InjectMocks
    private CompanyService companyService;

    @Test
    void findById() {
        doReturn(Optional.of(new Company(COMPANY_ID, null, Collections.emptyMap())))
            .when(companyRepository).findById(COMPANY_ID);
        doReturn(new CompanyReadDto(COMPANY_ID, null))
            .when(companyReadMapper).map(any());

        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        Assertions.assertTrue(actualResult.isPresent());
        var expectedResult = new CompanyReadDto(COMPANY_ID, null);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));

        verify(eventPublisher).publishEvent(any(EntityEvent.class));
        verifyNoMoreInteractions(eventPublisher);
    }
}