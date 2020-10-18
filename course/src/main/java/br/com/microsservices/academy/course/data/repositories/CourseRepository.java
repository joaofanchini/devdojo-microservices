package br.com.microsservices.academy.course.data.repositories;

import br.com.microsservices.academy.course.data.models.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends RepositoryBase<Course> {
}
