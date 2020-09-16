package vertx.servicos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import vertx.repositorios.JogoRepositorio;

public class JogoServico extends AbstractVerticle {	
    private JogoRepositorio repositorio = new JogoRepositorio();
    private Router router;
    
    public JogoServico(Router router) {
    	this.router = router;
    }
	
	public void start(Future<Void> future) {
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Meu Primeiro Server com Vertx Funcionando</h1>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/api/jogos*").handler(BodyHandler.create());
		router.post("/api/jogos").handler(repositorio::adicionar);
		router.get("/api/jogos").handler(repositorio::obterTodos);
		router.get("/api/jogos/:id").handler(repositorio::obterPorId);
		router.put("/api/jogos/:id").handler(repositorio::atualizar);
		router.delete("/api/jogos/:id").handler(repositorio::deletar);
	}
}
