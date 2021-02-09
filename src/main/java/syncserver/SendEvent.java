package syncserver;

import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;

public class SendEvent {

    private SendEvent() {
    }

    public static CompletableFuture<JSONObject> sendCommand(int clusterId, String command) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("cluster_id", clusterId);
        dataJson.put("command", command);

        return SyncManager.getInstance().getClient().send("CMD", dataJson);
    }

}
