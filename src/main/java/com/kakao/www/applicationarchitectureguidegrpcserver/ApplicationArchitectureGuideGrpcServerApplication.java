package com.kakao.www.applicationarchitectureguidegrpcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class ApplicationArchitectureGuideGrpcServerApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
       SpringApplication.run(ApplicationArchitectureGuideGrpcServerApplication.class, args);
       // jpa관련 Autowired di가 안돼서 변경 (serverbuilder->springboot)
       // final ProductInfoServer server = new ProductInfoServer();
       // server.start();
       // server.blockUntilShutdown();
    }

}
