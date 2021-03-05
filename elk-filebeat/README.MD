
# ELK stack for service logs monitoring. Communication performing by Filebeat

### Start containers with ELK and Filebeat:

    docker compose up

### Build and start service
    
    cd ..
    mvn clean install
    run.bat

### Open Kibana dashboard:

    http://localhost:5601

Using next login/password: 

    elastic/yourstrongpasswordhere

Add log index pattern:

    filebeat-*

Take a look on logs
