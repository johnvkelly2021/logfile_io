# Logfile_io
 
The following instructions should be used in order to run this application:


** To run the HSQL server:

cd [root]/hsqldb/data

java -cp ../lib/hsqldb.jar org.hsqldb.Server -database.0 file:./logdb -dbname.0 logdb



** To run the application:

mvn spring-boot:run -Dspring-boot.run.arguments=--file=[PATH-TO-LOG-FILE]



** to debug the application (if executed using the maven plugin)

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" -Dspring-boot.run.arguments=--file=[PATH-TO-LOG-FILE]

...then create a remote debugging session on port 5005...