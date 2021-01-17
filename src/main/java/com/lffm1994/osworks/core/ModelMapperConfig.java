package com.lffm1994.osworks.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Essa classe vai retornar um ModellMapper
 * Configuration componente spring
 * Metodo utilizaremos o Bean ele indica que esse metodo ele instancia, inicializa, 
 * configura um Bean do tipo Model Mapper que será gerenciado pelo spring. 
 * Será disponibiliado para injeção de dependência em outras classes.
 * Só fazendo isso ele vai scanear e instanciar um ModelMapper
 */

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
