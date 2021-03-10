
# ELK stack for service logs monitoring. Communication performing by Filebeat

### Start containers with ELK and Filebeat:

    docker compose up

### Build and start service
    
    cd ..
    mvn clean install
    run.bat

### Check saved logs via Kibana dashboard:

    http://localhost:5601

Using next login/password: `elastic/yourstrongpasswordhere`

Choose `Discover` -> `Create index pattern`, and enter `filebeat-*`

Take a look on logs

### How to destroy containers and volumes (with stored data):

    docker-compose down --volumes
