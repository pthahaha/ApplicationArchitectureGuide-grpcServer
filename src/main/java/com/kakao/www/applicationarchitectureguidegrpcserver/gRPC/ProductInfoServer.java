//package com.kakao.www.applicationarchitectureguidegrpcserver.gRPC;
//
//import com.kakao.www.applicationarchitectureguidegrpcserver.ApplicationArchitectureGuideGrpcServerApplication;
//import com.kakao.www.applicationarchitectureguidegrpcserver.service.ProductService;
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.logging.Logger;
//
//@Component
//public class ProductInfoServer {
//    private static final Logger logger = Logger.getLogger(ApplicationArchitectureGuideGrpcServerApplication.class.getName());
//
//    @Autowired
//    private Server grpcServer;
//
//    @Bean
//    public Server grpcServer() {
//        int port = 50051;
//        Server server = ServerBuilder.forPort(port)
//                .addService(new ProductInfoImpl())
//                .build();
//        return server;
//    }
//
////    public void start() throws IOException {
////        /* The port on which the server should run */
////        int port = 50051;
////        server = ServerBuilder.forPort(port)
////                .addService(new ProductInfoImpl())
////                .build()
////                .start();
////        logger.info("Server started, listening on " + port);
////        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
////            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
////            logger.info("*** shutting down gRPC server since JVM is shutting down");
////            ProductInfoServer.this.stop();
////            logger.info("*** server shut down");
////        }));
////    }
//
//    public void stop() {
//        if (grpcServer != null) {
//            grpcServer.shutdown();
//        }
//    }
//
//    /**
//     * Await termination on the main thread since the grpc library uses daemon threads.
//     */
//    public void blockUntilShutdown() throws InterruptedException {
//        if (grpcServer != null) {
//            grpcServer.awaitTermination();
//        }
//    }
//
//
//}
//
