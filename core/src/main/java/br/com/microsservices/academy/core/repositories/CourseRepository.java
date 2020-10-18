package br.com.microsservices.academy.core.repositories;

import br.com.microsservices.academy.core.models.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends RepositoryBase<Course> {
}
