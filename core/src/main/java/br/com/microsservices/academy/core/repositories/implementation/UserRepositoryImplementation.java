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
                new User(1L,"jonny","$2a$10$XXFr7Bp3r3Y1iXdzcEIRreoqL.berHEt5JJIJm9AJNlcTdsynCK0q","USER"), //password: 123
                new User(2L,"joao","$2a$10$lL/xm2vG3qAyUyUbIb3rZeoQoQOak9S2KdNmG3DJ08LQseK0hG7wm","USER"), //password: 456
                new User(3L,"john","$2a$10$BYuan7zdLYzGDqpKPnx8g.3iLSdFbSFVKJu.9rVrfO2WFjHqfvIFa","ADMIN") //password: 789
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

    @Override
    public Optional<User> findByUsername(String username) {
        return mockDataCourses.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }
}
