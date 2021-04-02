/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class World implements java.io.Serializable {
    
    private WorldController worldController;
    private List<Link> linkList = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();
    
    public World(WorldController worldController) {
        this.worldController = worldController;
    }
    
    public void addCountry(Country country){
         countryList.add(country);
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
        }
        
    }
    
    public void updateCountryFromSimulation(Country country) {
        Country c = FindCountryByUUID(country.GetId());
        if(c != null){
            c.fromCountry(country);
        }
    }
    
    public void removeCountry(UUID countryId){
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            RemoveAllBorders(country);
            countryList.remove(country);
        }
    }
    
    public void addRegion(UUID countryId, Region region) {
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            country.addRegion(region);
        }
    }
    
    public void updateRegion() {
    }
    
    public void RemoveRegion() {
        
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
    
    public void UpdateSelectionStateLink(UUID linkId, boolean select) {
        Link l = FindLinkByUUID(linkId);
        if(l != null) {
            l.SetSelectionState(select);
        }
    }
    
    public void RemoveAllBorders(Country country) {
        List<Link> links = linkList.stream().filter(l -> l.getCountry1().GetId().equals(country.GetId()) ||
                                                    l.getCountry2().GetId().equals(country.GetId())).collect(Collectors.toList());
        
        for(int i = 0; i < links.size(); i++) {
            linkList.remove(links.get(i));
        }
        
        worldController.NotifyLinksUpdated();
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
    
    public void RemoveLink(UUID linkId) {
        Link link = FindLinkByUUID(linkId);
        if(link != null) {
            linkList.remove(link);
            worldController.NotifyLinksUpdated();
        }
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
        return countryList; //Peut etre un retour comme celui-ci dans le controller.
    }
    
    public List getLinks(){
        return new ArrayList<>(linkList); //Peut etre un retour comme celui-ci dans le controller.
    }
    
    public void getInfos(UUID countryId){
        //Return le pays en DTO ?
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            //Do something...
        }
    }
}
