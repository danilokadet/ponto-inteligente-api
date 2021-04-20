package br.com.pontointeligente.pontointeligente.responsies;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {
	
	private T data;
	private List<String> error;
	
	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getError() {
		
		if (this.error == null) {
			this.error = new ArrayList<String>();
		}
		return error;
	}

	public void setError(List<String> error) {
		this.error = error;
	}
	
	

}
