package com.example.eCommBackendNew2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JsonPointer;

public interface PaymentInt {
public void doPayment();
}

public class PaymentService{
    @Autowired
    public PaymentService(){
    }
}

@RestController
@RequestMapping
public class PaymentController(){
    PaymentService paymentService
}

