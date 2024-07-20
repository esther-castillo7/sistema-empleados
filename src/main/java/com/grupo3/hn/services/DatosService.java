package com.grupo3.hn.services;

import com.grupo3.hn.data.Datos;
import com.grupo3.hn.data.DatosRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DatosService {

    private final DatosRepository repository;

    public DatosService(DatosRepository repository) {
        this.repository = repository;
    }

    public Optional<Datos> get(Long id) {
        return repository.findById(id);
    }

    public Datos update(Datos entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Datos> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Datos> list(Pageable pageable, Specification<Datos> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
