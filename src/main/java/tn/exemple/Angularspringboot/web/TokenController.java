package tn.exemple.Angularspringboot.web;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // autorise Angular en local
public class TokenController {

    private static final String SCOPE = "https://management.azure.com/.default";

    // ✅ Crée une classe interne pour recevoir les credentials depuis Angular
    public static class AzureCredentials {
        public String tenantId;
        public String clientId;
        public String clientSecret;
        public String subscriptionId; // Peut être ignoré ici mais utile pour Angular
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getAzureAccessToken(@RequestBody AzureCredentials creds) {
        String tokenUrl = "https://login.microsoftonline.com/" + creds.tenantId + "/oauth2/v2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("client_id", creds.clientId);
        body.put("client_secret", creds.clientSecret);
        body.put("scope", SCOPE);

        StringBuilder formBody = new StringBuilder();
        for (Map.Entry<String, String> entry : body.entrySet()) {
            formBody.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        formBody.setLength(formBody.length() - 1); // remove trailing &

        HttpEntity<String> request = new HttpEntity<>(formBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> tokenInfo = new HashMap<>();
                tokenInfo.put("access_token", response.getBody().get("access_token"));
                tokenInfo.put("expires_in", response.getBody().get("expires_in"));
                tokenInfo.put("subscription_id", creds.subscriptionId); // On retourne aussi l’ID pour l’UI
                return ResponseEntity.ok(tokenInfo);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid credentials or tenant"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to retrieve token: " + e.getMessage()));
        }
    }
}
