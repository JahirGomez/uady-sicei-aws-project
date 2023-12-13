package com.uady.sicei.Services;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uady.sicei.Model.Alumno;
import com.uady.sicei.Model.Request.PreAlumnoInfo;
import com.uady.sicei.Repository.AlumnoRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class AlumnoService {

   @Autowired
   private AlumnoRepository alumnoRepository;
    
   @Autowired
   private S3Client s3Client;

   @Value("${aws.sns.topic}")
   private String topicArn;

   private final SnsClient snsClient;

   private final DynamoDbClient dynamoDbClient;

   public AlumnoService(SnsClient snsClient, DynamoDbClient dynamoDbClient) {
    this.snsClient = snsClient;
    this.dynamoDbClient = dynamoDbClient;
    }

    public List<PreAlumnoInfo> getAlumnos() {
        return (List<PreAlumnoInfo>) alumnoRepository.findAll();      
    }

    public Optional<PreAlumnoInfo> getAlumnoById(int id){
        return alumnoRepository.findById(id);
    }

    public PreAlumnoInfo createAlumno(PreAlumnoInfo alumnoAux){
        if (alumnoAux.getNombres() == null || alumnoAux.getApellidos() == null || alumnoAux.getMatricula() == null || alumnoAux.getPromedio()==0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del alumno son inválidos");
        }
        return alumnoRepository.save(alumnoAux);
    }

    public PreAlumnoInfo actualizar(int id, PreAlumnoInfo alumnoAux){
        if (alumnoAux.getNombres() == null || alumnoAux.getApellidos() == null || alumnoAux.getMatricula() == null || alumnoAux.getPromedio()==0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del alumno son inválidos");
        }
        Optional<PreAlumnoInfo> alumnoExistente= alumnoRepository.findById(id);
        if (alumnoExistente.isPresent()) {
            PreAlumnoInfo alumnoUpdated = alumnoExistente.get();
            alumnoUpdated.setNombres(alumnoAux.getNombres());
            alumnoUpdated.setApellidos(alumnoAux.getApellidos());
            alumnoUpdated.setMatricula(alumnoAux.getMatricula());
            alumnoUpdated.setPromedio(alumnoAux.getPromedio());
            alumnoUpdated.setFotoPerfilUrl(alumnoAux.getFotoPerfilUrl());
            alumnoUpdated.setPassword(alumnoAux.getPassword());
            return alumnoRepository.save(alumnoUpdated);
         }
        return alumnoExistente.orElse(null);
    }

    public boolean deleteAlumno(int id){
        Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);
        if (optionalAlumno.isPresent()) {
            PreAlumnoInfo alumno = optionalAlumno.get();
            alumnoRepository.delete(alumno);
            return true;
        } else {
            return false;
        }
    }

    
   public String uploadPhotoProfile(int id, MultipartFile file) {
      try {
          String key = "photo"+ id + ".jpg";
          PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
                  .bucket("awsprojectmid")
                  .key(key)
                  .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            if (response.sdkHttpResponse().isSuccessful()) {
               String fotoPerfilUrl = s3Client.utilities().getUrl(GetUrlRequest.builder()
                        .bucket("awsprojectmid")
                        .key(key)
                        .build())
                        .toExternalForm();
               Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);
               if (optionalAlumno.isPresent()) {
                  PreAlumnoInfo alumno = optionalAlumno.get();
                  alumno.setFotoPerfilUrl(fotoPerfilUrl);
                  alumnoRepository.save(alumno);
               } else {
                  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno con el id: " + id +" no encontrado");
               }
               return fotoPerfilUrl;
            } else {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir la foto");
            }
          
      } catch (IOException e) {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir la foto", e);
      }
   }

   public void sendNotification(int id) {
      Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);

    if (optionalAlumno.isPresent()) {
        PreAlumnoInfo alumno = optionalAlumno.get();
        
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(alumno.getNombres() + "\n " + alumno.getApellidos() + " \n" + alumno.getPromedio())
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            // Manejar las excepciones específicas de SNS si es necesario
            System.err.println(e.awsErrorDetails().errorMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar notificación SNS", e);
        }
    } else {
        // Manejar el caso en el que no se encuentra el alumno
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado con id: " + id);
    }
   }

   public String login(int id, String password) {
      Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);
      if (optionalAlumno.isPresent()) {
         PreAlumnoInfo alumno = optionalAlumno.get();
         if (alumno.getPassword().equals(password)) {
            StringBuilder sessionStringBuilder = new StringBuilder();
            for (int i = 0; i < 128; i++) {
                int randomDigit = (int) (Math.random() * 10);
                sessionStringBuilder.append(randomDigit);
            }
            String sessionString = sessionStringBuilder.toString();
            long fecha = Instant.now().getEpochSecond();

            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
            itemValues.put("fecha", AttributeValue.builder().n(String.valueOf(fecha)).build());
            itemValues.put("alumnoId", AttributeValue.builder().n(String.valueOf(alumno.getId())).build());
            itemValues.put("active", AttributeValue.builder().bool(true).build());
            itemValues.put("sessionString", AttributeValue.builder().s(sessionString).build());

            try {
                PutItemRequest putItemRequest = PutItemRequest.builder()
                        .tableName("sesiones") 
                        .item(itemValues)
                        .build();

                dynamoDbClient.putItem(putItemRequest);

            } catch (DynamoDbException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al escribir sesión en DynamoDB", e);
            } 

            return sessionString;
         }else{
            System.out.println("Contraseña incorrecta");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña incorrecta");
         }
      } else {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado con id: " + id);
      }
   }  

   public boolean verifySession(int id, String sessionString) {
      Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);
      if (optionalAlumno.isPresent()) {
          PreAlumnoInfo alumno = optionalAlumno.get();
          try {
              ScanRequest scanRequest = ScanRequest.builder()
                      .tableName("sesiones")
                      .build();
  
              ScanResponse response = dynamoDbClient.scan(scanRequest);
              for (Map<String, AttributeValue> item : response.items()) {
                  if (item.get("alumnoId").n().equals(String.valueOf(alumno.getId())) &&
                          item.get("sessionString").s().equals(sessionString) &&
                          item.get("active").bool()) {
                      return true; // Sesión válida encontrada
                  }
              }
              // Si la sesión no es encontrada o no es válida
              return false;
          } catch (DynamoDbException e) {
              e.printStackTrace();
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al buscar sesión en DynamoDB", e);
          }
      } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado con id: " + id);
      }
  }
  

   public void logout(int id, String sessionString) {
      Optional<PreAlumnoInfo> optionalAlumno = alumnoRepository.findById(id);
      if (optionalAlumno.isPresent()) {
         PreAlumnoInfo alumno = optionalAlumno.get();
         try {
            ScanRequest scanRequest = ScanRequest.builder()
                .tableName("sesiones")
                .build();

            ScanResponse response = dynamoDbClient.scan(scanRequest);
            for (Map<String, AttributeValue> item : response.items()) {   
               if (item.get("alumnoId").n().equals(String.valueOf(alumno.getId())) &&
                        item.get("sessionString").s().equals(sessionString) &&
                        item.get("active").bool()) {
                    Map<String, AttributeValue> itemValues = new HashMap<>();
                    itemValues.put("id", AttributeValue.builder().s(item.get("id").s()).build());
                    itemValues.put("fecha", AttributeValue.builder().n(item.get("fecha").n()).build());
                    itemValues.put("alumnoId", AttributeValue.builder().n(item.get("alumnoId").n()).build());
                    itemValues.put("active", AttributeValue.builder().bool(false).build());
                    itemValues.put("sessionString", AttributeValue.builder().s(item.get("sessionString").s()).build());
                    try {
                        PutItemRequest putItemRequest = PutItemRequest.builder()
                                .tableName("sesiones-alumnos") 
                                .item(itemValues)
                                .build();
            
                        dynamoDbClient.putItem(putItemRequest);
                    } catch (DynamoDbException e) {
                        e.printStackTrace();
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al escribir sesión en DynamoDB", e);
                    } 
                }
            }
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al escribir sesión en DynamoDB", e);        
         }
      } else {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado con id: " + id);
      }
   }




}
