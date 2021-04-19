package br.com.pontointeligente.pontointeligente.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.pontointeligente.pontointeligente.entities.Empresa;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest  {
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String CNPJ = "66940121000121";
	
	@Before
	public void setUp() throws Exception {
		
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Ti Soluctions");
		empresa.setCnpj(CNPJ);
		this.empresaRepository.save(empresa);
	}
	
	@After
	public final void tearDown() {
		
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBucarPorCnpj() {
		
		Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
		
		assertEquals(CNPJ, empresa.getCnpj());
	}

}
