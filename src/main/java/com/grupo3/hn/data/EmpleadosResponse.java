package com.grupo3.hn.data;

import java.util.List;

public class EmpleadosResponse {

	private List<Datos> items;
	private int count;
	
	public List<Datos> getItems() {
		return items;
	}
	public void setItems(List<Datos> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
