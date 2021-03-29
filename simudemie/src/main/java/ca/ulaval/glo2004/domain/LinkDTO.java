/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

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
   
   public final UUID Id;
    
    public LinkDTO(Link link) {
        LinkType = link.GetLinkType();
        Country1 = new CountryDTO(link.getCountry1());
        Country2 = new CountryDTO(link.getCountry2());
        TravelRate = link.getTravelRate();
        IsOpen = link.isOpen();
        Id = link.GetId();
    }
}
