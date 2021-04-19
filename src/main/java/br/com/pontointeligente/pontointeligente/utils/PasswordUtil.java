package br.com.pontointeligente.pontointeligente.utils;

import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(PasswordUtil.class);
	
	public PasswordUtil() {
	}
	
	
	// md5 para o campo senha automatico
	public static String gerarBCrypt(String senha) {
		
		if (senha == null) {
			
			return senha;
		}
		
		log.info("Gerendo hash com BCrype");
		 BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		 
		 return bCryptPasswordEncoder.encode(senha);
	}
	
	

}
