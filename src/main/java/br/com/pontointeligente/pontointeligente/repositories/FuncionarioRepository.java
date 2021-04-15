package br.com.pontointeligente.pontointeligente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pontointeligente.pontointeligente.entities.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

}
