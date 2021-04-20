package br.com.pontointeligente.pontointeligente.repositories;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.pontointeligente.pontointeligente.entities.Lancamento;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "LancamentoRepository.findByFuncionarioId",
			query = "SELECT lanc FROM Lancamento lanc WHERE lanc.funcionario.id = :funcionarioId")
})
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	Page<Lancamento> findByFuncionarioId(@Param("funcionario") Long funcionarioId, PageRequest pageRequest);
	
	// Cria uma paginação para a página
	//Page<Lancamento> findByFuncionarioId(@Param("funcioanrioId") Pageable funcioanrioId);

}
