package br.com.microsservices.academy.course.services;

import br.com.microsservices.academy.course.data.models.Course;
import br.com.microsservices.academy.course.data.repositories.CourseRepository;
import br.com.microsservices.academy.course.dtos.request.PageableDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseService {
    private final CourseRepository courseRepository;

    public Iterable<Course> listCourses(PageableDTO pageableDto) {
        log.info("Request pageableDto: ".concat(pageableDto.toString()));
        return courseRepository.findAll(pageableDto);
    }
}
