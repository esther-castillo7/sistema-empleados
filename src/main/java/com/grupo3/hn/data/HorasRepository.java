package com.grupo3.hn.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HorasRepository extends JpaRepository<Horas, Long>, JpaSpecificationExecutor<Horas> {

}
