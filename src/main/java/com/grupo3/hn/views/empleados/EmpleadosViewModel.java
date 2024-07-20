package com.grupo3.hn.views.empleados;

import java.util.List;
import com.grupo3.hn.data.Datos;

public interface EmpleadosViewModel {
	
	void mostrarEmpleados(List<Datos> items);
	void mensajeExito(String mensaje);
	void mensajeError(String mensaje);
}