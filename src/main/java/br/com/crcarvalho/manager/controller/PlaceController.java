package br.com.crcarvalho.manager.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.crcarvalho.manager.controller.form.PlaceForm;
import br.com.crcarvalho.manager.model.Place;
import br.com.crcarvalho.manager.repository.PlaceRepository;

@RestController
@RequestMapping(
		value = "places",
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
		consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
		)
public class PlaceController {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@GetMapping(consumes = MediaType.ALL_VALUE)
	public Page<Place> toList(@PageableDefault(page = 0, size = 10) Pageable pagination,
			@RequestParam(required = false) String name){

		return (name == null) ? placeRepository.findAll(pagination) : placeRepository.findByName(name, pagination);
		
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Place> register(@RequestBody @Valid PlaceForm form, UriComponentsBuilder uriBuilder){
		Place place = form.toConvert();
		this.placeRepository.save(place);
		
		URI location = uriBuilder.path("/places/{id}")
				.buildAndExpand(place.getId())
				.toUri();
		
		
		return ResponseEntity.created(location).body(place);
	}
	
	@GetMapping(value = "{id}", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<Place> detail(@PathVariable("id") Long id) {
		Optional<Place> optional = placeRepository.findById(id);
		
		if(optional.isPresent()) {
			return ResponseEntity.ok().body(optional.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("{id}")
	@Transactional
	public ResponseEntity<Place> update(@PathVariable("id") Long id, @RequestBody @Valid PlaceForm form) {
		Optional<Place> optional = placeRepository.findById(id);
		
		if(optional.isPresent()) {
			Place place = form.update(id, placeRepository);
			return ResponseEntity.ok().body(place);
		}
		
		return ResponseEntity.notFound().build();
	}
	
}
