package com.assignment.spring.services;

import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.models.CityWeatherModel;
import com.assignment.spring.repositories.WeatherRepository;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

  @Autowired
  RestTemplate weatherRestTemplate;

  @Value("${weatherToken}")
  private String weatherToken;
  @Autowired
  WeatherRepository weatherRepository;
  @Value("${weatherAPI}")
  private String weatherAPI;

  @Transactional
  @Cacheable(value = "cities", key = "#city.toLowerCase()")
  public WeatherEntity getCity(String city) {
    log.info("Searching for city: {}", city);
    city = city.toLowerCase();
    CityWeatherModel weatherModel = weatherRestTemplate.getForObject(
        weatherAPI,
        CityWeatherModel.class, city, weatherToken);
    WeatherEntity cityEntity = WeatherEntity.builder()
        .city(city)
        .country(weatherModel.getSys().getCountry())
        .temperature(weatherModel.getMain().getTemp()).build();
    WeatherEntity repoEntity = weatherRepository.findByCity(city);
    if(Objects.nonNull(repoEntity)){
      repoEntity.setTemperature(weatherModel.getMain().getTemp());
      weatherRepository.save(repoEntity);
    }else {
      weatherRepository.save(cityEntity);
    }

    return cityEntity;
  }
}
