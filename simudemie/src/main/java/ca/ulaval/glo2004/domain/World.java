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
    
    //TODO:
    private List<Link> linkList = new ArrayList<Link>();
    private List<Country> countryList = new ArrayList<Country>();
    
    public void addCountry(Country country){
         countryList.add(country);
    }
    
    public void addlink(){
    }
    
    public Country findCountryByPosition(int x, int y){
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void removeCountry(){
        
    }
    
    public List getCountries(){
        return countryList;
    }
    
    public List getLinks(){
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void getInfos(Country country){
        
    }
}
