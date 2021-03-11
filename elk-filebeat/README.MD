
# ELK stack for service logs monitoring. Communication performing by Filebeat

### Start containers with ELK stack and Filebeat:

    docker compose up

### Build and start article-backend-service
From project root:

    mvn clean install
    docker compose up

### Check saved logs via Kibana dashboard:

    http://localhost:5601

Using next login/password: `elastic/yourstrongpasswordhere`

Choose `Discover` -> `Create index pattern`, and enter `filebeat-*`

Take a look on saved records

### How to destroy ELK containers and volumes (with stored data):
From `elk-filebeat` folder:

    docker-compose down --volumes
