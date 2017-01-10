package domfarr.sparkjava.questions.sessionexpired;

import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.HashedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.utils.IOUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.EventListener;
import java.util.UUID;

import static java.lang.String.format;
import static spark.Spark.*;

public class Application {
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");

        before("/", (request, response) -> {
            String requestedSessionId = request.raw().getRequestedSessionId();
            boolean requestedSessionIdValid = request.raw().isRequestedSessionIdValid();
            if (requestedSessionId != null && !requestedSessionIdValid) {
                response.redirect("/expired");
                LOG.info(format("session %s expired", requestedSessionId));
            } else {
                LOG.info(format("session %s active valid %s", requestedSessionId, requestedSessionIdValid));
            }
        });

        get("/", (request, response) -> {
            LOG.info("Root Get");
            if(request.session(false) == null) {
                LOG.info("Session null");
                HttpSession session = request.session().raw();
                session.setMaxInactiveInterval(10*120);
                ((AbstractSession)session).getSessionManager().addEventListener(new HttpSessionListener() {
                    @Override
                    public void sessionCreated(HttpSessionEvent se) {
                        LOG.info("Session CREATED");
                    }

                    @Override
                    public void sessionDestroyed(HttpSessionEvent se) {
                        LOG.info("Session DESTROYED");
                    }
                });
            }
            return IOUtils.toString(Spark.class.getResourceAsStream("/index.html"));
        });

        get("/expired", (request, response) -> IOUtils.toString(Spark.class.getResourceAsStream("/expired/renew-session.html")));

        post("/renew-session", (request, response) -> {
            request.session(true);
            response.cookie("session", UUID.randomUUID().toString(), 120);
            response.redirect("/");
            return null;
        });

        exception(Exception.class, (exception, request, response) -> {
            LOG.error("", exception);
        });
    }
}
