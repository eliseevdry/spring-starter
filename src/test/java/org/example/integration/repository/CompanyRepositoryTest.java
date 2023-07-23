package org.example.integration.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.database.entity.Company;
import org.example.database.repository.CompanyRepository;
import org.example.integration.BaseIT;
import org.example.integration.annotation.IT;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CompanyRepositoryTest extends BaseIT {

    public static final Integer APPLE_ID = 5;

    private final EntityManager entityManager;

    private final CompanyRepository companyRepository;

    @Test
    void findByQueries() {
        companyRepository.findByName("google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    void findById() {
        Company company = entityManager.find(Company.class, 1);
        assertNotNull(company);
        Assertions.assertThat(company.getLocales()).hasSize(2);
    }

    @Test
    @Disabled
    void delete() {
        Optional<Company> maybeCompany = companyRepository.findById(APPLE_ID);
        assertTrue(maybeCompany.isPresent());
        companyRepository.delete(maybeCompany.get());
        entityManager.flush();
        maybeCompany = companyRepository.findById(APPLE_ID);
        assertFalse(maybeCompany.isPresent());
    }

    @Test
    void save() {
        Company company = Company.builder()
            .name("Apple")
            .locales(Map.of(
                "ru", "Apple описание",
                "en", "Apple description"
            ))
            .build();
        entityManager.persist(company);
        assertNotNull(company.getId());
    }
}