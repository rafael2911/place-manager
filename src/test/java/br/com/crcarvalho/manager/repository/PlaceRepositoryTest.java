package br.com.crcarvalho.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.crcarvalho.manager.model.Place;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceRepositoryTest {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Before
	public void carryPlaces() {
		if(placeRepository.findAll().isEmpty()) {
			placeRepository.save(new Place("Test One", "Test", "Test One", "One"));
			placeRepository.save(new Place("Test Two", "Test", "Test Two", "Two"));
		}
	}
	
	@Test
	public void testLoadPlaceList() {
		Page<Place> places = this.placeRepository.findAll(PageRequest.of(0, 10));
		assertThat(places.getTotalElements()).isGreaterThan(1);
	}
	
	@Test
	public void testSearchPlaceByNonexistentName() {
		Page<Place> places = this.placeRepository.findByName("Test", PageRequest.of(0, 10));
		assertThat(places.getTotalElements()).isEqualTo(0);
	}
	
	@Test
	public void testRegisterNewPlace() {
		Place place = this.placeRepository.save(new Place("City Name", "City Slug", "City Name", "State"));
		assertThat(place).isNotNull();
		assertThat(place.getName()).isEqualTo("City Name");
		assertThat(place.getSlug()).isEqualTo("City Slug");
		assertThat(place.getCity()).isEqualTo("City Name");
		assertThat(place.getState()).isEqualTo("State");
		assertThat(place.getCreatedAt()).isNotNull();
	}
	
	@Test
	public void testFindPlaceByExistingId() {
		Optional<Place> optional = this.placeRepository.findById(2L);
		assertThat(optional.isPresent()).isTrue();
		assertThat(optional.get().getName()).isEqualTo("Test Two");
		assertThat(optional.get().getSlug()).isEqualTo("Test");
		assertThat(optional.get().getCity()).isEqualTo("Test Two");
		assertThat(optional.get().getState()).isEqualTo("Two");
		assertThat(optional.get().getCreatedAt()).isNotNull();
	}
	
	@Test
	public void testFindPlaceByNonexistingId() {
		Optional<Place> optional = this.placeRepository.findById(4L);
		assertThat(optional.isPresent()).isFalse();
	}
		
}
