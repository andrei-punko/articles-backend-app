#!/usr/bin/env bash

cd /home/ubuntu/server

sudo /usr/bin/java -jar \
    -Dspring.datasource.url=jdbc:h2:mem:testdb \
    -Dspring.datasource.username=sa \
    -Dspring.datasource.password=password \
    -Dspring.datasource.username=sa \
    -Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect \
    *.jar > /dev/null 2> /dev/null < /dev/null &
