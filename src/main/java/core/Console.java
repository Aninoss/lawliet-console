package core;

import core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

    private final static Logger LOGGER = LoggerFactory.getLogger(Console.class);

    public Console() {
        new Thread(this::run).start();
    }

    private void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                if (br.ready()) {
                    String line = br.readLine();
                    if (line.length() > 0) {
                        try {
                            processInput(line);
                        } catch (Throwable e) {
                            LOGGER.error("Error", e);
                        }
                    }
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                LOGGER.error("Unexpected console exception", e);
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

        SendEvent.sendCommand(clusterId, readLine)
                .exceptionally(ExceptionLogger.get());
    }

}
