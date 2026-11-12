package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameData;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private String serverURL;
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private String authToken;
    private List<GameData> gameData;

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

    public String login(String username, String password) throws Exception{
        String urlString = serverURL + "/session";
        var body = Map.of("username", username, "password", password);
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

    public void logout() throws Exception{
        String urlString = serverURL + "/session";
        var json = new Gson().toJson(Map.of());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        if(response.statusCode() == 200){
            this.authToken = null;
        }
        else{
            throw new Exception(responseBody.get("message").toString());
        }
    }

    public int createGame(String gameName) throws Exception{
        String urlString = serverURL + "/game";
        var body = Map.of("gameName", gameName);
        var json = new Gson().toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        if(response.statusCode() == 200){
            return Integer.parseInt(responseBody.get("gameID").toString());
        }
        else{
            throw new Exception(responseBody.get("message").toString());
        }
    }

    public List<GameData> listGames() throws Exception{
        String urlString = serverURL + "/game";
        var body = Map.of();
        var json = new Gson().toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        if(response.statusCode() == 200){
            Type listType = new TypeToken<List<GameData>>(){}.getType();
            gameData = new Gson().fromJson(responseBody.get("games").toString(), listType);
            return gameData;
        }
        else{
            throw new Exception(responseBody.get("message").toString());
        }
    }

    public void join(ChessGame.TeamColor color, int id) throws Exception{
        String urlString = serverURL + "/game";
        var body = Map.of("playerColor", color, "gameID", id);
        var json = new Gson().toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        if(response.statusCode() == 200){
            //websocket!
        }
        else{
            throw new Exception(responseBody.get("message").toString());
        }
    }

}
