# GUUSTO Coding Challenge

## Overview
* I developed this solution using microservices depand on Orchestration-based saga pattern approach.
  with tow microservices `card-service` and `balance-service` with `common-dto` module for exchanging messages between them.
* 1-The Card Service receives the POST /orders request and creates the Create Order saga orchestrator
* 2- The saga orchestrator creates an Order in the PENDING state
* 3- It then sends a Reserve Credit command to the Balance Service
* 4- The Balance Service compares the total amount of order with customer's balance, the process of subtracting from the balance in the balance table and adding the number of cards in the gift table
* 5- It then sends back a reply message indicating the outcome
* Each microservice `card-service` , `balance-service` and `gateway-security` can build, run, dockerize, deploy & run independently using Docker, Kubernetes, and Jenkins

## TOOls
* JAVA 11 + Gradle
* Lombok: to reduce boilerplate code for model/data objects
* Spring Cloud Stream: for building highly scalable event-driven microservices connected with shared messaging systems.
* Spring Reactive Web:  to operate on the reactive HTTP request and response
* Spring Data JPA:  to access and persist data between Java object/ class and relational database.
* APACHE KAFKA
* Redis
## Database
* for coding-challenge I use `H2Database` it is Very fast, open source, JDBC API, and in-memory databases Browser based Console application.
* The database is automatically generated when the program is run
* card-service has table `PurchaseOrder` record the state of order and payment
* balance-service has three tables `UserBalanc` with columns userId and Balance, `UserCard` with columns userId and gift, and `UserTransaction`

## Security
* for coding-challenge purpose I made things as simple as I could.
* I use API Defuel Gateway by adding `gateway-security` microservice as Middleware between the clients and services.
* `gatway-security` for coding challenge I made authentication access token fixed string while in real project it will be auto generated when log in with username and password
* (Note) I am still working to complete Front end and `login_service`
  after finishing login_service we should authenticate first using `/login` api to get the JWT access token.

## Docker solution
### Dockerfile
I added `Dockerfile` in each microservice to provide configuration to build and run docker image.

1. To build a docker image for `card-service`, Goto the project's root location, where you have `Dockerfile` and run following command:-
    ```
    $guusto-challenge/card-service > build -t card-service .
    ```
### docker-compose.yaml
to docker multiple microservices at once I added `docker-compose.yaml` file in the root folder
Docker compose configuration file `docker-compose.yaml` is at the root folder of repo directory i.e. `guusto-challenge/docker-compose.yaml` which provides all the configuration required to build and run docker image of `card-service` and `balance-service`
to check goto the root directory `guusto-challenge`
and run

```
 docker-compose up
```

to bring all the services down:-

```
docker-compose down
```

### Docker Hub
first creating  account on docker hub, we push local docker images to remote docker hub repository
1. login to remote DockerHub account:-
   run
    ```
     docker login
    ```
2. tag local docker image to remote docker image:-
    ```
    docker tag com.guusto/card-service:latest aklahoti/card-service/0.0.1
    ```
3. push image to remote docker hub repository:-
    ```
    docker image push aklahoti/card-service:0.0.1 
    ``` 
## Kubernates Solution
I created `deployment.yml` in each microservice to provide configuration for kubernetes load-balancer service, deployment, pod and container.

To deploy docker image of `card-service` in K8S cluster, Goto the root location and run:
e.g
```
kubectl apply -f /card-service/deployment.yml
```
This will create 2 pods for `card-service` in Kubernetes cluster using RollingUpdate strategy. Load balancer service
It will be accessible at `localhost:9091` which will load balance the traffic to these 2 pods running at port `8081`


## CI/CD pipeline Solution
to automate the continuous integration and continuous deployment (CI/CD) in microservice environment:

1. First: you need to added  plugins to build and run docker image using gradle tasks
    ```
    plugins {
        id 'com.palantir.docker' version '0.26.0'
        id 'com.palantir.docker-run' version '0.26.0'
    }
    ```
   after adding the plugins. We should be able to run `:docker` and `:dockerRun` gradle tasks for each microservice
2. I created `Jenkinsfile` in each microservice to build Jenkins pipeline such as build, test, build and push docker image using the Gradle tasks.

## How to run the app
* In root folder of solution
* run
````
docker-compose up -d
````
* After project run in docker you can make request
* (Note) for crul request I use RiqBin extension of chrom, It is amazing
````
curl --location --request POST 'http://localhost:8080/guusto-service/buy-gift' \
--header 'gatewaykey: 343C-ED0B-4137-B27E' \
--header 'Content-Type: application/json' \
--data-raw '{
   
    "userId":1,
    "amount":4,
    "quantity":5
    
}'
````
* To check changing in database of balance-microservice, In browser open H2 console from this url `http=//localhost:8082/h2/`
 ````
Driver Class= org.h2.Driver
JDBC URL= jdbc:h2:mem:guustodb
User Name= root`
Password= password
````
* To check changing in database of card-microservice, In browser open H2 console from this url `http=//localhost:8082/h2/`
````
http=//localhost:8081/h2/
Driver Class= org.h2.Driver
JDBC URL= jdbc:h2:mem:guustodb
User Name= root`
Password= password
````