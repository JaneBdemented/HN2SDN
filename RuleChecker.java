package org.opendaylight.controller.tutorial_L2_forwarding;

import java.lang.Object; 
import java.util.*;
import java.sql.Timestamp;
import java.lang.*;
import java.lang.reflect.Array;
import java.util.HashMap;

public class RuleChecker {
	
	private Date timeNow = new Date() ;
	private Timestamp timeStamp = new Timestamp(timeNow.getTime());
	
	public int Check_MAC_Rule(String MAC, List<?> rule){
		//first find the rules that apply
		
		int Mac_rule = rule.indexOf(MAC);
		ListIterator<?> index = rule.listIterator(Mac_rule);
		int block = (Integer) index.next();
		//act according to the rules
		switch (block){
			case 0: // unblocked MAC
				return (0);
			case 1: //blocked MAC
				return(1);
		}	
		index.next();
		index.next();
		int countERblock = (Integer) index.next();
		if(countERblock == 0){
			return (1);
		}else{
			
			Timestamp startTime = (Timestamp) index.next();
			Timestamp stopTime = (Timestamp) index.next();
			
			if ( timeStamp.before(startTime) && timeStamp.after(stopTime)){
				return(1);
			}else{
				return(1);
			}
		}
	} 
	
}
