package com.malekkamoua.meet5.fraudservice.constant;

public class AppConstant {

    //Kafka constants
    public static final String FRAUD= "fraud-topic";
    public static final String USER_INTERACTIONS = "user-interactions-topic";
    public static  final String LIKE_FRAUD =  "Excessive likes";
    public static  final String VISIT_FRAUD =  "Excessive visits";
    public static  final String FRAUD_CONS_GROUP =  "my-fraud-consumer-group";

    //Fraud constants
    public static final int MAX_INTERACTIONS = 1;
    public static final int TIME_IN_MINUTES = 10;
}
