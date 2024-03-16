package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.entity.User;
import org.example.database.querydsl.QPredicates;
import org.example.database.repository.UserRepository;
import org.example.dto.UserCreateEditDto;
import org.example.dto.UserFilter;
import org.example.dto.UserReadDto;
import org.example.mapper.UserCreateEditMapper;
import org.example.mapper.UserReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final ImageService imageService;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDateAfter(), user.birthDate::after)
                .add(filter.birthDateBefore(), user.birthDate::before)
                .build();

        return userRepository.findAll(predicate, pageable).map(userReadMapper::map);
    }

//    @PostFilter("filterObject.role.name().equals('USER')")
//    @PostFilter("@companyService.findById(filterObject.company.id()).isPresent()")
    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }

    //    @PostAuthorize("returnObject")
    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id).map(User::getImage).filter(StringUtils::hasText).flatMap(imageService::get);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(dto -> {
                            uploadImage(dto.getImage());
                            return userCreateEditMapper.map(dto);
                        }
                )
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return Optional.of(id)
                .map(userRepository::getById)
                .map(user -> {
                    uploadImage(userCreateEditDto.getImage());
                    return userCreateEditMapper.map(userCreateEditDto, user);
                })
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(daoUser ->
                        new org.springframework.security.core.userdetails.User(
                                daoUser.getUsername(),
                                daoUser.getPassword(),
                                Collections.singleton(daoUser.getRole())
                        ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
