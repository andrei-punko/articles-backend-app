
echo "Run load tests. Check report at ./load-test/target/gatling/"

cd load-test
mvn gatling:test -Dlogback.configurationFile=logback-gatling.xml
