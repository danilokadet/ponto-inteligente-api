package br.com.pontointeligente.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import br.com.pontointeligente.pontointeligente.api.services.LancamentoService;
import br.com.pontointeligente.pontointeligente.entities.Lancamento;
import br.com.pontointeligente.pontointeligente.repositories.LancamentoRepository;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepo;
	
	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcioanrioId, PageRequest pageRequest) {

		log.info("Buscando lançamentos para o funciário ID {}", funcioanrioId );
		
		return this.lancamentoRepo.findByFuncionarioId(funcioanrioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		
		log.info("Buscando um lançamento pelo ID {}", id);
		return this.lancamentoRepo.findById(id);
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		
		log.info("Persistindo o lançamento: {}", lancamento);
		return this.lancamentoRepo.save(lancamento);
	}

	@Override
	public void remover(Long id) {

		log.info("Removendo o lançamento ID {}", id);
		this.lancamentoRepo.deleteById(id);
		
	}

}
