package br.com.pontointeligente.pontointeligente.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pontointeligente.pontointeligente.api.dtos.FuncionarioDto;
import br.com.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.pontointeligente.pontointeligente.entities.Funcionario;
import br.com.pontointeligente.pontointeligente.responsies.Response;
import br.com.pontointeligente.pontointeligente.utils.PasswordUtil;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public FuncionarioController() {
	}

	/**
	 * Atualiza os dados de um funcionário
	 * @param id
	 * @param dto
	 * @param result
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id, 
			@Valid FuncionarioDto dto, BindingResult result ) throws NoSuchAlgorithmException {
		
		log.info("Atualizando funcionário: {}", dto.toString());
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = this.funcionarioService.bucarPorId(id);
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado"));
		}
		
		this.atulizarDadosFuncionario(funcionario.get(), dto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando funcionario: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getError().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converterFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um DTO com os dadso de um funcionário.
	 * @param funcionario
	 * @return
	 */
	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {

		FuncionarioDto dto = new FuncionarioDto();
		dto.setId(funcionario.getId());
		dto.setEmail(funcionario.getEmail());
		dto.setNome(funcionario.getNome());
		
		funcionario.getQtdHorasAlmocoOpt ()
		.ifPresent(qtdHoraAlmoco -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHoraAlmoco))));
		
		funcionario.getQtdHorasTrabalhoDiaOpt()
		.ifPresent(qtdHorasTbDi -> dto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTbDi))));
		
		funcionario.getValorHorasOpt()
		.ifPresent(valorHora -> dto.setValorHora(Optional.of(valorHora.toString())));
		
		return dto;
	}

	/**
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * @param funcionario
	 * @param dto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atulizarDadosFuncionario(Funcionario funcionario, @Valid FuncionarioDto dto, BindingResult result) 
	throws NoSuchAlgorithmException {
		
		funcionario.setNome(dto.getNome());
		
		if(!funcionario.getEmail().equals(dto.getEmail())) {
			this.funcionarioService.buscarPorEmail(dto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente")));
			funcionario.setEmail(dto.getEmail());
		}
		
		funcionario.setQtdHorasAlmoco(null);
		dto.getQtdHorasAlmoco()
		.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		
		funcionario.setQtdHorasTrabalhoDia(null);
		dto.getQtdHorasTrabalhoDia()
		.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		
		funcionario.setValorHora(null);
		dto.getValorHora()
		.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		if(dto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtil.gerarBCrypt(funcionario.getSenha()));
		}
	}
	
}
