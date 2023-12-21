package com.kakao.www.applicationarchitectureguidegrpcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class ApplicationArchitectureGuideGrpcServerApplication {
    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ApplicationArchitectureGuideGrpcServerApplication.class, args);
        final ProductInfoServer server = new ProductInfoServer();
        server.start();
        server.blockUntilShutdown();

    }

}
