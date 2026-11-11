package client;

import com.google.gson.Gson;
import exceptions.AlreadyTakenException;
import exceptions.BadArgumentException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {

    private String serverURL;
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private String authToken;

    public ServerFacade(String serverURL){
        this.serverURL = serverURL;
    }

    public void clear() throws Exception{ //should throw an exception
        String urlString = serverURL + "/db";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String register(String username, String password, String email) throws Exception{
        String urlString = serverURL + "/user";
        var body = Map.of("username", username, "password", password, "email", email);
        var json = new Gson().toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        if(response.statusCode() == 200){
            this.authToken = responseBody.get("authToken").toString();
            return responseBody.get("username").toString();
        }
        else{
            throw new Exception(responseBody.get("message").toString());
        }
    }

}
