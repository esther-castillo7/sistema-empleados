package com.grupo3.hn.controller;

import com.grupo3.hn.repository.DatabaseRepositoryImpl;
import com.grupo3.hn.views.empleados.EmpleadosViewModel;
import com.grupo3.hn.data.EmpleadosResponse;

public class EmpleadosInteractorImpl implements EmpleadosInteractor {

    private DatabaseRepositoryImpl modelo;
    private EmpleadosViewModel vista;

    public EmpleadosInteractorImpl(EmpleadosViewModel vista) {
        super();
        this.vista = vista;
        this.modelo = DatabaseRepositoryImpl.getInstance("/pls/apex/20192001_castillo/gestionemp/empleados", 30000L); // TIEMPO EN MILISEGUNDOS
    }

    @Override
    public void consultarEmpleados() {
        // ESTRUCTURA TRY...CATCH PARA MANEJO DE ERRORES
        try {
            // EL CODIGO QUE SE EJECUTA EN EL HAPPY PATH
            EmpleadosResponse respuesta = this.modelo.consultarEmpleados();
            if (respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
                this.vista.mensajeError("No hay Empleados que mostrar");
            } else {
                this.vista.mostrarEmpleados(respuesta.getItems());
            }
        } catch (Exception error) {
            // EL CODIGO QUE SE EJECUTA CUANDO HAY PROBLEMAS
            error.printStackTrace();
            this.vista.mensajeError("Ha ocurrido un problema, revisa tu conexión a internet");
        }
    }
}

	
