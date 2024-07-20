package com.grupo3.hn.services;

import com.grupo3.hn.data.Horas;
import com.grupo3.hn.data.HorasRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class HorasService {

    private final HorasRepository repository;

    public HorasService(HorasRepository repository) {
        this.repository = repository;
    }

    public Optional<Horas> get(Long id) {
        return repository.findById(id);
    }

    public Horas update(Horas entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Horas> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Horas> list(Pageable pageable, Specification<Horas> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
