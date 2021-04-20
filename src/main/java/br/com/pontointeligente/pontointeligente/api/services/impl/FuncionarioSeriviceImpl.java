package br.com.pontointeligente.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.pontointeligente.pontointeligente.entities.Funcionario;
import br.com.pontointeligente.pontointeligente.repositories.FuncionarioRepository;

@Service
public class FuncionarioSeriviceImpl implements FuncionarioService {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(FuncionarioSeriviceImpl.class);
	
	@Autowired
	private FuncionarioRepository funcionaiorRepo;

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		
		log.info("Persisitindo funcionario {}", funcionario);
		return this.funcionaiorRepo.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {

		log.info("Buscando funcionario pelo CNPJ {}", cpf);
		return Optional.ofNullable(this.funcionaiorRepo.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		
		log.info("Bucando funcionario pelo email {}", email);
		return Optional.ofNullable(this.funcionaiorRepo.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> bucarPorId(Long id) {
		
		log.info("Bucando funcionario pelo email {}", id);
		return this.funcionaiorRepo.findById(id);
	}
	

}
