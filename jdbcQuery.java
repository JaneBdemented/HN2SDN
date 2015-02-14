 package JDBC;
   //import com.mysql.jdbc.PreparedStatement;
import java.sql.*;
    //import com.mysql.*;
    //import org.gjt.mm.mysql.*;
    public class jdbcQuery {
           
            private Connection myConnection;
            public jdbcQuery(){
                   
            }
            public Array Connect(long mac){ 
                     try {
                        // The newInstance() call is a work around for some
                        // broken Java implementations
                               
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                    } catch (Exception ex) {
                             System.out.println("SQLException: class.forname:" + ex.getMessage());
                    }
                                    //modify these as needed
                                    String rootUsername = "root";
                                    String rootPassword = "";
                                    String connectionDatabase = "jdbc:mysql://localhost:3306/macRulesSchema";
                                   
                                    try {
                                            //attempt to connect to mysql database
                                            myConnection = DriverManager.getConnection(connectionDatabase, rootUsername, rootPassword);                    
                                    }
                                    catch (SQLException ex) {//handel error here
                                             System.out.println("SQLException: Driver skrewup" + ex.getMessage());
                                             System.out.println("SQLState: " + ex.getSQLState());
                                             System.out.println("VendorError: " + ex.getErrorCode());
                                    }
                    boolean advance = false;                
                    PreparedStatement update = null;
                    PreparedStatement check = null;
                    String checkMAC = "SELECT from macRulesTable where (mac) = ?";
                    String inputMac = "INSERT into macRulesTable (mac) Value (?)";
                    try {
						check = myConnection.prepareStatement(checkMAC);
						check.setLong(1, mac);
						advance = check.execute();
						System.out.print(advance);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    
                    if(!advance){
                    	try{
                    		update = myConnection.prepareStatement(inputMac);
                    		update.setLong(1,mac);
                    		update.executeUpdate();
                    	}catch (Exception exc) {//handle exceptions here
                    		System.out.println(" Installing mac failed "+ exc.getMessage());
                            exc.printStackTrace();
                    	}
                    	finally { //cleanup
                            if (update != null) {
                                   // update.close();
                            }
                    	}
                    }else{
                    	PreparedStatement myStatement = null;
                    	ResultSet myResultSet = null;
                    	Array Rules = null;
                    	String rule[]=null;
                    	//String dbTable = "macRulesTable";
                    	String SQL = "(SELECT * FROM macRulesTable WHERE MAC = ?)";
                    	try {
                            myStatement = myConnection.prepareStatement(SQL);//generate statement based on established connection
                            myStatement.setLong(1,mac);
                            //execute bd extract query
                            myResultSet = (myStatement.executeQuery());
                            //myResultSet = myStatement.executeQuery("select * from ".concat(dbTable)."WHERE MAC =".concat(MAC));
                            //process result set to obtain the list of mac addresses with their respective blocked status
                            //while (myResultSet.next()) {
                            Rules = myResultSet.getArray("is_nullable"); //still needs up dating for array
                             rule =(String[]) Rules.getArray();
                            //}
                            //to add INSERT UPDATE  DELETE
                           
                    	}catch (Exception exc) {//handle exceptions here
                            exc.printStackTrace();
                    	}
                    	finally { //cleanup
                            if (myResultSet != null) {
                                //    myResultSet.close();
                            } if (myStatement != null) {
                                  //  myStatement.close();
                            }
                    	}
                    	try{
                    		myConnection.close();
                    		return(Rules);
                    	}catch(SQLException e){
                    		System.out.println("Could not close connetion: "+e.getMessage());
                    	}
                    }
                    return(null);
            }
    }

