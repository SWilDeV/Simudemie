/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 *
 * @author charl
 */
public class World {
    
    private List<Link> linkList = new ArrayList<Link>();
    private List<Country> countryList = new ArrayList<Country>();
    
    public void addCountry(Country country){
         countryList.add(country);
    }
    
    public void updateCountry(CountryDTO country) {
        Country c = FindCountryByUUID(country.Id);
        if(c != null){
            c.fromDTO(country);
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
    
    public void Addlink(UUID firstCountryId, UUID secondCountryId, Link.LinkType type) {
        Link link = new Link(FindCountryByUUID(firstCountryId), FindCountryByUUID(secondCountryId), type);       
        boolean alreadyLink = linkList.stream().anyMatch(e -> e.equals(link));

        if(!alreadyLink) {
            linkList.add(link);
        }
    }
    
    public void RemoveLink() {
        
    }
    
    public Country findCountryByPosition(Point position) {
        for(Country country: countryList) {                
            if (Utility.IsInRectangle(country.getShape().GetPoints(), position)) {
                return country; //TODO: DTO dans le controller.
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
       
    public List getCountries(){
        return countryList; //Peut etre un retour comme celui-ci dans le controller.
    }
    
    public List getLinks(){
        return linkList; //Peut etre un retour comme celui-ci dans le controller.
    }
    
    public void getInfos(UUID countryId){
        //Return le pays en DTO ?
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            //Do something...
        }
    }
}
