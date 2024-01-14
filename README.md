# gRPC server code
## 이 프로젝트는 gRPC 서버 소스 입니다.
- server쪽 소스는 gRPC lib + proto buf source를 사용하며 실질적인 로직을 구현하는 곳 입니다.
- MVC패턴으로 비교해보면 로직이 없는 컨트롤러부분이 stub code 로직이 있는 서버쪽이 service라고 보시면 됩니다.
- 또한 jpa가 연동된 프로젝트 입니다.

## Server 기동 방법 2가지
- ServerBuilder를 통해 서버 기동
  - springboot의 기능이 제대로 작동하지 않는 단점이 있습니다.
  - 학습용으로 체크만 해주시면 될것 같습니다.
 
```  
package com.kakao.www.applicationarchitectureguidegrpcserver;
 
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.logging.Logger;
 
public class ProductInfoServer {
    private static final Logger logger = Logger.getLogger(ApplicationArchitectureGuideGrpcServerApplication.class.getName());
 
    private Server server;
 
    public void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new ProductInfoImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            logger.info("*** shutting down gRPC server since JVM is shutting down");
            ProductInfoServer.this.stop();
            logger.info("*** server shut down");
        }));
    }
 
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
 
    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
 
 
}
```
- springboot를 통해 gRPC 서버 기동
  - dependency를 추가해야 합니다.
```
// https://mvnrepository.com/artifact/net.devh/grpc-server-spring-boot-starter
implementation 'net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE'
  ```
 - application.yaml에 gRPC 포트 추가
```
grpc:
  server:
    port: 50051
```
 - 구현부에 @GrpcService 추가 합니다.
```
@GrpcService
public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase { ... }   

```
  - springboot server application을 기동하면 gRPC server도 함께 기동 됩니다.
```
Tomcat started on port 8080 (http) with context path ''
2024-01-14T16:18:55.372+09:00  INFO 68457 --- [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: com.kakao.www.product.ProductInfo, bean: productInfoImpl, class: com.kakao.www.applicationarchitectureguidegrpcserver.gRPC.ProductInfoImpl
2024-01-14T16:18:55.372+09:00  INFO 68457 --- [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: grpc.health.v1.Health, bean: grpcHealthService, class: io.grpc.protobuf.services.HealthServiceImpl
2024-01-14T16:18:55.372+09:00  INFO 68457 --- [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: grpc.reflection.v1alpha.ServerReflection, bean: protoReflectionService, class: io.grpc.protobuf.services.ProtoReflectionService
2024-01-14T16:18:55.420+09:00  INFO 68457 --- [  restartedMain] n.d.b.g.s.s.GrpcServerLifecycle          : gRPC Server started, listening on address: *, port: 50051
2024-01-14T16:18:55.425+09:00  INFO 68457 --- [  restartedMain] onArchitectureGuideGrpcServerApplication : Started ApplicationArchitectureGuideGrpcServerApplication in 2.073 seconds (process running for 7.458)

```

## JPA 연동
우선 build.gradle에 spring data jpa dependency를 추가 합니다.
```
// spring-data-jpa
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

application.yaml에 아래의 정보를 넣어줍니다.
```
spring:
  datasource:
    username: root
    url: jdbc:mysql://localhost:3306/my_database
    password: 1025

  jpa:
    hibernate:
      ddl-auto: create
      show-sql: true
```

### mySQL 연동
- docker pull mysql 명령어로 최신버전의 mysql을 pull 합니다.
- docker images로 mysql 이미지를 확인 합니다.
- image가 있으니 이제 컨테이너를 바로 띄워보겠습니다.
```
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=1025 -d -p 3306:3306 mysql:latest
```
- docker ps -a로 컨테이너 리스트를 출력 해봅니다.

#### MySQL Docker 컨테이너 시작/중지/재시작

```
컨테이너 중지 : docker stop mysql-container
컨테이너 시작 :  docker start mysql-container
컨테이너 재시작 : docker restart mysql-container
```

#### MySQL Docker 컨테이너 접속
```
docker exec -it mysql-container bash
```

- mysql -u root -p 로 mysql에 접속합니다.
  - show databases;
  - CREATE DATABASE my_database default CHARCTER SET UTF8;

DB셋팅 후 다시 실행하면 아래와 같이 오류가 납니다.
```
Factory method 'dataSource' threw exception with message: Failed to load driver class
com.mysql.cj.jdbc.Driver in either of HikariConfig class loader or Thread context classloader
```
mysql driver가 load되지 않아서 fail되었습니다.

build.gradle에  mysql-connector-java를 추가 해줍니다.
```
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
implementation 'mysql:mysql-connector-java:8.0.33'
```
그리고나서 스프링부트를 실행시키면 아래와 같이 잘 동작하게 됩니다.


### gRPC server 구현부 → service → repository → mysql
- (ProductInfoImpl)   (ProductService)  (ProductRepository) 
- Entity : Product.java
