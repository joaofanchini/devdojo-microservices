package br.com.microsservices.academy.course.data.repositories;

import br.com.microsservices.academy.course.data.models.BaseModel;
import br.com.microsservices.academy.course.dtos.request.PageableDTO;


public interface RepositoryBase<ENTITY extends BaseModel> {
    Iterable<ENTITY> findAll(PageableDTO pageable);
}
