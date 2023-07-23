package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.querydsl.QPredicates;
import org.example.database.repository.UserRepository;
import org.example.dto.UserCreateEditDto;
import org.example.dto.UserFilter;
import org.example.dto.UserReadDto;
import org.example.mapper.UserCreateEditMapper;
import org.example.mapper.UserReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
            .add(filter.firstname(), user.firstname::containsIgnoreCase)
            .add(filter.lastname(), user.lastname::containsIgnoreCase)
            .add(filter.birthDateAfter(), user.birthDate::after)
            .add(filter.birthDateBefore(), user.birthDate::before)
            .build();

        return userRepository.findAll(predicate, pageable).map(userReadMapper::map);
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
            .map(userCreateEditMapper::map)
            .map(userRepository::save)
            .map(userReadMapper::map)
            .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return Optional.of(id)
            .map(userRepository::getById)
            .map(user -> userCreateEditMapper.map(userCreateEditDto, user))
            .map(userRepository::saveAndFlush)
            .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
            .map(user -> {
                userRepository.delete(user);
                userRepository.flush();
                return true;
            })
            .orElse(false);
    }
}
