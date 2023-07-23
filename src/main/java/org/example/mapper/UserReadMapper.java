package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.User;
import org.example.dto.CompanyReadDto;
import org.example.dto.UserReadDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {
    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto map(User source) {
        CompanyReadDto companyReadDto = Optional.ofNullable(source.getCompany())
            .map(companyReadMapper::map)
            .orElse(null);

        return new UserReadDto(
            source.getId(),
            source.getUsername(),
            source.getBirthDate(),
            source.getFirstname(),
            source.getLastname(),
            source.getRole(),
            companyReadDto
        );
    }
}
