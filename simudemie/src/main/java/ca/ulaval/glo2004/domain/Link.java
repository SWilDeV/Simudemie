/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Sean
 */

public class Link {
//    Attributs
   public enum LinkType { TERRESTRE, MARITIME, AERIEN }; 
    
   private final LinkType linkType;
   private Country country1;
   private Country country2;
   private double travelRate;
   private boolean isOpen;
   private boolean isSelected;
   private String linkName;
   
   private final UUID id;
   
   private static final Color landColor = Color.pink;
   private static final Color airColor = Color.GREEN;
   private static final Color maritimeColor = Color.BLUE;
   
//   methodes
   public Link(Country first, Country second, LinkType type){
       this.setCountry1(first);
       this.setCountry2(second);
       linkType = type;
       id = UUID.randomUUID();
   }
   
   public UUID GetId() {
       return id;
   }
   
   public boolean IsSelected() {
       return isSelected;
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
   
   public void SetSelectionState(boolean select) {
       isSelected = select;
   }
   
   public LinkType GetLinkType() {
       return linkType;
   }
   
   public static Color GetColor(LinkType type) {
       if(type == LinkType.TERRESTRE) {
           return landColor;
       } else if(type == LinkType.MARITIME) {
           return maritimeColor;
       }
       
       return airColor;
   }
   
   public static int GetDrawOffset(LinkType type) {
       if(type == LinkType.MARITIME) {
           return 20;
       }
       
       return 0;
   }
   
   public String getlinkName() {
       String firstPart = "";
       
       if (linkType == linkType.TERRESTRE) {
           firstPart = "T - ";
       } else if (linkType == linkType.AERIEN) {
           firstPart = "A - ";
       } else {
           firstPart = "M - ";
       }
       
       this.linkName = firstPart + country1.getName() + " - " + country2.getName();
       
       
       return this.linkName;
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
        return id == link.GetId() ||
                (linkType.equals(link.GetLinkType()) &&
               ((country1 == link.getCountry1() &&
               country2 == link.getCountry2()) || 
               (country1 == link.getCountry2() &&
                country2 == link.getCountry1())));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.linkType);
        hash = 31 * hash + Objects.hashCode(this.country1);
        hash = 31 * hash + Objects.hashCode(this.country2);
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
