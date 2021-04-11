/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Sean
 */
public class RegionLink implements Serializable, Cloneable {
   private UUID region1Id;
   private UUID region2Id;
   private double travelRate;
   private boolean isOpen;
   private UUID id;
   
   //   methodes
   public RegionLink(UUID firstId, UUID secondId){
       this.setRegion1Id(firstId);
       this.setRegion2Id(secondId);
       id = UUID.randomUUID();
   }
   
   public UUID GetId() {
       return id;
   }
   
   public UUID GetRegion1Id(){
       return region1Id;
   }
   public UUID GetRegion2Id(){
       return region2Id;
   }
   
   public void setRegion1Id(UUID id){
       region1Id = id;
   }
   
   public void setRegion2Id(UUID id){
       region2Id = id;
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
}


