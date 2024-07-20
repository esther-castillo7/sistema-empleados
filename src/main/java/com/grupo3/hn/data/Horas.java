package com.grupo3.hn.data;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Horas extends AbstractEntity {

    private Integer codigo;
    private String nombre_completo;
    private String departamento;
    private LocalDate fecha_ingreso;
    private LocalDate fecha_salida;
    private Integer horas;

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
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public LocalDate getFecha_ingreso() {
        return fecha_ingreso;
    }
    public void setFecha_ingreso(LocalDate fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
    public LocalDate getFecha_salida() {
        return fecha_salida;
    }
    public void setFecha_salida(LocalDate fecha_salida) {
        this.fecha_salida = fecha_salida;
    }
    public Integer getHoras() {
        return horas;
    }
    public void setHoras(Integer horas) {
        this.horas = horas;
    }

}
