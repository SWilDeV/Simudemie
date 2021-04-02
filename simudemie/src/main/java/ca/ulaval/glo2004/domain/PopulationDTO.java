/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

/**
 *
 * @author Sean
 */
public class PopulationDTO {
    public int totalPopulationDTO;
    public int infectedPopulationDTO;
    public int curedPopulationDTO;
    public int deadPopulationDTO;
    public int nonInfectedPopulationDTO;
    
    public PopulationDTO(Population population) {
        totalPopulationDTO = population.getTotalPopulation();
        infectedPopulationDTO = population.getInfectedPopulation();
        curedPopulationDTO = population.getCuredPopulation();
        deadPopulationDTO = population.getDeadPopulation();
        nonInfectedPopulationDTO = population.getNonInfectedPopulation();
    }
    public int getTotalPopulationDTO(){
        return totalPopulationDTO;
    }
    

    public void addPatientZeroDTO(){
        infectedPopulationDTO = 1;
    }
    
    public int getInfectedPopulationDTO(){
        return infectedPopulationDTO;
    }
    
    public int getCuredPopulationDTO(){
        return curedPopulationDTO;
    }
    
    public int getDeadPopulationDTO(){
        return deadPopulationDTO;
    }
    
    public int getNonInfectedPopulationDTO(){
        return nonInfectedPopulationDTO;
    }
    
    public void setInfectedPopulationDTO(int p_InfectedPop){
        infectedPopulationDTO = p_InfectedPop;
    }
    
    public void setCuredPopulationDTO(int p_CuredPop){
        curedPopulationDTO = p_CuredPop;
    }
    
    public void setDeadPopulationDTO(int p_DeadPop){
        deadPopulationDTO = p_DeadPop;
    }
    
    public void setTotalPopulationDTO(int p_TotalPop){
        totalPopulationDTO = p_TotalPop;
    }
    
    public void setNonInfectedPopulationDTO(int p_NonInfectedPop){
        nonInfectedPopulationDTO = p_NonInfectedPop;
    }
    
        @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Nombre de morts: %d \n", deadPopulationDTO));
        sb.append(String.format("Population infectée: %d \n", infectedPopulationDTO));
        sb.append(String.format("Population saine: %d \n", nonInfectedPopulationDTO));
        sb.append(String.format("Population total: %d \n", totalPopulationDTO));
        //sb.append(String.format("Guerrie total: %d \n", curedPopulationDTO));
        
        
        
        return sb.toString();
    }
}
