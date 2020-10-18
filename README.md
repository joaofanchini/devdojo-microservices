# Arquitetura simples baseada em Microsserviços

Projeto visando o estudo da arquitetura baseada em Microsserviços para criação de sistemas. 

## Start

* Para inicar os serviços, iniciar primeiro o *Service Discovery (Eureka Server - discovery)* pois todas os serviços
irão tentar realizar uma requisição ao Eureka server buscando se registrar.

* Usuários para login:
    - username: jonny | password: 123 -> Role: USER
    - username: joao | password: 456 -> Role: USER
    - username: john | password: 789 -> Role: ADMIN

## Tecnologias

 - [Lombok](https://projectlombok.org/features/all)
 - [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
 - [Spring Cloud](https://spring.io/projects/spring-cloud)
 - [Spring Validation](https://docs.spring.io/spring-framework/docs/4.1.7.RELEASE/spring-framework-reference/html/validation.html)
 - [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
 - [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.1.17.RELEASE/maven-plugin/)
 - [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
 - [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#using-boot-devtools)
 - [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
 - [Spring Security](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-security)
 - [Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery/)
 - [Routing and Filtering with Netflix Zuul](https://spring.io/guides/gs/routing-and-filtering/)
 
## Padrões utilizados relativo a montagem da arquitetura

 - [Padrões]()



## *Big Picture* sobre a arquitetura

#### General:

![Alt text](resources-to-readme/microsservices_architecture.png?raw=true "General")
fonte: *[DevDojo](https://www.youtube.com/playlist?list=PL62G310vn6nH_iMQoPMhIlK_ey1npyUUl)*

#### Security:
![Alt text](resources-to-readme/microservices_architecture-security.png?raw=true "Security")
fonte: *[DevDojo](https://www.youtube.com/playlist?list=PL62G310vn6nH_iMQoPMhIlK_ey1npyUUl)*


## Serviços

 - Course
 - Service Discovery
 - API Gateway
 - Authentication Service


## Referências:

*A arquitetura de modelo foi baseada de acordo com o curso disponibilizado pelo 
**[DevDojo](http://devdojo.academy/)***
 


