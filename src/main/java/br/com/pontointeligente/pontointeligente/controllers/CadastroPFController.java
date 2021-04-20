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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pontointeligente.pontointeligente.api.dtos.CadastroPFDto;
import br.com.pontointeligente.pontointeligente.api.services.EmpresaService;
import br.com.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.pontointeligente.pontointeligente.entities.Empresa;
import br.com.pontointeligente.pontointeligente.entities.Funcionario;
import br.com.pontointeligente.pontointeligente.enums.PerfilEnum;
import br.com.pontointeligente.pontointeligente.responsies.Response;
import br.com.pontointeligente.pontointeligente.utils.PasswordUtil;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(CadastroPFController.class);

	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public CadastroPFController() {
	}
	
	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa
	 * @param cadastroPFDto
	 * @param result
	 * @return CadastroPFDto
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
			BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PF : {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoPataFucionario(cadastroPFDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validadno dados cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getError()
					.add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.conveterCadastroPFDto(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funciário não existe na base de dados
	 * @param cadastroPFDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não encontrada."));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
		.ifPresent(func -> result.addError(new ObjectError("Funcionario", "CPF não encontrado.")));
		
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email não encontrado.")));
	}
	
	/**
	 * Converte os dados DTO para funcionario
	 * @param cadastroPFDto
	 * @param result
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoPataFucionario(CadastroPFDto cadastroPFDto, BindingResult result) 
	throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtil.gerarBCrypt(cadastroPFDto.getSenha()));

		cadastroPFDto.getQtdHorasAlmoco()
		.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhoDia()
		.ifPresent(qtdHorasTrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalho)));
		cadastroPFDto.getValorHora()
		.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
		
	}
	
	/**
	 * Converte Objeto funcionaior para DTO
	 * 
	 * @param funcionario
	 * @return CadastroPFDto
	 */
	private CadastroPFDto conveterCadastroPFDto(Funcionario funcionario) {
		
		CadastroPFDto cadastroPFDto = new CadastroPFDto();
		cadastroPFDto.setId(funcionario.getId());
		cadastroPFDto.setNome(funcionario.getNome());
		cadastroPFDto.setEmail(funcionario.getEmail());
		cadastroPFDto.setCpf(funcionario.getCpf());
		cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());

		funcionario.getQtdHorasAlmocoOpt()
		.ifPresent(qtdHorasAlmoco -> 
		cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
		.ifPresent(qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHorasOpt()
		.ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));
		
		return cadastroPFDto;
	}
	
}
