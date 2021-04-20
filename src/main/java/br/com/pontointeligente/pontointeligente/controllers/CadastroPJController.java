package br.com.pontointeligente.pontointeligente.controllers;

import java.security.NoSuchAlgorithmException;

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

import br.com.pontointeligente.pontointeligente.CadastroPJDto;
import br.com.pontointeligente.pontointeligente.api.services.EmpresaService;
import br.com.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.pontointeligente.pontointeligente.entities.Empresa;
import br.com.pontointeligente.pontointeligente.entities.Funcionario;
import br.com.pontointeligente.pontointeligente.enums.PerfilEnum;
import br.com.pontointeligente.pontointeligente.responsies.Response;
import br.com.pontointeligente.pontointeligente.utils.PasswordUtil;

@RestController
@RequestMapping("api/cadastrar-pj")
@CrossOrigin(origins = "*")// habilita o acesso para qualquer domínio
public class CadastroPJController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPJController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPjDto,
			BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PJ: {}",  cadastroPjDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		validaDadoaExistente(cadastroPjDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPjDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPjDto, result);
		
		if (result.hasErrors()) {
			
			log.info("Erro validando dados de catadastro PJ {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getError().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPjDto(funcionario));
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Converta os dados DTO para empresa
	 * @param cadastroPjDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa(@Valid CadastroPJDto cadastroPjDto) {
		
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPjDto.getCnpj());
		empresa.setRazaoSocial(cadastroPjDto.getRazaoSocial());
		return empresa;
	}
	
	/**
	 * Converte os dados DTO para funiconario
	 * @param cadastroPjDto
	 * @param result
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPjDto, BindingResult result)
	throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPjDto.getNome());
		funcionario.setEmail(cadastroPjDto.getEmail());
		funcionario.setCpf(cadastroPjDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtil.gerarBCrypt(cadastroPjDto.getSenha()));
		
		return funcionario;
		
	}

	/**
	 * Verifica se a empresa ou funcionario já existem na base de dados.
	 * @param cadastroPjDto
	 * @param result validaDadoaExistente
	 */
	private void validaDadoaExistente(CadastroPJDto cadastroPjDto, BindingResult result) {
		
		this.empresaService.buscarPorCnpj(cadastroPjDto.getCnpj())
		.ifPresent(emp -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(cadastroPjDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente")));
		
		this.funcionarioService.buscarPorCpf(cadastroPjDto.getCpf())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente")));
		
	}
	
	/**
	 * Popula o DTO de cadastro com os dados do funionario e empresa
	 * @param funcionario
	 * @return CadastroPJDto
	 */
	private CadastroPJDto converterCadastroPjDto(Funcionario funcionario) {
		
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastroPJDto;
		
		
	}
	
}
