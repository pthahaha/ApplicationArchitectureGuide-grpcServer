package com.kakao.www.applicationarchitectureguidegrpcserver.gRPC;

import com.kakao.www.applicationarchitectureguidegrpcserver.domain.Product;
import com.kakao.www.applicationarchitectureguidegrpcserver.service.ProductService;
import com.kakao.www.product.ProductInfoGrpc;
import com.kakao.www.product.ProductInfoOuterClass;
import io.grpc.Status;
import io.grpc.StatusException;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {

    private Map productMap = new HashMap<String, ProductInfoOuterClass.Product>();

    @Autowired ProductService productService;
    @Autowired Product product;

    @Override
    public void addProduct(ProductInfoOuterClass.Product request,
                           io.grpc.stub.StreamObserver<ProductInfoOuterClass.ProductID> responseObserver) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        request = request.toBuilder().setId(randomUUIDString).build();
        productMap.put(randomUUIDString, request);
        System.out.println(request.getPrice()+"----------->"+request.getDescription());
        System.out.println(request.getName()+"----------->"+request.getDescription());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Instant now = Instant.now();
        product.setCreatedDate(now.atZone(ZoneId.systemDefault()).toLocalDateTime());
        product.setUpdatedDate(now.atZone(ZoneId.systemDefault()).toLocalDateTime());

        productService.createProduct(product);
        ProductInfoOuterClass.ProductID id
                = ProductInfoOuterClass.ProductID.newBuilder().setValue(randomUUIDString).build();
        responseObserver.onNext(id);
        responseObserver.onCompleted();
    }
    @Override
    public void getProduct(ProductInfoOuterClass.ProductID request,
                           io.grpc.stub.StreamObserver<ProductInfoOuterClass.Product> responseObserver) {
        String id = request.getValue();
        System.out.println("id="+id);
        if (productMap.containsKey(id)) {
            responseObserver.onNext((ProductInfoOuterClass.Product) productMap.get(id));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }
}