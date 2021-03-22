/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author Sean
 */

public class Link {
//    Attributs
   public enum LinkType { AERIEN, TERRESTRE, MARITIME }   
    
   private final LinkType linkType;
   private Country country1;
   private Country country2;
   private double travelRate;
   private boolean isOpen;
   
   private static final Color landColor = Color.BLUE;
   private static final Color airColor = Color.pink;
   private static final Color maritimeColor = Color.GREEN;
   
//   methodes
   public Link(Country first, Country second, LinkType type){
       this.setCountry1(first);
       this.setCountry2(second);
       linkType = type;
   }
   
   public Country getCountry1(){
       return country1;
   }
   public Country getCountry2(){
       return country2;
   }
   
   public void setCountry1(Country p_country1){
       country1 = p_country1;
   }
   
   public void setCountry2(Country p_country2){
       country2 = p_country2;
   }
   
   public double getTravelRate(){
       return travelRate;
   }
   
   public void setTravelRate(double p_travelRate){
      travelRate = p_travelRate;
   }
   
   public boolean isOpen(){
       return isOpen;
   }
   
   public void setOpen(boolean open){
       isOpen = open;
   }
   
   public LinkType GetLinkType() {
       return linkType;
   }
   
   public Color GetColor() {
       if(linkType == LinkType.TERRESTRE) {
           return landColor;
       } else if(linkType == LinkType.MARITIME) {
           return maritimeColor;
       }
       
       return airColor;
   }
   
    @Override
    public boolean equals(Object other) {       
        if(other == null || !(other instanceof Link)){
            return false;
        }
        
        if(other == this) {
            return true;
        }
        
        Link link = (Link)other;
        return linkType == link.GetLinkType() &&
               country1 == link.getCountry1() &&
               country2 == link.getCountry2();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.linkType);
        hash = 79 * hash + Objects.hashCode(this.country1);
        hash = 79 * hash + Objects.hashCode(this.country2);
        return hash;
    }
}
