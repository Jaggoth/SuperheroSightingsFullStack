package com.tudor.superherosightings.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tudor.superherosightings.dao.LocationDAO;
import com.tudor.superherosightings.dao.OrganisationDAO;
import com.tudor.superherosightings.dao.SightingDAO;
import com.tudor.superherosightings.dao.SuperheroDAO;
import com.tudor.superherosightings.dto.Location;
import com.tudor.superherosightings.dto.Sighting;
import com.tudor.superherosightings.dto.Superhero;

@Controller
public class SightingController {
    @Autowired
    SuperheroDAO superheroDAO;

    @Autowired
    OrganisationDAO organisationDAO;

    @Autowired
    LocationDAO locationDAO;

    @Autowired
    SightingDAO sightingDAO;

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        String superheroId = request.getParameter("superId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");

        sighting.setSuperhero(superheroDAO.getHeroById(Integer.parseInt(superheroId)));
        sighting.setLocation(locationDAO.getLocationById(Integer.parseInt(locationId)));
        sighting.setDate(LocalDate.parse(date));
        sightingDAO.addSighting(sighting);

        return "redirect:/sightings";
    }
    
    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDAO.getAllSightings();
        List<Superhero> superheroes = superheroDAO.getAllHeroes();
        List<Location> locations = locationDAO.getAllLocations();
        model.addAttribute("sightings", sightings);
        model.addAttribute("superheroes", superheroes);
        model.addAttribute("locations", locations);
        return "sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sighting = sightingDAO.getSightingById(id);
        List<Superhero> superheroes = superheroDAO.getAllHeroes();
        List<Location> locations = locationDAO.getAllLocations();
        model.addAttribute("sighting", sighting);
        model.addAttribute("superheroes", superheroes);
        model.addAttribute("locations", locations);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(@Valid Sighting sighting, BindingResult result, HttpServletRequest request, Model model) {
        String superheroId = request.getParameter("superId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");

        if(superheroId == null) {
            FieldError error = new FieldError("sighting", "superhero", "Must include a superhero");
            result.addError(error);
        }
        if(locationId == null){
            FieldError error = new FieldError("sighting", "location", "Must include a location");
            result.addError(error);
        }
        if(date == null){
            FieldError error = new FieldError("sighting", "date", "Must include a date");
            result.addError(error);
        }

        sighting.setSuperhero(superheroDAO.getHeroById(Integer.parseInt(superheroId)));
        sighting.setLocation(locationDAO.getLocationById(Integer.parseInt(locationId)));
        sighting.setDate(LocalDate.parse(date));
        sightingDAO.updateSighting(sighting);


        if(result.hasErrors()) {
            model.addAttribute("sighting", sighting);
            return "editSighting";
        }

        return "redirect:/sightings";
    }
    
    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        sightingDAO.deleteSightingById(id);
        return "redirect:/sightings";
    }
}
