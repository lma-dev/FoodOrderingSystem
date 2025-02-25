JDK22

Every Services Must Have
```
eureka.instance.hostname=localhost
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

The Below is a Must
```
eureka.client.register-with-eureka=true
```
Dont Forgot to Update

```
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/auth_db
spring.datasource.username=root
spring.datasource.password=
````

and 
Run Service-registry
Run All Services
And Last
Run ApiGateway


To Run via Maven
```
mvn spring-boot:run
```

In VSCode Install Java Extensions and Spring Extensions and CLick On Play Button

Check Status With Eureka
```
http://127.0.0.1:8761/
```
