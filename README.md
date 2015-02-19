Getting a JDBC driver working within the Opendaylight ad-sal Tutorial

1. Begin by making the mysql-connector-java-5.1.34.jar into a maven wrapped plug-in
you may need to down load the above driver 
right click inside the package explorer in eclips and select new project.
Expand the Plug-in Development tab and select Plug-in from Existing JAR Archives
click next then add External and select the mysql-connector-java... jar from your files
click next and name your project (I had named it com.mysql.jdbc) correct the version to that of your jar file, (note the Plug-in name that has been given to the file)
click finish then yes
Now you have the maven plug-in

2. changing the pom.xml files
	a. Within the terminal navigate to the following:
		cd SDNHub_Opendaylight_Tutorial/adsal_L2_forwarding/
now use the nano editor to exit the pom type
nano pom.xml
Scroll down until you see <Import-Package>	
under this headding type com.mysql.jdbc, (or what u named your plug-in, however dont forget the comma)
scroll down further until you hit the bottom of the file above </project> and </dependancies> type the following:
<dependency>
<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.34</version>
		      </dependency>
now press CTL+x and then y and Enter
	b. Next navigate to the following:
		cd SDNHub_Opendaylight_Tutorial/distribution/opendaylight-osgi-adsal
again use the nano editor
nano pom.xml
scroll down until <!---- SDNHub Artifacts – >  at the bottom of this section add the following:
<dependency>
<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>5.1.34</version>
     </dependency>
now press CTL+x and then y and Enter
	c. finally navagate to the following:
		cd SDNHub_Opendaylight_Tutorial/commons/parent/
once again use the nano editor
nano pom.xml
scroll way down till you hit <repositories> and add the following:
			<repository>
			     <id>maven-central</id>
			     <url>http://repo1.maven.org/maven2</url>
			     <releases>
				<enabled>true</enabled>
			    <\releases>
			    <snapshots>
				<enabled>false</enabled>
			    </snapshots>
			<\repository>
now press CTL+x and then y and Enter

3. now you need to build  mvn clean install from the current directory
4. head back to cd SDNHub_Opendaylight_Tutorial/distribution/opendaylight-osgi-adsal and run mvn clean install -DskipTests -DskipIT -nsu
 
5. Install mySQL->JDBC driver sudo apt-get install libmysqll.java
That should do it! 
I will also post my updated code
