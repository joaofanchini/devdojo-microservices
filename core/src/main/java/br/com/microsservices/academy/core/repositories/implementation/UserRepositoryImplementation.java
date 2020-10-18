package br.com.microsservices.academy.core.repositories.implementation;

import br.com.microsservices.academy.core.models.User;
import br.com.microsservices.academy.core.repositories.UserRepository;
import br.com.microsservices.academy.core.request.PageableDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImplementation implements UserRepository {

    private final List<User> mockDataCourses;

    public UserRepositoryImplementation() {
        mockDataCourses = Arrays.asList(
                new User(1L,"jonny","123"),
                new User(2L,"joao","456"),
                new User(3L,"john","789")
        );
    }

    @Override
    public Iterable<User> findAll(PageableDTO pageable) {
        Integer pageSize = pageable.getPageSize();
        Integer pageNumber = pageable.getPageNumber();

        int intervalIndex = mockDataCourses.size() / pageNumber;
        int finalIndex = (pageSize * pageNumber);

        return Optional.of(mockDataCourses.subList(finalIndex - intervalIndex, finalIndex)).orElse(new ArrayList<>());
    }
}
