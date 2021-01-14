package com.lffm1994.osworks.api.controller;

import java.util.List;
import java.util.Optional;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lffm1994.osworks.domain.model.Cliente;
import com.lffm1994.osworks.domain.repository.ClienteRepository;


//Anotando o controlardor para diminuir repetitividade de codigo /clientes abaixo podemos removelos dos metodos

@RestController	
@RequestMapping("/clientes")
public class ClienteController {
	/*
	 variavel de instancia do javax.persistence e 
	 uma interface do Jakarta utilizada para ações 
	 nas entidades.
	 
	@PersistenceContext
	private EntityManager manager; 
	*/
	
	
	/*
	 """PARA PROTOTIPO""" 
	 PROJETOS PROFISSIONAIS TEM QUE SEPARAR RESPONSABILIDADE COM CLASSE RESPONSAVEL PELA EXECUÇÃO DE METODOS
	 PRIMEIRO - deve ser inserido o manager
	 SEGUNDO - o que deve ser feito pelo createQuery
	 TERCEIRO - "from Cliente" um JPQL é um Metodo para retornar lista de clientes
	 QUATO - Tipo
	 QUINTO - Método get  
	 */
	
	// Autowired dizendo que quer uma instancia de clienteRepository com isso iremos separar as responsabilidades
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping
	public List<Cliente> lista() {
		return clienteRepository.findAll();
		
		/*
		exemplos de consultas customizada por nome espeficico
		return clienteRepository.findByNome("a");
		exemplos de consultas customizada por letra espeficica
		return clienteRepository.findByNomeContaining("a");
		*/
		
		
		/*
		 REMOVEREMOS TUDO QUE TINHA INTERAÇÃO DIRETA COM JPA, POIS UTILIZAREMOS O clienteRepository
		 return manager.createQuery("from Cliente", Cliente.class)
			.getResultList();
		 */
		
	}
	
	
	//vamos inserir uma variavel de caminho para selecionar um consulta especifica
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		//logica em caso de não achar o variavel de busca, ResponseEntity representa a responsata que será retornada 
		if(cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		//Caso não encontre essa é a devolutiva com código 404
		return ResponseEntity.notFound().build();
	}
	
	
	//metodos de inserção post RequetBody para verificar o corpo de inserção com valores metodo save
	//Anotação ResponseStatus
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@RequestBody Cliente cliente) {
		return clienteRepository.save(cliente);
		
	}
	
	//@PathVariable para vincular ao parametro clienteId
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long clienteId, 
			@RequestBody Cliente cliente) {
		
		//negativa se procurar o id do cliente e não achar
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		//Atualizar o cliente
		cliente.setId(clienteId);
		cliente = clienteRepository.save(cliente);
		
		return ResponseEntity.ok(cliente);
		
	}
	
	
	//noCONTENT CODIGO 204 deu sucesso mais não retorna corpo de mensagem
	@DeleteMapping("/{clienteId}")
	public  ResponseEntity<Void> remover(@PathVariable Long clienteId) {
		
		//negativa se procurar o id do cliente e não achar
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		//remoção do codigo do cliente
		clienteRepository.deleteById(clienteId);
		return ResponseEntity.noContent().build();
	}

}
