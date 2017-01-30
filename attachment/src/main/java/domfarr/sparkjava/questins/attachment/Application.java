package domfarr.sparkjava.questins.attachment;

import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

import static spark.Spark.get;

public class Application {

    public static void main(String[] args) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        get("/", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                HttpServletResponse httpResponse = response.raw();

                httpResponse.addHeader("Cache-Control", "no-cache");
                httpResponse.setContentType("text/plain");

                httpResponse.addHeader("Content-Disposition", "attachment; filename=\"abc.txt\"");

                ServletOutputStream outputStream = httpResponse.getOutputStream();
                byte[] bytes = "hello".getBytes(Charset.defaultCharset());
                outputStream.write(bytes);

                httpResponse.setStatus(200);
                httpResponse.setContentLength(bytes.length);

                return null;
            }
        });
    }
}
