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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class World implements java.io.Serializable {
    
    private List<Link> linkList = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();
    
    public World(){   
    }
    
    public void addCountry(Country country){
         countryList.add(country);
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
            c.fromCountry(country);
        }
    }
    
    public void removeCountry(UUID countryId){
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
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
    }
    
    public void RemoveLink(UUID linkId) {
        Link link = FindLinkByUUID(linkId);
        if(link != null) {
            linkList.remove(link);
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
        for(Country country: countryList) {
            if(country.GetId() == id) {
                return country;
            }
        }
        
        return null;
    }
    
    private Link FindLinkByUUID(UUID id) {
        for(Link link: linkList) {
            if(link.GetId() == id) {
                return link;
            }
        }
        
        return null;
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
    
    public void clearWorld() {
        countryList.clear();
        linkList.clear();
    }
}
