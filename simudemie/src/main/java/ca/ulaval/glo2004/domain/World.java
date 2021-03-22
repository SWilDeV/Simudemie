/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.util.ArrayList;
import java.util.List;

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
    
    public void addRegion(Country country, Region region) {
        if(countryList.contains(country)) {
            int index = countryList.indexOf(country);
            countryList.get(index).addRegion(region);
        }
    }
    
    public void addlink(Link link) {
        linkList.add(link); //TODO: Verifier si le pays est deja lier.
    }
    
    public Country findCountryByPosition(int x, int y){
        throw new UnsupportedOperationException("Not supported");
    }
    
//    public Country FindCountryByUUID(UUID id) {
//        for(Country country: countryList) {
//            if(country.GetId() == id) {
//                return country; //DTO du pays
//            }
//        }
//        
//        return null; //Cree une exception ici.
//    }
    
    public void removeCountry(Country country){
        countryList.remove(country);
    }
    
    public List getCountries(){
        return new ArrayList<>(countryList);
    }
    
    public List getLinks(){
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void getInfos(Country country){
        //Return le pays en DTO ?
    }
}
