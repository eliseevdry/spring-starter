package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Company;
import org.example.database.entity.User;
import org.example.database.repository.CompanyRepository;
import org.example.dto.UserCreateEditDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

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

        Optional.ofNullable(source.getPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(target::setPassword);

        Optional.ofNullable(source.getImage())
                .filter(Objects::nonNull)
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> target.setImage(image.getOriginalFilename()));

        return target;
    }

    private Company getCompany(Integer id) {
        return Optional.ofNullable(id)
                .map(companyRepository::getById)
                .orElse(null);
    }
}
