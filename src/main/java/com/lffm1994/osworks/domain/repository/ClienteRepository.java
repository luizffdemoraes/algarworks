package com.lffm1994.osworks.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lffm1994.osworks.domain.model.Cliente;

/*
 Primeiro usamos o extends informamos a interface do jpa.repository 
 Segundo informamos a tipagem o tipo da entidade e qual o tipo do identificador. 
 Anotação Repository define que essa interface e um componente do spring gerenciado pelo spring
 */

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	// Caso haja necessidade de consultas especificas podemos utilizar query metodos
	
	List<Cliente> findByNome(String nome);
	List<Cliente> findByNomeContaining(String nome);

}
