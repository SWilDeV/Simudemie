/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Sean
 */

public class Link implements Serializable, Cloneable {
//    Attributs
   public enum LinkType { TERRESTRE, MARITIME, AERIEN }; 
    
   private final LinkType linkType;
   private UUID country1Id;
   private UUID country2Id;
   private double transmissionRate;
   private boolean isOpen = true;
   private boolean isSelected;
 
   private UUID id;
   private static final long serialVersionUID = 7L; 
   private static final Color landColor = Color.pink;
   private static final Color airColor = new Color(191, 0, 179);
   private static final Color maritimeColor = Color.BLUE;
   
//   methodes
   public Link(UUID firstId, UUID secondId, LinkType type, double p_transmissionRate){
       this.setCountry1Id(firstId);
       this.setCountry2Id(secondId);
       linkType = type;
       id = UUID.randomUUID();
       transmissionRate = p_transmissionRate;
   }
   
   public UUID GetId() {
       return id;
   }
   
   public boolean IsSelected() {
       return isSelected;
   }
   
   public UUID getCountry1Id(){
       return country1Id;
   }
   public UUID getCountry2Id(){
       return country2Id;
   }
   
   public void setCountry1Id(UUID id){
       country1Id = id;
   }
   
   public void setCountry2Id(UUID id){
       country2Id = id;
   }
   
   public double getTransmissionRate(){
       return transmissionRate;
   }
   
   public void setTransmissionRate(double p_transmissionRate){
      transmissionRate = p_transmissionRate;
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
   
   public void closeLink() {
       isOpen = false;
   }
   
   public void openLink() {
       isOpen = true;
   }
   
   public static int GetDrawOffset(LinkType type) {
       if(type == LinkType.MARITIME) {
           return 20;
       }
       
       return 0;
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
               ((country1Id == link.getCountry1Id() &&
               country2Id == link.getCountry2Id()) || 
               (country1Id == link.getCountry2Id() &&
                country2Id == link.getCountry1Id())));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.linkType);
        hash = 31 * hash + Objects.hashCode(this.country1Id);
        hash = 31 * hash + Objects.hashCode(this.country2Id);
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public Link clone() throws CloneNotSupportedException {
        Link link = null;
        try {
            link = (Link) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        link.country1Id = country1Id;
        link.country2Id = country2Id;
        link.id = id;
        
        return link;
    }
}
