package com.leonardo.data;

import java.util.List;
import java.util.Random;

import com.leonardo.model.City;
import org.springframework.stereotype.Service;

@Service
public class CityDAO {

	private final CityDatabase cityDatabase;

	public CityDAO() {
		this.cityDatabase = CityDatabase.getInstance();
	}

	public City findById(Integer id) {
		for(City city : cityDatabase.getCityList()) {
			if(city.getId().equals(id))
				return city;
		}

		return null;
	}

	public void deleteById(Integer id) {
		City city = this.findById(id);
		cityDatabase.getCityList().remove(city);
	}

	public List<City> listAll() {
		return cityDatabase.getCityList();
	}

	public Integer create(City city) {
		Random random = new Random();
		Integer id = java.lang.Math.abs(random.nextInt());

		city.setId(id);
		cityDatabase.getCityList().add(city);

		return id;
	}

	public void update(City updatedCity) {
		City city = this.findById(updatedCity.getId());

		if(city == null) {
			return;
		}

		city.setName(updatedCity.getName());
		city.setLatitude(updatedCity.getLatitude());
		city.setLongitude(updatedCity.getLongitude());
	}
}
