package core;

import syncserver.SyncManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            new Console();
            new HttpServer(80);
            SyncManager.getInstance().start();
        } catch (Throwable e) {
            LOGGER.error("Could not start up!", e);
        }
    }

}
