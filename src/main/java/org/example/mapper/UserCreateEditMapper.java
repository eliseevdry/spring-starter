package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Company;
import org.example.database.entity.User;
import org.example.database.repository.CompanyRepository;
import org.example.dto.UserCreateEditDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User map(UserCreateEditDto source) {
        return map(source, new User());
    }

    @Override
    public User map(UserCreateEditDto source, User target) {
        target.setUsername(source.getUsername());
        target.setBirthDate(source.getBirthDate());
        target.setFirstname(source.getFirstname());
        target.setLastname(source.getLastname());
        target.setRole(source.getRole());
        target.setCompany(getCompany(source.getCompanyId()));
        return target;
    }

    private Company getCompany(Integer id) {
        return Optional.ofNullable(id)
            .map(companyRepository::getById)
            .orElse(null);
    }
}
