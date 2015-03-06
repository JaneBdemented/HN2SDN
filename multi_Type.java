package JDBC;

import java.sql.Time;
import java.math.BigInteger;
public class multi_Type {
	private boolean a1;//block
	private int a2;//usr limit
	private int a3;//total limit
	private java.sql.Time a4;//start time
	private java.sql.Time a5;//stope time
	private int a6; //bandwidth
	private int a7; //current user usage
	private int a8;//current total usage
	private String a9;//terminal name
	public multi_Type(boolean boolean1, int int1, int int2, Time time,
			Time time2, int BW, int UsrUsage, int totUsage, String name) {
	    a1 = boolean1;
	    a2 =int1;
	    a3 = int2;
	    a4 = time;
		a5 = time2;
		a6 = BW;
		a7 = UsrUsage;
		a8 = totUsage;
		a9 = name;
		
	}
	public boolean getBlock(){
		return(this.a1);
	}
	public int getUsrUsageLimit(){
		return(this.a2);
	}
	public int getTotalUsageLimit(){
		return(this.a3);
	}
	public java.sql.Time getStartTime(){
		return(this.a4);
	}
	public java.sql.Time getStopTime(){
		return(this.a5);
	}
	public int getBandwidth(){
		return(this.a6);
	}
	public int getUsrUsage(){
		return(this.a7);
	}
	public int getNetworkUsage(){
		return(this.a8);
	}
	public String getName(){
		return(this.a9);
	}
	
}
