/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.List;

/**
 *
 * @author Abergel Clement
 */
public interface WorldObserver {
    public void OnSimulationTick(int day, int deads,int infected, int PopTot);
    public void OnLinksUpdated();
}
