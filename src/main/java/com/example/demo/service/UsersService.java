package com.example.demo.service;
import com.example.demo.model.ActivateBeneficiary;
import com.example.demo.model.BSFAccountResponse;
import com.example.demo.model.CreateBSFBen;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {

    private String accessToken;
    private boolean login;
    private String serviceAgreementId;
    private String userContextResponse;
    private String userContext;
    private String Account;
    private String beneficiaryName;
    private String accountNumber;
    private String beneficiarySequence;

    //This method will be called when the user enter his username and password
    public ResponseEntity<?> login(Map<String, String> credentials){
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }
        //Method call to retrieve the access token from Backbase
        accessToken = retrieveAccessToken(username, password);
        if (accessToken == null) {
            login = false;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to retrieve access token");
        }else{login = true;}

        //Method call to retrieve the service agreement ID from Backbase
        serviceAgreementId = serviceAgreement();//pass the access token
        if (serviceAgreementId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to retrieve Service Agreement Id");
        }
        //Method call to retrieve response from the user context in Backbase
        userContextResponse = userContext();//pass the access token and service agreemant id

        //To check if the method has been entered or not
        System.out.println(userContextResponse);

         if(login){ //Boolean login (True , False)
             // To Represent the body if the request
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Successful login");
            responseBody.put("accessToken", accessToken);
            responseBody.put("Service Agreement Id", serviceAgreementId);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
            }
    }

    //Method call to Backbase access token API
    private String retrieveAccessToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = String.format("username=%s&password=%s&client_id=bb-tooling-client&grant_type=password", username, password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    "https://identity.u.omni-fm.alfransi.com.sa/auth/realms/retail/protocol/openid-connect/token",
                    HttpMethod.POST,
                    request,
                    Map.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //Check the response of the request
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                return (String) responseBody.get("access_token");//return the access token
            }
        }
        return null;
    }

    //Method call to Backbase service Agreement API to retrieve service Agreement Id
    private String serviceAgreement() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/access-control/client-api/v3/accessgroups/user-context/service-agreements?from=0&size=7",
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> responseBody = response.getBody();
            if (responseBody != null) {
                return (String) responseBody.get(0).get("id");
            }
        }

        return null;
    }

    //Method call to Backbase User Context API to retrieve the response of the request if 204 then success
    private String userContext() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String body = String.format("{\"serviceAgreementId\":\"%s\"}", serviceAgreementId);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/access-control/client-api/v2/accessgroups/usercontext",
                    HttpMethod.POST,
                    request,
                    Map.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            HttpHeaders responseHeaders = response.getHeaders();
            List<String> cookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
            if (cookies != null && !cookies.isEmpty()) {
                userContext = cookies.get(0).split(";")[0];
            }
            return "User context Success";
        }
        return null;
    }

    //Method call to Backbase Product List API to retrieve all the current accounts information
    public Object getProductsList() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.add(HttpHeaders.COOKIE, userContext); // set the user context in the cookie
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/arrangement-manager/client-api/v2/productsummary",
                    HttpMethod.GET,
                    request,
                    Object.class
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.out.println("Unauthorized access - possibly due to incorrect or expired token.");
                System.out.println("Response Body: " + e.getResponseBodyAsString());
                System.out.println("Response Headers: " + e.getResponseHeaders());
            } else {
                e.printStackTrace();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            return response.getBody();
        }

        return null;
    }

    public Object getTransactionsList() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/transaction-manager/client-api/v2/transactions",
                    HttpMethod.GET,
                    request,
                    Object.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public Object getBeneficiaries() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/custom-beneficiary-manager/client-api/v3/beneficiary-manager/beneficiaries?active=true",
                    HttpMethod.GET,
                    request,
                    Object.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public BSFAccountResponse validate_BSF_Account(Map<String, String> account) {
        Account = account.get("account");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String body = String.format("{\"account\":\"%s\"}", Account);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<BSFAccountResponse> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/custom-beneficiary-manager/client-api/v1/validate/account/bsf",
                    HttpMethod.POST,
                    request,
                    BSFAccountResponse.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            accountNumber = response.getBody().getAccountNumber();
            beneficiaryName = response.getBody().getEnglishName();
            System.out.println(accountNumber+" "+beneficiaryName);
            return response.getBody();
        }
        return null;
    }

    public CreateBSFBen create_BSF_Beneficiary() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String body = String.format("{\"beneficiaryName\":\"%s\",\"accountNumber\":\"%s\"}", beneficiaryName,accountNumber);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<CreateBSFBen> response;
        try {
            response = restTemplate.exchange(
                    "https://edge.u.omni-fm.alfransi.com.sa/api/custom-beneficiary-manager/client-api/v1/beneficiary-manager/bsf-beneficiaries",
                    HttpMethod.POST,
                    request,
                    CreateBSFBen.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            beneficiarySequence = response.getBody().getBeneficiarySequence();
            return response.getBody();
        }
        return null;
    }

    public ActivateBeneficiary bsfactivateBeneficiary() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = String.format("https://edge.u.omni-fm.alfransi.com.sa/api/custom-beneficiary-manager/client-api/v1/beneficiary-manager/beneficiaries/%s/mobile-client/activate", beneficiarySequence);

        ResponseEntity<ActivateBeneficiary> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    ActivateBeneficiary.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }


    public String deleteBeneficiary(Map<String, String> BenSequence) {
        String BenSequence2 = BenSequence.get("BenfSequence");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = String.format("https://edge.u.omni-fm.alfransi.com.sa/api/custom-beneficiary-manager/client-api/v1/beneficiary-manager/beneficiaries/%s",BenSequence2);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "user not deleted";
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return "User deleted successfully";
        }
        return null;
    }

    









}
