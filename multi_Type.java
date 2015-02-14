package JDBC;

import java.sql.Time;
import java.math.BigInteger;
public class multi_Type {
	private boolean a1;
	private int a2;
	private int a3;
	private java.sql.Time a4;
	private java.sql.Time a5;
	
	public multi_Type(boolean boolean1, int int1, int int2, Time time,
			Time time2) {
	    a1 = boolean1;
	    a2 =int1;
	    a3 = int2;
	    a4 = time;
		a5 = time2;
		
	}
	public boolean getBlock(){
		return(this.a1);
	}
	public int getUsrUsage(){
		return(this.a2);
	}
	public int getTotalUsage(){
		return(this.a3);
	}
	public java.sql.Time getStartTime(){
		return(this.a4);
	}
	public java.sql.Time getStopTime(){
		return(this.a5);
	}
}
