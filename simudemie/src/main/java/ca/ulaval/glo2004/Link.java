/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004;

/**
 *
 * @author Sean
 */
enum linkType{
    AERIEN,
    TERRESTRE,
    MARITIME
}

public class Link {
//    Attributs
   private linkType link;
   private Country country1;
   private Country country2;
   private double travelRate;
   private boolean isOpen;
   
//   methodes
   public Link(){
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
   
   public void getEnumType(){
       switch (link){
           case AERIEN:
               break;
               
           case TERRESTRE:
               break;
               
           case MARITIME:
               break;
       }
   }
}
