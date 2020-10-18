package br.com.microsservices.academy.core.repositories;


import br.com.microsservices.academy.core.request.PageableDTO;

public interface RepositoryBase<ENTITY> {
    Iterable<ENTITY> findAll(PageableDTO pageable);
}
