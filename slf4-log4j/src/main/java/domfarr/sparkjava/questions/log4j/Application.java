package domfarr.sparkjava.questions.log4j;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.get;

public class Application {

    public static void main(String[] args) {
        get("/", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                return IOUtils.toString(Spark.class.getResourceAsStream("/index.html"));
            }
        });
    }
}
