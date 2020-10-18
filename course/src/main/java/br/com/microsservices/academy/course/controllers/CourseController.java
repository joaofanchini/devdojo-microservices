package br.com.microsservices.academy.course.controllers;

import br.com.microsservices.academy.course.data.models.Course;
import br.com.microsservices.academy.course.dtos.request.PageableDTO;
import br.com.microsservices.academy.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/courses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<Iterable<Course>> getCourses(@Valid PageableDTO pageable){
        return ResponseEntity.ok(courseService.listCourses(pageable));
    }
}
