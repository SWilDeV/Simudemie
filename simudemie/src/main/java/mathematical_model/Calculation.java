/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathematical_model;


import ca.ulaval.glo2004.domain.Country;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.BinomialDistribution;

/**
 *
 * @author Abergel Clement
 * 
 * A voir ce que l'on met dedans.
 */
public class Calculation implements java.io.Serializable {
    private List<Country> countryList = new ArrayList<Country>();
    public Calculation() {
        
    }
    
    public int Calculate(int x, double y){
        Random rnd = new Random();
        double tolerance = 0.0001;
        BinomialDistribution binomial = new BinomialDistribution(x,y);
        ArrayList<Double> probabilites = new ArrayList<>();
        ArrayList<Integer> numberOfSuccess = new ArrayList<>();
        
        int esperance = (int)Math.round(x*y);//esperance: (nombre de test)*(probabilite de succes)
        double prob = binomial.probability(esperance);
        while(prob > tolerance){
            probabilites.add(prob);
            numberOfSuccess.add(esperance);
            esperance+=1;
            prob = binomial.probability(esperance);
        }
        
        esperance = (int)Math.round(x*y)-1;//esperance - 1: (nombre de test)*(probabilite de succes)-1
        prob = binomial.probability(esperance);
        while(prob > tolerance && esperance>=0){
            probabilites.add(prob);
            numberOfSuccess.add(esperance);
            esperance-=1;
            prob = binomial.probability(esperance);
        }
//        
//        System.out.println(probabilites);
//        System.out.println(numberOfSuccess);
     
        
        //for(int k=0; k<10;k++){
            double randomNumber =  rnd.nextDouble();
            double current = 0.0;
            int success = 0;
            for (int i =0; i<probabilites.size();i++){
                current += probabilites.get(i);
                if(randomNumber < current){
                    success = numberOfSuccess.get(i);
                    break;
                }
            }
            
        //}
        
    return success;
    }
    
}
