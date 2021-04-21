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
   public UUID Country1Id;
   public UUID Country2Id;
   public double TransmissionRate;
   public boolean IsOpen;
   public boolean IsSelected;
   
   public final UUID Id;
    
    public LinkDTO(Link link) {
        LinkType = link.GetLinkType();
        Country1Id = link.getCountry1Id();
        Country2Id = link.getCountry2Id();
        TransmissionRate = link.getTransmissionRate();
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
               ((Country1Id == link.Country1Id &&
               Country2Id == link.Country2Id) || 
               (Country1Id == link.Country2Id &&
                Country2Id == link.Country1Id)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.LinkType);
        hash = 97 * hash + Objects.hashCode(this.Country1Id);
        hash = 97 * hash + Objects.hashCode(this.Country2Id);
        hash = 97 * hash + Objects.hashCode(this.Id);
        return hash;
    }
}
