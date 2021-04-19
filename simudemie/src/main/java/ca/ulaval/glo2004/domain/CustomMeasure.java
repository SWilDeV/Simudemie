/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

/**
 *
 * @author melanietremblay
 */
public class CustomMeasure extends HealthMesure {
    
    //à déterminer
    public CustomMeasure(double p_adhesionRate, boolean p_active, String p_mesureName, double p_threshold,
                          double p_effectTransmissionRate, double p_effectReproductionRate) {
        super(p_adhesionRate, p_active, p_mesureName, p_threshold, p_effectTransmissionRate, 
                        p_effectReproductionRate);
    }
}
