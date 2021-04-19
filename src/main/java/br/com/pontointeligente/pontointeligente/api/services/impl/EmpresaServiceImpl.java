package br.com.pontointeligente.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pontointeligente.pontointeligente.api.services.EmpersaService;
import br.com.pontointeligente.pontointeligente.entities.Empresa;
import br.com.pontointeligente.pontointeligente.repositories.EmpresaRepository;

@Service
public class EmpresaServiceImpl implements EmpersaService {

	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		
		log.info("Buscando uma empresa para o CNPJ {}", cnpj);
		return Optional.ofNullable(empresaRepo.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		
		log.info("Persistindo empresa {}", empresa);
		return this.empresaRepo.save(empresa);
	}

}
