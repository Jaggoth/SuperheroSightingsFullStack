package com.tudor.superherosightings.controller;

import java.util.ArrayList;
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
import com.tudor.superherosightings.dto.Organisation;
import com.tudor.superherosightings.dto.Superhero;

@Controller
public class OrganisationController {
    @Autowired
    SuperheroDAO superheroDAO;

    @Autowired
    OrganisationDAO organisationDAO;

    @Autowired
    LocationDAO locationDAO;

    @Autowired
    SightingDAO sightingDAO;

    @PostMapping("addOrganisation")
    public String addOrganisation(Organisation organisation, HttpServletRequest request) {
        String[] superheroIds = request.getParameterValues("superId");

        List<Superhero> superheroes = new ArrayList<>();
        for(String superheroId : superheroIds) {
            superheroes.add(superheroDAO.getHeroById(Integer.parseInt(superheroId)));
        }
        organisation.setMembers(superheroes);
        organisationDAO.addOrg(organisation);

        return "redirect:/organisations";
    }
    
    @GetMapping("organisations")
    public String displayOrganisations(Model model) {
        List<Organisation> organisations = organisationDAO.getAllOrgs();
        List<Superhero> superheroes = superheroDAO.getAllHeroes();
        model.addAttribute("organisations", organisations);
        model.addAttribute("superheroes", superheroes);
        return "organisations";
    }

    @GetMapping("organisationDetail")
    public String organisationDetail(Integer id, Model model) {
        Organisation organisation = organisationDAO.getOrgById(id);
        model.addAttribute("organisation", organisation);
        return "organisationDetail";
    }

    @GetMapping("editOrganisation")
    public String editOrganisation(Integer id, Model model) {
    	Organisation organisation = organisationDAO.getOrgById(id);
        List<Superhero> superheroes = superheroDAO.getAllHeroes();
        model.addAttribute("organisation", organisation);
        model.addAttribute("superheroes", superheroes);
        return "editOrganisation";
    }

    @PostMapping("editOrganisation")
    public String performEditOrganisation(@Valid Organisation organisation, BindingResult result, HttpServletRequest request, Model model) {
        String[] superheroIds = request.getParameterValues("superId");

        List<Superhero> superheroes = new ArrayList<>();
        if(superheroIds != null) {
            for(String superheroId : superheroIds) {
                superheroes.add(superheroDAO.getHeroById(Integer.parseInt(superheroId)));
            }
        } else {
            FieldError error = new FieldError("organisation", "superheroes", "Must include one superhero");
            result.addError(error);
        }

        organisation.setMembers(superheroes);

        if(result.hasErrors()) {
            model.addAttribute("superheroes", superheroDAO.getAllHeroes());
            model.addAttribute("organisation", organisation);
            return "editOrganisation";
        }

        organisationDAO.updateOrg(organisation);

        return "redirect:/organisations";
    }
    
    @GetMapping("deleteOrganisation")
    public String deleteOrganisation(Integer id) {
        organisationDAO.deleteOrgById(id);
        return "redirect:/organisations";
    }

}
