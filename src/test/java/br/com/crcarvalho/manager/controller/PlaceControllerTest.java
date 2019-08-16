package br.com.crcarvalho.manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.crcarvalho.manager.model.Place;
import br.com.crcarvalho.manager.repository.PlaceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PlaceControllerTest {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@TestConfiguration
	static class Config{
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder();
		}
	}
	
	@Before
	public void setUp() {
		if(placeRepository.findAll().isEmpty()) {
			placeRepository.save(new Place("Test One", "Test", "Test One", "One"));
			placeRepository.save(new Place("Test Two", "Test", "Test Two", "Two"));
		}
		
	}
	
	@Test
	public void testPlacesListing() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/places", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void testfilterByNameExisting() throws Exception {
		String url = "/places?name=Test Two";

		this.mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testPlaceSearchByIdReturn200() {
		ResponseEntity<Place> response = restTemplate.getForEntity("/places/2", Place.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo("Test Two");
	}
	
	@Test
	public void testPlaceSearchByIdReturn404() {
		ResponseEntity<Place> response = restTemplate.getForEntity("/places/20", Place.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void registerNewPlaceWithReturn400() {
		Place place = new Place("City 1", null, "City 1", "One");
		ResponseEntity<String> response = restTemplate.postForEntity("/places", place, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("field", "slug");
	}
	
	@Test
	public void registerNewPlaceWithReturn201() {
		Place place = new Place("City 1", "City", "City 1", "One");
		ResponseEntity<String> response = restTemplate.postForEntity("/places", place, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().get("Location")).contains("http://localhost:" + this.port + "/places/3");
	}
	
	@Test
	public void updatePlaceWithReturn400() {
		Place place = new Place("City 5", null, "City 5", "One");
		restTemplate.put("/places/1", place);
		
		ResponseEntity<Place> response = restTemplate.getForEntity("/places/1", Place.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isNotEqualTo("City 5");
	}
	
	@Test
	public void updatePlaceWithReturn201() {
		Place place = new Place("City 1", "City", "City 1", "One");
		restTemplate.put("/places/1", place);
			
		ResponseEntity<Place> response = restTemplate.getForEntity("/places/1", Place.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo("City 1");
	}
	
	@Test
	public void updatePlaceWithReturn404() {
		Place place = new Place("City 404", "City", "City 404", "One");
		restTemplate.put("/places/10", place);
			
		ResponseEntity<Place> response = restTemplate.getForEntity("/places/10", Place.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}
	
}
