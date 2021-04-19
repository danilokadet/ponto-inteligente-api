package br.com.pontointeligente.pontointeligente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.pontointeligente.pontointeligente.entities.Funcionario;

@Transactional(readOnly = true)
public interface FuncionarioRepositoryTest extends JpaRepository<Funcionario, Long> {
	
	Funcionario FindbyCpf(String cpf);
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpfOrEmail(String cpf, String email);

}
