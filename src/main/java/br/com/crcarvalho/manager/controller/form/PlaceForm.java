package br.com.crcarvalho.manager.controller.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.crcarvalho.manager.model.Place;
import br.com.crcarvalho.manager.repository.PlaceRepository;

public class PlaceForm {
	
	@NotNull @NotEmpty
	private String name;
	
	@NotNull @NotEmpty
	private String slug;
	
	@NotNull @NotEmpty
	private String city;
	
	@NotNull @NotEmpty
	private String state;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Place toConvert() {

		return new Place(name, slug, city, state);
	}

	public Place update(Long id, PlaceRepository placeRepository) {
		Place place = placeRepository.getOne(id);
		place.setName(this.name);
		place.setSlug(this.slug);
		place.setCity(this.city);
		place.setState(this.state);
		place.setUpdatedAt(LocalDateTime.now());
		
		return place;
	}

}
