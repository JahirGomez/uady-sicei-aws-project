package com.uady.sicei.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.regions.Region;


@Configuration
public class OpenAPIConfig {
   @Value("${aws.accessKeyId}")
   private String accessKeyId;

   @Value("${aws.secretKey}")
   private String secretKey;

   @Value("${aws.region}")
   private Region region;

   @Value("${aws.sessionToken}")
   private String sessionToken;

   @Bean
   public S3Client s3Client() {
      AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey, sessionToken);
      return S3Client.builder()
               .region(region) 
               .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
               .build();
   }

   @Bean
   public SnsClient snsClient() {
      AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey, sessionToken);
      return SnsClient.builder()
               .region(region)
               .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
               .build();
   }

   @Bean
    public DynamoDbClient dynamoDbClient() {
      AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey, sessionToken);
        return DynamoDbClient.builder()
                .region(region)
               .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .build();
    }
}
