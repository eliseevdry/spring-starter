package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.repository.CompanyRepository;
import org.example.dto.CompanyReadDto;
import org.example.listener.entity.AccessType;
import org.example.listener.entity.EntityEvent;
import org.example.mapper.CompanyReadMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyReadMapper companyReadMapper;

    public Optional<CompanyReadDto> findById(Integer id) {
        return companyRepository.findById(id)
            .map(entity -> {
                eventPublisher.publishEvent(new EntityEvent(entity, AccessType.READ));
                return companyReadMapper.map(entity);
            });
    }

    public List<CompanyReadDto> findAll() {
        return companyRepository.findAll().stream()
            .map(companyReadMapper::map)
            .toList();
    }
}
