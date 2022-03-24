package core;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class SyncApi {

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public CompletableFuture<Void> sendCommand(int clusterId, String command) {
        String url = "http://" + System.getenv("SYNC_HOST") + ":" + System.getenv("SYNC_PORT") + "/api/CMD";

        JSONObject requestJson = new JSONObject();
        requestJson.put("cluster_id", clusterId);
        requestJson.put("command", command);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestJson.toString(), MediaType.get("application/json")))
                .addHeader("Authorization", System.getenv("SYNC_AUTH"))
                .build();

        CompletableFuture<Void> future = new CompletableFuture<>();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                future.complete(null);
            }
        });

        return future;
    }

}
