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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class Country implements Serializable, Cloneable  {
    
    private List<HealthMesure> mesures = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private List<RegionLink> regionLinks = new ArrayList<>();
    private Population population;
    private GeometricForm shape;
    private Color color = Color.GREEN;
    private String name;
    private UUID id;
    private boolean isSelected;
    private static final long serialVersionUID = 2L; 
    
    public Country(GeometricForm form, String countryName, int countryPop){
        id = UUID.randomUUID();
        shape = form;
        name = countryName;
        population = new Population(countryPop);
        //regions.add(new Region(new IrregularForm(form.GetPoints()),"region1",countryPop,1));
    }
    
    public void AddMesure(HealthMesure mesure) {
        mesures.add(mesure);
        for (Region region: regions) {
            region.AddMesure(mesure);
        }
    }
    
    public void RemoveMesure(UUID mesureId) {
        HealthMesure mesure = null;
        for(HealthMesure m: mesures) {
            if(m.getID().equals(mesureId)) {
                mesure = m;
                break;
            }
        }
        
        mesures.remove(mesure);
        for (Region region: regions) {
            region.RemoveMesure(mesure);
        }
    }
    
    public List<HealthMesure> GetMesures() {
        return mesures;
    }
    
    @Override
    public boolean equals(Object other) {       
        if(other == null || !(other instanceof Country)){
            return false;
        }
        
        if(other == this) {
            return true;
        }
        
        Country country = (Country)other;
        return id == country.GetId();
    }

    public void fromCountryDTO(CountryDTO countryDTO){
        if(countryDTO.Shape instanceof RegularForm) {
            shape = new RegularForm(countryDTO.Shape.GetPoints());
        } else {
            shape = new IrregularForm(countryDTO.Shape.GetPoints());
        }
        
        regions = countryDTO.Regions.stream().map(r -> r.toRegion()).collect(Collectors.toList());
        population.setTotalPopulation(countryDTO.getPopulationDTO().getTotalPopulationDTO());
        population.setCuredPopulation(countryDTO.getPopulationDTO().getCuredPopulationDTO());
        population.setInfectedPopulation(countryDTO.getPopulationDTO().getInfectedPopulationDTO());
        population.setNonInfectedPopulation(countryDTO.getPopulationDTO().getNonInfectedPopulationDTO());
        population.setDeadPopulation(countryDTO.getPopulationDTO().getDeadPopulationDTO());
    }
    
    public Color getColor() {
//        double p = 1.0;
//        double p_dead;
//        if(population.getTotalPopulation() > 0) {
//            if(population.getInfectedPopulation() > 0) {
//                p = (population.getInfectedPopulation() ) / (double)population.getTotalPopulation() ;
//                if(p > 1) {
//                    p = 1;
//                }
//                if(population.getDeadPopulation() > 0){
//                    p_dead = (population.getInfectedPopulation() ) / (double)(population.getTotalPopulation() + population.getDeadPopulation());
//                }
//                else {
//                    p_dead = 0.0;
//                } 
//            } else {
//                p = 0.0;
//            }
//        }
//        
//        if(population.getTotalPopulation() == 0){
//            return new Color(100,0,0);
//        }
//        else{
//            return Utility.GetColorGradient(p);
//        
//        }
        return new Color(218, 227, 127);
        
        //return Utility.GetColorGradientSrcDest(variance_rouge_vert, Color.black, p);    // marche mais cest pas beau
        //return variance_rouge_vert;
        
    }
    
    public UUID GetId() {
        return id;
    }
    
    public List GetLinks(){
        return regionLinks;
    }
    
    public String getName() {
        return name;
    }
    
    public Population getPopulation(){
        return population;
    }
    
    public GeometricForm getShape(){
        return shape;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    public boolean IsSelected() {
        return isSelected;
    }
    
    public void setName(String countryName) {
        name = countryName;
    }
    
    public void setPopulation(Population population) {
        this.population = population;
    }
    
    public void setRegionLinkTransmissionRate(double transmissionRate) {
        for (RegionLink link : regionLinks) {
            link.setTransmissionRate(transmissionRate);
        }
    }
    
    public void UpdateSelectionStateRegion(UUID regionId, boolean select) {
        Region region = regions.stream().filter(r -> r.GetId().equals(regionId)).findFirst().get();
        if(region != null) {
            region.SetSelectionState(select);
        }
    }
    
    public void updateTotalPopulation(int totalPopulation) {
        population.setTotalPopulation(totalPopulation);
        regions.forEach(r -> {
            r.SetPercentage(totalPopulation, r.getPercentagePop());
        });
    }
    
    public void updateCountryPopulation(){
        int totalPopulation =0;
        int infectedPopulation =0;
        int nonInfectedPopulation =0;
        int deadPopulation =0;
        for(Region region:regions){
            nonInfectedPopulation += region.getPopulation().getNonInfectedPopulation();
            infectedPopulation += region.getPopulation().getInfectedPopulation();
            totalPopulation += region.getPopulation().getTotalPopulation();
            deadPopulation += region.getPopulation().getDeadPopulation();
        }
        population.setTotalPopulation(totalPopulation);
        population.setInfectedPopulation(infectedPopulation);
        population.setNonInfectedPopulation(nonInfectedPopulation);
        population.setDeadPopulation(deadPopulation);
    }
    
    public void SetPosition(Point position) {
        shape.SetPosition(position);
    }
    
    public void SetSelectionState(boolean select) {
        isSelected = select;
    }
    
    public void splitNewInfectedInRegions(int newInfected){
        while (newInfected >0){
            Random rand = new Random();
            int index = rand.nextInt(regions.size());

            int counter = 0;
            for(Region region:regions){
                if(index == counter){
                    Population population = region.getPopulation();
                    int infected = population.getInfectedPopulation();

                    Random randPop = new Random();
                    int randInfected = randPop.nextInt(newInfected);

                    if(randInfected == 0){
                        newInfected = 0;
                    }else{
                        infected +=randInfected;
                        population.setInfectedPopulation(infected);
                        region.setPopulation(population);
                        newInfected -=randInfected;
                    }
                }
                counter +=1;
            }
        }
    }
    
    public void removeTravellingInfectedInRegions(int newInfected){
        while (newInfected >0){
            Random rand = new Random();
            int index = rand.nextInt(regions.size());

            int counter = 0;
            for(Region region:regions){
                if(index == counter){
                    Population population = region.getPopulation();
                    int infected = population.getInfectedPopulation();

                    Random randPop = new Random();
                    int randInfected = randPop.nextInt(newInfected);

                    if(randInfected == 0){
                        newInfected = 0;
                    }else{
                        infected -=randInfected;
                        if(infected < 0){
                            infected = 0;
                        }
                        population.setInfectedPopulation(infected);
                        region.setPopulation(population);
                        newInfected -=randInfected;
                    }
                }
                counter +=1;
            }
        }
    }
    
    
    
    /////////////////REGIONS////////////////////////
    
    public void addRegion(List<Point> points, String regionName) {
        double percentagePerRegion = ((double)population.getTotalPopulation() / (regions.size() + 1)) / (double)population.getTotalPopulation();
        
        regions.forEach(r -> {
            r.SetPercentage(population.getTotalPopulation(), percentagePerRegion);
        });
        
        double lastPercentage = 1.0 - (percentagePerRegion * regions.size());
        
        List<Point> clonePts = new ArrayList<>();
        for(Point pt: points) {
            clonePts.add((Point)pt.clone());
        }
        
        IrregularForm form = new IrregularForm(clonePts);
        Region region = new Region(form, regionName, population.getTotalPopulation(), lastPercentage);
        
        //ajout liens entre regions a chaque creation de region (sauf pour la premiere region)
        if(regions.size()>0){
            regions.forEach(r->{
            RegionLink link = new RegionLink(region.GetId(),r.GetId());
            regionLinks.add(link);
        });
        }
        
        regions.add(region);
    }
    
    public Region FindRegionByUUID(UUID id) {
        try {
            return regions.stream().filter(c -> c.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public List<Region> GetRegions() {
        return regions;
    }
    
    public void UpdateRegion(RegionDTO region) {
        Region r = FindRegionByUUID(region.Id);
        if(r != null) {
            r.SetPercentage(population.getTotalPopulation(), region.PercentagePop);
        }
    }
    
    public void ValidateRegions() throws NotAllPopulationAssign {
        double totalPercentage = 0.0;
        for(Region r: regions) {
            totalPercentage += r.getPercentagePop();
            if(totalPercentage > 1) {
                throw new NotAllPopulationAssign("Les r??gions ont plus de population que le pays!");
            }
        }
        
        if(totalPercentage != 1.0) {
            throw new NotAllPopulationAssign("La population total n'as pas ??t?? compl??tement assign??.");
        }
    }
    
    public void removeRegion(UUID regionId){//faudrait ajouter un index ou un id de la region
        Region r = FindRegionByUUID(regionId);
        if(r != null) {
            regions.remove(r);
            double sommePourcentage = 0;
            for(Region reg : regions){
                sommePourcentage+= reg.getPercentagePop();
            }
            Region lastResgionOfList = regions.get(regions.size()-1);
            double newLastPourcentage = lastResgionOfList.getPercentagePop() + (1 - sommePourcentage);
            lastResgionOfList.SetPercentage(population.getTotalPopulation(), newLastPourcentage);
            System.out.println(r.getPercentagePop());
        }
    }
    
    public void setPopulationToRegion(Population population,UUID regionId){ 
        Region r = FindRegionByUUID(regionId);
        if(r != null) {
            r.setPopulation(population);
        } 
    }
    
    @Override
    public Country clone() throws CloneNotSupportedException {
        Country country = null;
        try {
            country = (Country) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        country.population = population.clone();
        country.id = id;
        country.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
        country.shape = shape.clone();
        
        
        List<Region> regionsClone = new ArrayList<>(regions.size());
        regions.forEach(r -> { try {
            regionsClone.add((Region) r.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
            }
});
        country.regions = regionsClone;
          
        List<HealthMesure> mesuresClone = new ArrayList<>(mesures.size());
        mesures.forEach(m -> { try {
            mesuresClone.add((HealthMesure) m.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
            }
});
        country.mesures = mesuresClone;
        
        return country;
    }
}
