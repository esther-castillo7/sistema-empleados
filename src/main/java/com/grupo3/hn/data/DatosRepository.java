package com.grupo3.hn.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DatosRepository extends JpaRepository<Datos, Long>, JpaSpecificationExecutor<Datos> {

}
