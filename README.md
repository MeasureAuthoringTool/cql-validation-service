# cql-validation-service
CQL Validation Rules

# Running Locally

```
mvn clean test verify install spring-boot:run
```

The application can also be run in a Docker container.  This can be done by running

```
docker compose down --remove-orphans && docker volume prune && docker compose build --no-cache && docker compose up --force-recreate --build madie-measure
```

#Testing
The application can be check for "health" by running Spring actuator endpoints.  For example, 

```
curl --location --request GET 'http://localhost:8088/api/actuator/health'