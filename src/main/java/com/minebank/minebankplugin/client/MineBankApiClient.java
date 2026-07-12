package com.minebank.minebankplugin.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class MineBankApiClient {
    private final HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";

    public MineBankApiClient() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    //Registro do player
    public CompletableFuture<String> registerPlayer(String uuid, String nickName) {
        //Text Block - monta exatamente como está, sem precisar quebra de linhas
        String json = """
                {
                    "minecraftUuid": "%s",
                    "nickname": "%s"
                }
                """.formatted(uuid, nickName);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/players"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        //Nao deixa esperando, retorna apenas uma string quando terminar
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
                thenApply(HttpResponse::body);
    }

    //Busca saldo
    public CompletableFuture<String> getSaldo(String uuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/wallets/player/" + uuid + "/balance"))
                .GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    //Busca mercado
    public CompletableFuture<String> getMarket() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/market/items"))
                .GET().build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
