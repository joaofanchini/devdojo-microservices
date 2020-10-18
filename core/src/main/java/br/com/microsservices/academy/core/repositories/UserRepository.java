package br.com.microsservices.academy.core.repositories;

import br.com.microsservices.academy.core.models.Course;
import br.com.microsservices.academy.core.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends RepositoryBase<User> {
    Optional<User> findByUsername(String username);
}
