package com.leonardo;

import com.leonardo.model.City;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;


import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { JavaTestApplication.class }, webEnvironment
		= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JavaTestApplicationTests {

	private static final String API_ROOT
			= "http://localhost:8081/city";

	private City createRandomCity() {
		City city = new City();
		city.setId(16);
		city.setName("Mumbai");
		city.setLongitude(72.8777);
		city.setLatitude(19.0760);
		return city;
	}

	private String createCityAsUri(City city) {
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(city)
				.post(API_ROOT);
		return API_ROOT + "/" + response.jsonPath().get("id");
	}

	@Test
	public void whenGetAllCities_thenOK() {
		Response response = RestAssured.get(API_ROOT);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	public void whenGetNotExistCityById_thenNotFound() {
		Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}

	//Test creating a new City
	@Test
	public void whenCreateNewCity_thenCreated() {
		City city = createRandomCity();
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(city)
				.post(API_ROOT);

		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	}

	@Test
	public void whenInvalidVityk_thenError() {
		City city = createRandomCity();
		city.setName(null);
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(city)
				.post(API_ROOT);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}

	//Updating An existing City

	@Test
	public void whenUpdateCreatedCity_thenUpdated() {
		City city = createRandomCity();
		String location = createCityAsUri(city);
		city.setId(Integer.parseInt(location.split("/city/")[1]));
		city.setName("Pune");
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(city)
				.put(location);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = RestAssured.get(location);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals("Pune", response.jsonPath()
				.get("name"));
	}

	//Delete a City
	@Test
	public void whenDeleteCreatedCity_thenOk() {
		City city = createRandomCity();
		String location = createCityAsUri(city);
		Response response = RestAssured.delete(location);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = RestAssured.get(location);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}
}
