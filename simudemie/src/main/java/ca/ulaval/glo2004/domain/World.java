/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class World implements Serializable, Cloneable {
    
    private transient WorldController worldController;
    private List<Link> linkList = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();
    private Population worldPopulation = new Population();
    private static final long serialVersionUID = 1L; 
    
    public World() {
    }

    public World(WorldController worldController) {
        this.worldController = worldController;
    }
    
    public void SetWorldController(WorldController worldController) {
        this.worldController = worldController;
    }
    
    public void LoadWorld(World world) {
        countryList = new ArrayList<>(world.getCountries());
        linkList = new ArrayList<>(world.getLinks());
        worldPopulation = new Population(world.getWorldPopulation());
    }
    
    
    public void AddMesure(UUID countryId, double adhesionRate, boolean active, String mesureName) {
       Country c = FindCountryByUUID(countryId);
       if(c != null) {
            HealthMesure mesure = new CustomMeasure(adhesionRate, active, mesureName);
            c.AddMesure(mesure);
            worldController.NotifyOnMesureCreated();
       }
    }
    
    public void addCountry(Country country){
         countryList.add(country);
         worldController.NotifyCountryCreated(new CountryDTO(country));
         UpdateLandBorder(country.GetId());
    }

    public void addLink(Link link) {
        linkList.add(link);
        worldController.NotifyOnLinkCreated();
    }
    
    public void Addlink(UUID firstCountryId, UUID secondCountryId, Link.LinkType type) {
        Link link = new Link(firstCountryId, secondCountryId, type);       
        boolean isAbleToLink = !linkList.stream().anyMatch(e -> e.equals(link));
 
        if(type == Link.LinkType.TERRESTRE) {
            Country country1 = FindCountryByUUID(firstCountryId);
            Country country2 = FindCountryByUUID(secondCountryId);
            if(country1 != null && country2 != null) {
                isAbleToLink = Utility.AsCommonLandBorder(country1, country2);
            } else {
                isAbleToLink = false;
            }
        }
        
        if(isAbleToLink) {
            linkList.add(link);
            worldController.NotifyLinksUpdated(); //TODO: Changer!!! pour worldController.NotifyOnLinkCreated();
            worldController.NotifyOnLinkCreated();
        }
        
    }
    
    public void addRegion(UUID countryId, List<Point> points, String name) {
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            country.addRegion(points, name);
            worldController.NotifyOnRegionCreated();
        }
    }
        
    public void clearWorld() {
        countryList.clear();
        linkList.clear();
        worldPopulation = new Population();
    }
    
    public boolean ExistLink(Country a, Country b, Link.LinkType type) {
        return linkList.stream().anyMatch(l -> l.GetLinkType().equals(type) &&
                                                   (l.getCountry1Id() == a.GetId() ||
                                                    l.getCountry2Id() == a.GetId()) &&
                                                   (l.getCountry1Id() == b.GetId() ||
                                                    l.getCountry2Id() == b.GetId()));
    }
    
    public Country findCountryByPosition(Point position) {
        try {
            return countryList.stream().filter(c -> Utility.IsInRectangle(c.getShape().GetPoints(), position)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public Country FindCountryByUUID(UUID id) {
        try {
            return countryList.stream().filter(c -> c.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public Link FindLinkByUUID(UUID id) {
        try {
            return linkList.stream().filter(l -> l.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public List getCountries(){
        return countryList;
    }
    
    public List getLinks(){
        return linkList;
    }
    
    public Population getWorldPopulation(){
        return worldPopulation;
    }
    
    public void RemoveAllBorders(Country country) {
        List<Link> links = linkList.stream().filter(l -> l.getCountry1Id().equals(country.GetId()) ||
                                                    l.getCountry2Id().equals(country.GetId())).collect(Collectors.toList());
        
        for(int i = 0; i < links.size(); i++) {
            linkList.remove(links.get(i));
        }
        
        worldController.NotifyLinksUpdated();
    }
    
    public void removeCountry(UUID countryId){
        Country country = FindCountryByUUID(countryId);
        if(country != null) {
            RemoveAllBorders(country);
            countryList.remove(country);
            worldController.NotifyOnCountryDestroy();
        }
    }
    
    public void RemoveLink(UUID linkId) {
        Link link = FindLinkByUUID(linkId);
        if(link != null) {
            linkList.remove(link);
            worldController.NotifyLinksUpdated(); //TODO: Changer ca!!!
            worldController.NotifyOnLinkDestroy();
        }
    }
    
    
    public void RemoveRegion(UUID countryId, UUID regionId) {
        Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.removeRegion(regionId);
            worldController.NotifyOnRegionDestroy();
        }
    }
    
   public void RemoveMesure(UUID countryId, UUID mesureId) {
       Country c = FindCountryByUUID(countryId);
       if(c != null) {
        c.RemoveMesure(mesureId);
        worldController.NotifyOnMesureDestroy();
       }
    }
    
    public void UpdateSelectionStateRegion(UUID countryId, UUID regionId, boolean select) {
       Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.UpdateSelectionStateRegion(regionId, select);
        } 
    }
    
    public void UpdateRegion(UUID countryId, RegionDTO region) {
        Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.UpdateRegion(region);
            worldController.NotifyOnRegionUpdated();
        }
    }
    
    
    public void UpdateSelectionStateCountry(UUID id, boolean select) {
        Country country = FindCountryByUUID(id);
        if(country != null) {
            country.SetSelectionState(select);
        }
    }

    public void updateCountry(CountryDTO country) {
        Country c = FindCountryByUUID(country.Id);
        if(c != null){
            c.fromCountryDTO(country);
            UpdateLandBorder(country.Id);
            c.setName(country.Name);
            c.updateTotalPopulation(country.populationDTO.totalPopulationDTO);
        }
    }
    
    public void UpdateTotalPopulation(UUID countryId, int totalPopulation) {
        Country c = FindCountryByUUID(countryId);
        if(c != null) {
            c.updateTotalPopulation(totalPopulation);
        }
    }

    
    public void updateRegionFromSimulation(Country country,Region region) {
        Country c = FindCountryByUUID(country.GetId());
        if(c != null){
            Region r =c.FindRegionByUUID(region.GetId());
            if(r != null){
                r.updateRegion(region);
            }
        }
    }
    
    public void UpdateSelectionStateLink(UUID linkId, boolean select) {
        Link l = FindLinkByUUID(linkId);
        if(l != null) {
            l.SetSelectionState(select);
        }
    }
    
    public void UpdateLandBorder(UUID countryId) {
        Country country = FindCountryByUUID(countryId);

        for(Country c: countryList) {
            if(c != country) {
                if(Utility.AsCommonLandBorder(c, country) && !ExistLink(c, country, LinkType.TERRESTRE)) {
                    Addlink(c.GetId(), country.GetId(), LinkType.TERRESTRE);
                    //Addlink(c.getRegion0().GetId(), country.getRegion0().GetId(), LinkType.TERRESTRE);
                }
            }
        }
        
        
        List<Link> links = linkList.stream().filter(l -> l.GetLinkType().equals(LinkType.TERRESTRE) &&
//                                                   (l.getCountry1Id().equals(country.getRegion0().GetId()) ||
//                                                    l.getCountry2Id().equals(country.getRegion0().GetId()))).collect(Collectors.toList());
                                                   (l.getCountry1Id().equals(country.GetId()) ||
                                                    l.getCountry2Id().equals(country.GetId()))).collect(Collectors.toList());
        
        if(links != null) {
            for(int i = 0; i < links.size(); i++) {
                Country a = FindCountryByUUID(links.get(i).getCountry1Id());
                Country b = FindCountryByUUID(links.get(i).getCountry2Id());
                if(!Utility.AsCommonLandBorder(a, b)) {
                    linkList.remove(links.get(i));
                }
            }
        }
        
        worldController.NotifyLinksUpdated();
    }
    
    public void updateWorldPopulation(){
        int totalPop = 0;
        int infectedPop = 0;
        int deadPop = 0;
        int nonInfectedPop = 0;
        for(Country country: countryList){
            country.updateCountryPopulation();
            totalPop += country.getPopulation().getTotalPopulation();
            infectedPop += country.getPopulation().getInfectedPopulation();
            deadPop += country.getPopulation().getDeadPopulation();
            nonInfectedPop += country.getPopulation().getNonInfectedPopulation();
        }
        worldPopulation.setInfectedPopulation(infectedPop);
        worldPopulation.setNonInfectedPopulation(nonInfectedPop);
        worldPopulation.setDeadPopulation(deadPop);
        worldPopulation.setTotalPopulation(totalPop);
    }
    
    
    public void ValidateRegions() throws NotAllPopulationAssign {
        for(Country c: countryList) {
            c.ValidateRegions();
        }
    }
    
    @Override
    public World clone() throws CloneNotSupportedException {
        World world = null;
        try {
            world = (World) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        List<Country> countriesClone = new ArrayList<>(countryList.size());
        countryList.forEach(c -> { try {
            countriesClone.add((Country) c.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
});
        
        List<Link> linksClone = new ArrayList<>(linkList.size());
        linkList.forEach(l -> { try {
            linksClone.add((Link) l.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
});
        
        world.countryList = countriesClone;
        world.linkList = linksClone;
        world.worldPopulation = worldPopulation.clone();
        
        return world;
    }
}
