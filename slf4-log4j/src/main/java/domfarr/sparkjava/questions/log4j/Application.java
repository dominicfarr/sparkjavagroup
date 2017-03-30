package domfarr.sparkjava.questions.log4j;

import spark.*;
import spark.utils.IOUtils;

import static spark.Spark.exception;
import static spark.Spark.get;

public class Application {

    public static void main(String[] args) {
        get("/", (request, response) -> IOUtils.toString(Spark.class.getResourceAsStream("/index.html")));

        get("/client-error", (request, response) -> {
            throw new RuntimeException("Forced RuntimeException");
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body("This handler catches all exceptions.");
        });
    }
}
