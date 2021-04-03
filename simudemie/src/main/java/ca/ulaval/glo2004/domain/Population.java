/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;

/**
 *
 * @author Sean
 */
public class Population implements Serializable { 
    //    Attributs
    private int totalPopulation;
    private int infectedPopulation =0;
    private int curedPopulation = 0;
    private int deadPopulation = 0;
    private int nonInfectedPopulation =0;
    
    public Population(){
    }
    //   methodes
    public Population(int p_TotalPop){
        this.totalPopulation = p_TotalPop;
    }
    
    public int getTotalPopulation(){
        return totalPopulation;
    }
    
    public void setTotalPopulation(int p_TotalPop){
        totalPopulation = p_TotalPop;
    }
    
    public void addPatientZero(){
        infectedPopulation = 1;
    }
    
    public int getInfectedPopulation(){
        return infectedPopulation;
    }
    
    public int getCuredPopulation(){
        return curedPopulation;
    }
    
    public int getDeadPopulation(){
        return deadPopulation;
    }
    
    public void setInfectedPopulation(int p_InfectedPop){
        infectedPopulation = p_InfectedPop;
    }
    
    public void setCuredPopulation(int p_CuredPop){
        curedPopulation = p_CuredPop;
    }
    
    public void setDeadPopulation(int p_DeadPop){
        deadPopulation = p_DeadPop;
    }
    
    public int getNonInfectedPopulation(){
        return nonInfectedPopulation;
    }
    
    public void setNonInfectedPopulation(int p_NonInfectedPop){
        nonInfectedPopulation = p_NonInfectedPop;
    }
}
