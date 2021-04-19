package br.com.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.pontointeligente.pontointeligente.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * Retorna um lançamento po ID
	 * @param funcioanrioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcioanrioId, PageRequest pageRequest);
	
	/**
	 * Retorna lançamento por ID
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	
	/**
	 * Persiste um lançamento na base de dados
	 * @param lancamento
	 * @return
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remore um lançamento da base de dados
	 * @param id
	 */
	void remover(Long id);

}
