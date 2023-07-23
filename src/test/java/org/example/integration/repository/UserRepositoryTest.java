package org.example.integration.repository;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Role;
import org.example.database.entity.User;
import org.example.database.repository.UserRepository;
import org.example.dto.PersonalInfo;
import org.example.dto.UserFilter;
import org.example.dto.WorkerInfo;
import org.example.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserRepositoryTest extends BaseIT {

    private final UserRepository userRepository;

    @Test
    void checkJdbcTemplate() {
        List<WorkerInfo> workerInfos = userRepository.findInfoByCompanyIdAndRole(1, Role.USER);

        assertThat(workerInfos).hasSize(1);
    }

    @Test
    void checkJdbcTemplateBatchUpdate() {
        List<User> all = userRepository.findAll();

        userRepository.updateCompanyAndRole(all);

        System.out.println();
    }

    @Test
    void checkJdbcTemplateBatchUpdateNamed() {
        List<User> all = userRepository.findAll();

        userRepository.updateCompanyAndRoleNamed(all);

        System.out.println();
    }

    @Test
    void checkAuditing() {
        User ivan = userRepository.getById(2L);
        ivan.setBirthDate(LocalDate.now().minusYears(30));
        userRepository.flush();
        User ivan2 = userRepository.getById(2L);
        System.out.println(ivan2);
    }

    @Test
    void checkQueryDSL() {
        UserFilter userFilter = new UserFilter(null, "ov", null, LocalDate.now());

        List<User> allByFilter = userRepository.findAllByFilterWithQueryDSL(userFilter);

        assertThat(allByFilter).hasSize(4);
    }

    @Test
    void checkProjection() {
        List<PersonalInfo> allByCompanyId = userRepository.findAllByCompanyId(1);

        List<PersonalInfo> allByCompanyId1 = userRepository.findAllByCompanyId(1, PersonalInfo.class);
        System.out.println();
    }

    @Test
    void checkPageable() {
        var pageable = PageRequest.of(0, 2, Sort.by("id"));
        var page = userRepository.findAllBy(pageable);

        assertThat(page.stream().map(User::getId).toList()).containsExactlyInAnyOrder(1L, 2L);
        assertEquals(5, page.getTotalElements());
        assertEquals(3, page.getTotalPages());

        page.forEach(user -> System.out.println(user.getCompany().getName()));

        while (page.hasNext()) {
            page = userRepository.findAllBy(page.nextPageable());
            assertThat(page.stream().map(User::getId).toList()).containsAnyElementsOf(List.of(3L, 4L, 5L));
            assertEquals(5, page.getTotalElements());
            assertEquals(3, page.getTotalPages());

            page.forEach(user -> System.out.println(user.getCompany().getName()));
        }
    }

    @Test
    void checkSort() {
        var sortBy = Sort.sort(User.class);
        var sort = sortBy.by(User::getFirstname)
            .and(sortBy.by(User::getLastname));

        var sortByNames = Sort.by("firstname").and(Sort.by("lastname"));
        var allUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), sort);
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop() {
        var topWorker = userRepository.findTopByOrderByIdDesc();
        assertTrue(topWorker.isPresent());
        topWorker.ifPresent(workerInfo -> assertEquals("Kate", workerInfo.firstname()));
    }

    @Test
    void checkUpdate() {
        User ivan = userRepository.getById(1L);
        assertSame(Role.ADMIN, ivan.getRole());
        ivan.setBirthDate(LocalDate.of(1991, 3, 20));

        int resultCount = userRepository.updateRoles(Role.USER, 1L, 5L);
        assertEquals(2, resultCount);

        User ivan2 = userRepository.getById(1L);
        assertSame(Role.USER, ivan2.getRole());
    }

    @Test
    void findAllBy() {
        List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
    }
}