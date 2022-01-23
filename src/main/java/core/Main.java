package core;

import syncserver.SyncManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            if (System.getenv("SYNC_HOST") != null) {
                new Console();
                SyncManager.getInstance().start();
            }
            new HttpServer(80);
        } catch (Throwable e) {
            LOGGER.error("Could not start up!", e);
        }
    }

}
