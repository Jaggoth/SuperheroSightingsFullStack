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
import com.tudor.superherosightings.dto.Superhero;

@Controller
public class SuperheroController {

    @Autowired
    SuperheroDAO superheroDAO;

    @Autowired
    OrganisationDAO organisationDAO;

    @Autowired
    LocationDAO locationDAO;

    @Autowired
    SightingDAO sightingDAO;
    
    @PostMapping("addSuperhero")
    public String addSuperhero(String name, String power, String description) {
        Superhero superhero = new Superhero();
        superhero.setName(name);
        superhero.setPower(power);
        superhero.setDescription(description);
        superheroDAO.addHero(superhero);

        return "redirect:/superheroes";
    }
    
    @GetMapping("superheroes")
    public String displaySuperheroes(Model model) {
        List<Superhero> heroes = superheroDAO.getAllHeroes();
        model.addAttribute("superheroes", heroes);
        return "superheroes";
    }

    @GetMapping("editSuperhero")
    public String editSuperhero(Integer id, Model model) {
        Superhero superhero = superheroDAO.getHeroById(id);
        model.addAttribute("superhero", superhero);
        return "editSuperhero";
    }

    @PostMapping("editSuperhero")
    public String performEditSuperhero(@Valid Superhero superhero, BindingResult result) {
        if(result.hasErrors()) {
            return "editSuperhero";
        }
        superheroDAO.updateHero(superhero);
        return "redirect:/superheroes";
    }
    
    @GetMapping("deleteSuperhero")
    public String deleteSuperhero(Integer id) {
    	superheroDAO.deleteHeroById(id);
        return "redirect:/superheroes";
    }
}
