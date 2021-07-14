# Logfile_io
 
This application takes a filename as input parameter (not command-line prompt), processes log events and saves results to the database. It also logs alerts when events are greated than 4 seconds. The startup sequence to run this is:
1) Start the HSQLDB server
2) Run the application

The following instructions should be used...

### Prerequisites

Git installed

Java 8 JDK installed

Maven installed - I used version 3.6.3



### To run the HSQL server:

cd [ROOT-OF-APPLICATION]/hsqldb/data

java -cp ../lib/hsqldb.jar org.hsqldb.Server -database.0 file:./logdb -dbname.0 logdb


### To run the application directly using Java:

(Note: execute 'mvn package' to create the binaries)

java -jar [ROOT-OF-APPLICATION]/target/logfile-io-0.0.1-SNAPSHOT.jar --file=[PATH-TO-LOG-FILE]


### To run the application using the spring-boot maven plugin (navigate to the appropriate folder):

mvn spring-boot:run -Dspring-boot.run.arguments=--file=[PATH-TO-LOG-FILE]



### Notes
* The main application requires a HSQLDB running in server mode, but the unit tests use an in-memory H2 database. The HSQLDB jar and data files are in the [ROOT-OF-APPLICATION]/hsqldb.
* There is a sample logfile.txt included in the /resources folder of the main application. There are others in the test/resources folder.
* The approach I took was to develop the application in a way that would showcase (at least some) of the methodolgies I would use on a production/commercial application e.g. hence the use of JPA/Hibernate where some simple and basic JDBC would have sufficed.

...and finally, I enjoyed working on this and thanks for taking the time to have a look !