package com.assignment.spring.repositories;

import com.assignment.spring.entities.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Integer> {

  WeatherEntity findByCity(String city);
}
