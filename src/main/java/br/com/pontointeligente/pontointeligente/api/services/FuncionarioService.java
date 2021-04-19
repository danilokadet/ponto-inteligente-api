package br.com.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import br.com.pontointeligente.pontointeligente.entities.Funcionario;

public interface FuncionarioService {
	
	/**
	 * Persisti um funcioanrio na base de dados
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca e retorna um funcionario por CPF
	 * @param cpj
	 * @return Funcionario
	 */
	Optional<Funcionario> buscarPorCpf(String cpj);
	
	/**
	 * Busca e retorna um funcionario dado um email
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 * Busca e retorna um funcionario por ID
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> bucarPorId(Long id);

}
