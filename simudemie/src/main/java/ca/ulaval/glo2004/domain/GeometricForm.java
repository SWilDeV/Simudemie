/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.List;

/**
 *
 * @author charl
 */
public abstract class GeometricForm {
    
    protected List<RegularForm> Pointslist;
    
    public GeometricForm(){
        
    }
    
    public List getPoints(){
        //return Pointslist; //Sans doute passer la liste en new List<RegularForm>(Pointslist);
        throw new UnsupportedOperationException("Not supported");
    } 
}
