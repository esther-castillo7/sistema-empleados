package com.grupo3.hn.repository;

import com.grupo3.hn.data.EmpleadosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface DatabaseRepository {
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("/pls/apex/20192001_castillo/gestionemp/empleados")
	Call<EmpleadosResponse> getEmpleados();
	
}