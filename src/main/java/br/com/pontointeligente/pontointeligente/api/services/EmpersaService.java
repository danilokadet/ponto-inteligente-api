package br.com.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import br.com.pontointeligente.pontointeligente.entities.Empresa;

public interface EmpersaService {
	
	/**
	 * Retorna uma empresa dado um CNPJ
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	
	/**
	 * Cadastra uma nova empresa na base de dados
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);

}
