# microservices-course



## h2
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb
user: sa
password:

## docker-confluent

### Run:

Add lines
>      127.0.0.1       kafka_broker_1
>      127.0.0.1       kafka_broker_2
>      127.0.0.1       kafka_broker_3
to /etc/hosts

> cd .../microservices-course
> docker-compose up -d

### Verify:
> docker-compose ps
### Stop:
> docker-compose down  --remove-orphans



## Ref

### Kafka client examples
https://github.com/confluentinc/examples/tree/3.3.0-post/kafka-clients

https://github.com/sdaschner/scalable-coffee-shop

http://www.baeldung.com/spring-vertx

http://danielwhittaker.me/

https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
