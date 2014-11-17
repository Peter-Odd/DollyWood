==== COURSE ==== 

Process Oriented Programming (1DT049) Spring 2014

Department of Information Technology 
Uppsala university


==== GROUP ==== 

OSM Group 05


==== PROJECT NAME ==== 

DollyWood


==== PROJECT DESCRIPTION ==== 

Sheep pasture simulation using actors as main concurrency model. Implemented in Java. 


==== GROUP MEMBERS ==== 

831130 samuel.svensater.7105@student.uu.se
<br>900409 viktor.andersson.7837@student.uu.se
<br>910915 elin.parsjo.5585@student.uu.se
<br>920425 jimmy.helmersson.6800@student.uu.se
<br>931007 marcus.munger.6193@student.uu.se


==== MAY THE SOURCE BE WITH YOU ==== 

Everything you need to compile and run the system is included in src (source)
directory. 

However, you might want to get the most up to date version of this
directory. 

Usage: git clone https://github.com/Peter-Odd/DollyWood.git


==== JAVA VERSION ====

This software was developed and tested using Java 1.7.

     	      	  	    	       
==== MAKE IT HAPPEN ==== 

Set up Eclipse workspace to point at newly cloned folder from github repository above
and then use Eclipse to compile and run.

<br>Compile and run ==> Eclipse menu: Run -> Run.
<br>Clean project   ==> Project -> Clean.
<br>JDoc		     ==> Project -> Generate Javadoc
<br>Tests		     ==> Select test to run under src/tests and go to Run - Run As - JUnit Test


==== TO COMPILE ==== 

See note above.


==== TO RUN AND TEST THE SYSTEM ==== 

Run tests in Eclipse (see above)
OR
run the following command in the terminal (you should be in folder DollyWood/):
java -Djava.library.path=lib\native -jar DollyWood.jar 

Windows users can run the file run.bat do the above stated.
