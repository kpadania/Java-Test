package com.leonardo.controller;

import com.leonardo.data.CityDAO;
import com.leonardo.exception.ResourceNotFoundException;
import com.leonardo.model.City;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/city")
@Api(value = "City Management System")
public class CityController {

    @Autowired
    private CityDAO cityDao;

    public static final int EARTH_RADIUS = 6373;

    @ApiOperation(value = "View a list of available cities", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<City>> getAllCity(){
        List<City> allCities = null;
        allCities = cityDao.listAll();

        return new ResponseEntity<>(allCities, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Add a city")
    public void createCity(@RequestBody City city) {
        if(!Objects.equals(null,city)){
            cityDao.create(city);
        }

    }
    @ApiOperation(value = "Get a city by Id")
    @GetMapping("/{id}")
    public ResponseEntity<City> getById(@PathVariable(required = true) Integer id) throws ResourceNotFoundException {

        City city;
        city = cityDao.findById(id);

        if(Objects.equals(null, city)){
            throw new ResourceNotFoundException("City not found for this id :" + id);
        }


        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a city")
    @PutMapping("{id}")
    public ResponseEntity <City> updateCity(
            @ApiParam(value = "City Id to update city object", required = true) @PathVariable(value = "id") Integer id,
            @ApiParam(value = "Update city object", required = true) @Valid @RequestBody City cityDetails) throws ResourceNotFoundException {


        City city = cityDao.findById(id);
        if(Objects.equals(null, city)){
            throw new ResourceNotFoundException("City not found for this id :" + id);
        }
        city.setName(cityDetails.getName());
        city.setLatitude(cityDetails.getLatitude());
        city.setLongitude(cityDetails.getLongitude());
        cityDao.update(city);
        return ResponseEntity.ok(city);
    }

    @ApiOperation(value = "Delete a city")
    @DeleteMapping("/{id}")
    public ResponseEntity<City> delete(@PathVariable(required = true) Integer id) throws ResourceNotFoundException {
        City city = cityDao.findById(id);
        if(Objects.equals(null, city)){
            throw new ResourceNotFoundException("City not found for this id :" + id);
        } else {
            cityDao.deleteById(id);
            return new ResponseEntity<>(city, HttpStatus.OK);
        }
    }

    @GetMapping("/findDistance/{id1}/{id2}")
    public Double getDistance(@PathVariable(required=true) Integer id1, @PathVariable(required=true) Integer id2) {

        ResponseEntity<City> city1 = null;
        try {
            city1 = getById(id1);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        ResponseEntity<City> city2 = null;
        try {
            city2 = getById(id2);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        double latDistance = city2.getBody().getLongitude() - city1.getBody().getLongitude();
        double lonDistance = city2.getBody().getLatitude() - city1.getBody().getLatitude();
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(city1.getBody().getLatitude()) * Math.cos(city2.getBody().getLatitude())
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance;
    }


}
