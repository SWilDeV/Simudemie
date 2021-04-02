/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Abergel Clement
 */
public class LinkDTO {
    
   public Link.LinkType LinkType;
   public CountryDTO Country1;
   public CountryDTO Country2;
   public double TravelRate;
   public boolean IsOpen;
   public boolean IsSelected;
   
   public final UUID Id;
    
    public LinkDTO(Link link) {
        LinkType = link.GetLinkType();
        Country1 = new CountryDTO(link.getCountry1());
        Country2 = new CountryDTO(link.getCountry2());
        TravelRate = link.getTravelRate();
        IsOpen = link.isOpen();
        IsSelected = link.IsSelected();
        Id = link.GetId();
    }
    
    
    @Override
    public boolean equals(Object other) {       
        if(other == null || !(other instanceof LinkDTO)){
            return false;
        }
        
        if(other == this) {
            return true;
        }
        
        LinkDTO link = (LinkDTO)other;
        return Id == link.Id||
                (LinkType.equals(link.LinkType) &&
               ((Country1 == link.Country1 &&
               Country2 == link.Country2) || 
               (Country1 == link.Country2 &&
                Country2 == link.Country1)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.LinkType);
        hash = 97 * hash + Objects.hashCode(this.Country1);
        hash = 97 * hash + Objects.hashCode(this.Country2);
        hash = 97 * hash + Objects.hashCode(this.Id);
        return hash;
    }
}
