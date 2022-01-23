package core;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Arrays;

public class HttpServer extends NanoHTTPD {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        LOGGER.info("Starting on port {}", port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() == Method.POST) {
            int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
            byte[] buf = new byte[contentLength];
            try {
                session.getInputStream().read(buf, 0, contentLength);
                String body = new String(buf);
                if (body.length() > 0)
                    processBody(body);

                return newFixedLengthResponse("OK");
            } catch (IOException e) {
                LOGGER.error("Error", e);
            }
        } else {
            LOGGER.warn("Invalid method: " + session.getMethod());
        }

        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Error");
    }

    private void processBody(String body) {
        Arrays.stream(body.split("\n"))
                .filter(s -> s.length() > 0)
                .forEach(str -> {
                    JSONObject data = new JSONObject(str);
                    if (data.getString("source").equals("stdout"))
                        System.out.println(data.getString("log"));
                    else
                        System.err.println(data.getString("log"));
                });
    }

}
