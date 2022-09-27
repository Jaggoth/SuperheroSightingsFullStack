package com.tudor.superherosightings.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tudor.superherosightings.dao.LocationDAO;
import com.tudor.superherosightings.dao.OrganisationDAO;
import com.tudor.superherosightings.dao.SightingDAO;
import com.tudor.superherosightings.dao.SuperheroDAO;
import com.tudor.superherosightings.dto.Location;

@Controller
public class LocationController {
    @Autowired
    SuperheroDAO superheroDAO;

    @Autowired
    OrganisationDAO organisationDAO;

    @Autowired
    LocationDAO locationDAO;

    @Autowired
    SightingDAO sightingDAO;

    @PostMapping("addLocation")
    public String addLocation(String name, String address, String description, int latitude, int longitude) {
        Location location = new Location();
        location.setName(name);
        location.setAddress(address);
        location.setDescription(description);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        locationDAO.addLocation(location);

        return "redirect:/locations";
    }
    
    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDAO.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location location = locationDAO.getLocationById(id);
        model.addAttribute("location", location);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, BindingResult result) {
        if(result.hasErrors()) {
            return "editLocation";
        }
        locationDAO.updateLocation(location);
        return "redirect:/locations";
    }
    
    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) {
    	locationDAO.deleteLocationById(id);
        return "redirect:/locations";
    }
}
