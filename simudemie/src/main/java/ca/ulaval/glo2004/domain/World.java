/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    
    public void addRegion(UUID countryId, Region region) {
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            country.addRegion(region);
        }
    }
    
    public void addlink(UUID firstCountryId, UUID secondCountryId, Link.LinkType type) {
        //TODO: Aranger le tout!
        //Link link = new Link(FindCountryByUUID(firstCountryId), FindCountryByUUID(secondCountryId), type);
        boolean alreadyLink = false; //En fonction de la reponse de reponse du teams.
        for(Link link: linkList) {
            UUID countryId1 = link.getCountry1().GetId();
            UUID countryId2 = link.getCountry2().GetId();
            if((countryId1 == firstCountryId && countryId2 == secondCountryId) || (countryId1 == secondCountryId && countryId2 == firstCountryId)) {
                alreadyLink = true;
                break;
            }
        }
        
        if(!alreadyLink) {
            Link link = new Link(FindCountryByUUID(firstCountryId), FindCountryByUUID(secondCountryId), type);
            linkList.add(link); //TODO: Verifier si le pays est deja lier.
            System.out.println("Pays lier: " + link.getCountry1().GetId() + " | " + link.getCountry2().GetId());
        }
    }
    
    public Country findCountryByPosition(int x, int y){
        throw new UnsupportedOperationException("Not supported");
    }
    
    private Country FindCountryByUUID(UUID id) {
        for(Country country: countryList) {
            if(country.GetId() == id) {
                return country;
            }
        }
        
        return null; //Cree une exception ici.
    }
    
    public void removeCountry(UUID countryId){
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            countryList.remove(country);
        }
    }
    
    public List getCountries(){
        return new ArrayList<>(countryList); //Peut etre un retour comme celui-ci dans le controller.
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
