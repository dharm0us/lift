package com.lift;

import com.lift.model.Lift;

/**
 * Hello world!
 *
 */
public class LiftController 
{
    public static void main( String[] args )
    {
    	Lift lift = new Lift();
    	lift.init();
    	
    
    	
    	while(true) {//travel loop
    		lift.travelIfNeeded();
    		sleep(1);
    	}
    	
    }
    
    private static void sleep(int delay) {
    	
    }
}
