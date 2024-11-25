
java ^
-Dcom.sun.management.jmxremote=true ^
-Dcom.sun.management.jmxremote.port=5656 ^
-Dcom.sun.management.jmxremote.authenticate=false ^
-Dcom.sun.management.jmxremote.ssl=false ^
-Djava.rmi.server.hostname=localhost ^
-Dcom.sun.management.jmxremote.rmi.port=5656 ^
-jar target/articles-backend-app-0.0.1-SNAPSHOT.jar ^
 --spring.datasource.url=jdbc:h2:mem:testdb ^
 --spring.datasource.username=sa ^
 --spring.datasource.password=password ^
 --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
