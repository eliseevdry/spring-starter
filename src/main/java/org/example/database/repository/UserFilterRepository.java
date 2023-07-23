package org.example.database.repository;

import org.example.database.entity.Role;
import org.example.database.entity.User;
import org.example.dto.UserFilter;
import org.example.dto.WorkerInfo;

import java.util.List;

public interface UserFilterRepository {
    List<User> findAllByFilterWithQueryDSL(UserFilter filter);

    List<User> findAllByFilterWithCriteria(UserFilter filter);

    List<WorkerInfo> findInfoByCompanyIdAndRole(Integer companyId, Role role);

    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}
