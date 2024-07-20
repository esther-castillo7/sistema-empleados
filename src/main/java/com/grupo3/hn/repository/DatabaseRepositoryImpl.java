package com.grupo3.hn.repository;

import java.io.IOException;
import com.grupo3.hn.data.EmpleadosResponse;
import retrofit2.Call;
import retrofit2.Response;

public class DatabaseRepositoryImpl {
	private static DatabaseRepositoryImpl INSTANCE;
	private DatabaseClient cliente;
	
	private DatabaseRepositoryImpl(String url, Long timeout) {
		cliente = new DatabaseClient(url,timeout);
		
	}
	
	public static DatabaseRepositoryImpl getInstance(String url, Long timeout) {
		if(INSTANCE == null) {
			synchronized(DatabaseRepositoryImpl.class) {
				if (INSTANCE == null) {
					INSTANCE = new DatabaseRepositoryImpl(url, timeout);
				}
			}
		}
		return INSTANCE;
	}
	
	public EmpleadosResponse consultarEmpleados() throws IOException{
		Call<EmpleadosResponse> call = cliente.getClient().getEmpleados();
		Response<EmpleadosResponse> response = call.execute();
		if(response.isSuccessful()) {
			return response.body();
		}
		else {
			return null;
		}
	}
}
