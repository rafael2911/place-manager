package br.com.crcarvalho.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.crcarvalho.manager.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
