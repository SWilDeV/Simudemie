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
public class Calculation {
    private List<Country> countryList = new ArrayList<Country>();
    public Calculation() {
        
    }
    
    public void Calculate(){
        Random rnd = new Random(42);
        double tolerance = 0.0001;
        BinomialDistribution binomial = new BinomialDistribution(100,0.05);
        ArrayList<Double> probabilites = new ArrayList<>();
        ArrayList<Integer> numberOfSuccess = new ArrayList<>();
        
        int x = 5;
        double prob = binomial.probability(x);
        while(prob > tolerance){
            probabilites.add(prob);
            numberOfSuccess.add(x);
            x+=1;
            prob = binomial.probability(x);
        }
        
        x = 4;
        prob = binomial.probability(x);
        while(prob > tolerance && x>=0){
            probabilites.add(prob);
            numberOfSuccess.add(x);
            x-=1;
            prob = binomial.probability(x);
        }
        
        System.out.println(probabilites);
        System.out.println(numberOfSuccess);
     
        
        for(int k=0; k<10;k++){
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
            System.out.println(success); 
        }
        
               
    }
    
}
