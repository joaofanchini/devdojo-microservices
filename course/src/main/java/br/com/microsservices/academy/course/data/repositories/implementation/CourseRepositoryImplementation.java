package br.com.microsservices.academy.course.data.repositories.implementation;

import br.com.microsservices.academy.course.data.models.Course;
import br.com.microsservices.academy.course.data.repositories.CourseRepository;
import br.com.microsservices.academy.course.dtos.request.PageableDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepositoryImplementation implements CourseRepository {

    private final List<Course> mockDataCourses;

    public CourseRepositoryImplementation() {
        mockDataCourses = Arrays.asList(
                new Course(1L,"Title 1"),
                new Course(2L,"Title 2"),
                new Course(3L,"Title 3"),
                new Course(4L,"Title 4"),
                new Course(5L,"Title 5"),
                new Course(6L,"Title 6")
        );
    }

    @Override
    public Iterable<Course> findAll(PageableDTO pageable) {
        Integer pageSize = pageable.getPageSize();
        Integer pageNumber = pageable.getPageNumber();

        int intervalIndex = mockDataCourses.size() / pageNumber;
        int finalIndex = (pageSize * pageNumber);

        return Optional.of(mockDataCourses.subList(finalIndex - intervalIndex, finalIndex)).orElse(new ArrayList<>());
    }
}
