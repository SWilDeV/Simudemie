/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class World implements Serializable {
    
    private transient WorldController worldController;
    private List<Link> linkList = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();
    private Population worldPopulation = new Population();
    private static final long serialVersionUID = 1L; 
    
    public World(){
    }

    public World(WorldController worldController) {
        this.worldController = worldController;
    }
    
    public void SetWorldController(WorldController worldController) {
        this.worldController = worldController;
    }
    
    public void addCountry(Country country){
         countryList.add(country);
         worldController.NotifyCountryCreated(new CountryDTO(country));
    }

    public void addLink(Link link) {
        linkList.add(link);
    }
    
    public void Addlink(UUID firstCountryId, UUID secondCountryId, Link.LinkType type) {
        Link link = new Link(FindCountryByUUID(firstCountryId), FindCountryByUUID(secondCountryId), type);       
        boolean isAbleToLink = !linkList.stream().anyMatch(e -> e.equals(link));
 
        if(type == Link.LinkType.TERRESTRE) {
            Country country1 = FindCountryByUUID(firstCountryId);
            Country country2 = FindCountryByUUID(secondCountryId);
            if(country1 != null && country2 != null) {
                isAbleToLink = Utility.AsCommonLandBorder(country1, country2);
            } else {
                isAbleToLink = false;
            }
        }
        
        if(isAbleToLink) {
            linkList.add(link);
        }
        
        worldController.NotifyLinksUpdated();
    }
    
    public void addRegion(UUID countryId, String name, double popPercentage) {
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            country.addRegion(name, popPercentage);
        }
    }
    
    public void clearWorld() {
        countryList.clear();
        linkList.clear();
    }
    
    public Country findCountryByPosition(Point position) {
        for(Country country: countryList) {                
            if (Utility.IsInRectangle(country.getShape().GetPoints(), position)) {
                return country;
            }
        }
        return null;
    }
    
    private Country FindCountryByUUID(UUID id) {
        try {
            return countryList.stream().filter(c -> c.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    private Link FindLinkByUUID(UUID id) {
        try {
            return linkList.stream().filter(l -> l.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public List getCountries(){
        return countryList;
    }
    
    public List getLinks(){
        return linkList;
    }
    
    public void getInfos(UUID countryId) {
        //Return le pays en DTO ?
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            //Do something...
        }
    }
    
    public Population getWorldPopulation(){
        return worldPopulation;
    }
    
    public void RemoveAllBorders(Country country) {
        List<Link> links = linkList.stream().filter(l -> l.getCountry1().GetId().equals(country.GetId()) ||
                                                    l.getCountry2().GetId().equals(country.GetId())).collect(Collectors.toList());
        
        for(int i = 0; i < links.size(); i++) {
            linkList.remove(links.get(i));
        }
        
        worldController.NotifyLinksUpdated();
    }
    
    public void removeCountry(UUID countryId){
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            RemoveAllBorders(country);
            countryList.remove(country);
        }
    }
    
    public void RemoveLink(UUID linkId) {
        Link link = FindLinkByUUID(linkId);
        if(link != null) {
            linkList.remove(link);
            worldController.NotifyLinksUpdated();
        }
    }
    
    public void UpdateSelectionStateRegion(UUID countryId, UUID regionId, boolean select) {
       Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.UpdateSelectionStateRegion(regionId, select);
        } 
    }
    
    public void UpdateRegion(UUID countryId, RegionDTO region) {
        Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.UpdateRegion(region);
        }
    }
    
    public void ValidateRegions() {
        for(Country c: countryList) {
            c.ValidateRegions();
        }
    }
    
    public void RemoveRegion(UUID countryId, UUID regionId) {
        Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.removeRegion(regionId);
        }
    }
    
    public void UpdateSelectionStateCountry(UUID id, boolean select) {
        Country country = FindCountryByUUID(id);
        if(country != null) {
            country.SetSelectionState(select);
        }
    }
    

    public void updateCountry(CountryDTO country) {
        Country c = FindCountryByUUID(country.Id);
        if(c != null){
            c.fromCountryDTO(country);
            UpdateLandBorder(country);
            c.setName(country.Name);
            c.setPopulation(new Population(country.populationDTO.totalPopulationDTO));
        }
    }
    
    public void updateCountryFromSimulation(Country country) {
        Country c = FindCountryByUUID(country.GetId());
        if(c != null){
            c.updateCountryPopulation(country);
        }
    }
    
    public void updateRegionFromSimulation(Country country,Region region) {
        Country c = FindCountryByUUID(country.GetId());
        if(c != null){
            Region r =c.FindRegionByUUID(region.GetId());
            if(r != null){
                r.updateRegion(region);
            }
        }
    }
    
    public void UpdateSelectionStateLink(UUID linkId, boolean select) {
        Link l = FindLinkByUUID(linkId);
        if(l != null) {
            l.SetSelectionState(select);
        }
    }
    
    public void UpdateLandBorder(CountryDTO country) {
        List<Link> links = linkList.stream().filter(l -> l.GetLinkType().equals(LinkType.TERRESTRE) &&
                                                   (l.getCountry1().GetId() == country.Id ||
                                                    l.getCountry2().GetId() == country.Id)).collect(Collectors.toList());
        
        if(links != null) {
            for(int i = 0; i < links.size(); i++) {
                if(!Utility.AsCommonLandBorder(links.get(i).getCountry1(), links.get(i).getCountry2())) {
                    linkList.remove(links.get(i));
                }
            }
        }
        worldController.NotifyLinksUpdated();
    }
    
    public void updateWorldPopulation(){
        int totalPop = 0;
        int infectedPop = 0;
        int deadPop = 0;
        int nonInfectedPop = 0;
        for(Country country: countryList){
            totalPop += country.getPopulation().getTotalPopulation();
            infectedPop += country.getPopulation().getInfectedPopulation();
            deadPop += country.getPopulation().getDeadPopulation();
            nonInfectedPop += country.getPopulation().getNonInfectedPopulation();
        }
        worldPopulation.setInfectedPopulation(infectedPop);
        worldPopulation.setNonInfectedPopulation(nonInfectedPop);
        worldPopulation.setDeadPopulation(deadPop);
        worldPopulation.setTotalPopulation(totalPop);
    }
}
