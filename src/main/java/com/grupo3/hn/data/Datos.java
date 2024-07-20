package com.grupo3.hn.data;

import jakarta.persistence.Entity;

@Entity
public class Datos extends AbstractEntity {

    private Integer codigo;
    private String nombre_completo;
    private String email;
    private String departamento;

    public Integer getCodigo() {
        return codigo;
    }
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    public String getNombre_completo() {
        return nombre_completo;
    }
    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

}
