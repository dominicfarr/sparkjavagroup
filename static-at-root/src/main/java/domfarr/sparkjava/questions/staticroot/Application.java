package domfarr.sparkjava.questions.staticroot;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class Application {

    public static void main(String[] args) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        staticFiles.location("/");
        get("/", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                return IOUtils.toString(Spark.class.getResourceAsStream("/index.html"));
            }
        });
    }
}
