package org.example.database.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.example.database.entity.Role;
import org.example.database.entity.User;
import org.example.database.querydsl.QPredicates;
import org.example.dto.UserFilter;
import org.example.dto.WorkerInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.database.entity.QUser.user;

@RequiredArgsConstructor
public class UserFilterRepositoryImpl implements UserFilterRepository {

    private static final String FIND_BY_COMPANY_AND_ROLE = """
        SELECT
        firstname,
        lastname,
        birth_date
        FROM users
        WHERE company_id = ?
        AND role = ?
        """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
        UPDATE
        users
        SET company_id = ?, role = ?
        WHERE id = ?
        """;

    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
        UPDATE
        users
        SET company_id = :company_id, role = :role
        WHERE id = :id
        """;


    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<User> findAllByFilterWithQueryDSL(UserFilter filter) {
        var predicate = QPredicates.builder()
            .add(filter.firstname(), user.firstname::containsIgnoreCase)
            .add(filter.lastname(), user.lastname::containsIgnoreCase)
            .add(filter.birthDateAfter(), user.birthDate::after)
            .add(filter.birthDateBefore(), user.birthDate::before)
            .build();

        return new JPAQuery<User>(entityManager)
            .select(user)
            .from(user)
            .where(predicate)
            .fetch();
    }

    @Override
    public List<User> findAllByFilterWithCriteria(UserFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);

        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.firstname() != null) {
            predicates.add(criteriaBuilder.like(userRoot.get("firstname"), filter.firstname()));
        }
        if (filter.lastname() != null) {
            predicates.add(criteriaBuilder.like(userRoot.get("lastname"), filter.lastname()));
        }
        if (filter.birthDateAfter() != null) {
            predicates.add(criteriaBuilder.greaterThan(userRoot.get("birthDate"), filter.birthDateAfter()));
        }
        if (filter.birthDateBefore() != null) {
            predicates.add(criteriaBuilder.lessThan(userRoot.get("birthDate"), filter.birthDateBefore()));
        }
        criteria.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<WorkerInfo> findInfoByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ROLE, (rs, rowNum) -> new WorkerInfo(
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getDate("birth_date").toLocalDate()),
            companyId,
            role.name());
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        List<Object[]> args = users.stream()
            .map(user -> new Object[]{user.getCompany().getId(), user.getRole().name(), user.getId()}).toList();

        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        MapSqlParameterSource[] args = users.stream().map(user -> Map.of(
            "company_id", user.getCompany().getId(),
            "role", user.getRole().name(),
            "id", user.getId()
        )).map(MapSqlParameterSource::new).toArray(MapSqlParameterSource[]::new);

        namedJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);

        System.out.println();
    }
}
