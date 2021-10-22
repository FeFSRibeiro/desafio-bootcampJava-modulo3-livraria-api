package br.com.online.livraria.services;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.online.livraria.dto.LivroDto;
import br.com.online.livraria.dto.LivroFormDto;
import br.com.online.livraria.modelo.Autor;
import br.com.online.livraria.modelo.Livro;
import br.com.online.livraria.repository.AutorRepository;
import br.com.online.livraria.repository.LivroRepository;


@Service
public class LivroService {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
	

	public Page<LivroDto> listar(Pageable paginacao) {
		Page<Livro> livros = livroRepository.findAll(paginacao);
		return livros
				.map(t -> modelMapper.map(t, LivroDto.class));
	}
	
	@Transactional
	public LivroDto cadastrar(LivroFormDto dto) {
		
		Long idAutor = dto.getAutorId();
		try {
		Livro livro = modelMapper.map(dto, Livro.class);
		Autor autor = autorRepository.getById(idAutor);

		livro.setId(null);
		livro.setAutor(autor);
		livroRepository.save(livro);

		return modelMapper.map(livro, LivroDto.class);
		
	} catch (EntityNotFoundException e) {
		throw new IllegalArgumentException("Autor inexistente!");
	}

	}
}
