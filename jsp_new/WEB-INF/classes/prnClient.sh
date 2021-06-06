#!/bin/sh
$JAVA_HOME/bin/java -cp /usr/local/share/tomcat5/webapps/prochainTaiga/WEB-INF/lib/jdom.jar:/usr/local/share/tomcat5/webapps/prochainTaiga/WEB-INF/lib/xml.jar:/usr/local/share/tomcat5/webapps/prochainTaiga/WEB-INF/lib/xerces.jar:/usr/local/share/tomcat5/webapps/prochainTaiga/WEB-INF/lib/parser.jar: com.dimata.printman.RemotePrintTarget 192.168.0.2
