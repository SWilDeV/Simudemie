/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.UndoRedo.UndoRedoType;

/**
 *
 * @author Abergel Clement
 */
public interface WorldObserver {
    public void OnSimulationTick(int day, int deads,int infected, int PopTot);
    public void OnSimulationStarted();
    public void OnSimulationStopped();
    public void OnSimulationUndoRedo(UndoRedoType type);
    public void OnSimulationReset();
    
    public void OnLinkCreated();
    public void OnLinksUpdated();
    public void OnLinkDestroyed();
    
    public void OnDiseaseCreated(DiseaseDTO disease);
    public void OnDiseaseUpdated();
    public void OnDiseaseDestroyed();
    
    public void OnCountryCreated(CountryDTO country);
    public void OnCountryUpdated();
    public void OnCountryDestroy();
    
    public void OnMesuresCreated();
    public void OnMesuresUpdated();
    public void OnMesuresDestroy();
    
    public void OnRegionCreated();
    public void OnRegionUpdated();
    public void OnRegionDestroy();
    
    public void OnProjectLoaded();
}
