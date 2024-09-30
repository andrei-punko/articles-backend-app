
# ELK stack for service logs monitoring

Communication performing by Filebeat

## Start containers with ELK stack and Filebeat:
From [elk-filebeat](.) folder:

    docker compose up

## Build and start `article-backend-service` application
From project [root](..) folder:

    mvn clean install
    docker compose up

## Check saved logs via Kibana dashboard:
- Open page http://localhost:5601
- Use next `login/password` : `elastic/yourstrongpasswordhere`
- Choose `Discover` -> `Create index pattern`, and enter `filebeat-*`
- Take a look on saved records

## Destroy ELK containers and volumes (with stored data):
From [elk-filebeat](.) folder:

    docker-compose down --volumes
