package core;

import core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class Console {

    private final static Logger LOGGER = LoggerFactory.getLogger(Console.class);

    private final SyncApi syncApi = new SyncApi();

    public Console() {
        new Thread(this::run).start();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.length() > 0) {
                    try {
                        processInput(line);
                    } catch (Throwable e) {
                        LOGGER.error("Error", e);
                    }
                }
            }
        }
    }

    private void processInput(String readLine) {
        String first = readLine.split(" ")[0];
        int clusterId = -1;
        if (StringUtil.stringIsInt(first)) {
            clusterId = Integer.parseInt(first);
            readLine = readLine.substring(first.length() + 1);
        }

        syncApi.sendCommand(clusterId, readLine)
                .exceptionally(ExceptionLogger.get());
    }

}
