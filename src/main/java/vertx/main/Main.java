package vertx.main;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import vertx.servicos.JogoServico;

public class Main {
	private static int PORTA = Integer.parseInt(System.getenv().getOrDefault("PORT", "9000"));
    private static final Logger LOGGER = Logger.getLogger("Main");
    private static final Vertx VERTX = Vertx.vertx();
    private static final HttpServer HTTP_SERVER = VERTX.createHttpServer();
    private static final Router ROOT = Router.router(VERTX);
       
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Starting microservice above Vert.x");
        LOGGER.log(Level.INFO, "Listening on {0}", PORTA);
        
        HTTP_SERVER.requestHandler(ROOT).listen(PORTA);

        LOGGER.log(Level.INFO, "Configuring JSON serializer/deserializer");
        DatabindCodec.mapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
           	    		
        //Levantando as APIs
        VERTX.deployVerticle(new JogoServico(ROOT));
	}
}
