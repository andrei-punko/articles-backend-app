#!/bin/bash
java -jar /home/ubuntu/articles-backend-app-0.0.1-SNAPSHOT.jar \
 --spring.datasource.url=jdbc:h2:mem:testdb \
 --spring.datasource.username=sa \
 --spring.datasource.password=password \
 --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect > /dev/null 2> /dev/null < /dev/null &
