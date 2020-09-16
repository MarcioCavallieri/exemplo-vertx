package vertx.repositorios;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vertx.entidades.Jogo;

public class JogoRepositorio {
	private static final String CONTENT_TYPE = "content-type";
	private static final String CONTENT = "application/json; charset=utf-8";
	private static final AtomicInteger CONTADOR = new AtomicInteger();
	private Map<Long, Jogo> jogos = new TreeMap<>();

	private void mockJogos() {
		Jogo jogo1 = new Jogo(CONTADOR.getAndIncrement(), "The Last of Us", "Suspense", "PS4");
		Jogo jogo2 = new Jogo(CONTADOR.getAndIncrement(), "Horizon Zero Down", "Aventura", "PS4");
		Jogo jogo3 = new Jogo(CONTADOR.getAndIncrement(), "Devil May Cry", "Ação", "XBOX One");
		Jogo jogo4 = new Jogo(CONTADOR.getAndIncrement(), "God of War", "Aventura", "PS4");
		jogos.put(jogo1.getId(), jogo1);
		jogos.put(jogo2.getId(), jogo2);
		jogos.put(jogo3.getId(), jogo3);
		jogos.put(jogo4.getId(), jogo4);
	}

	public JogoRepositorio() {
		mockJogos();
	};

	public void adicionar(RoutingContext routingContext) {
		 Jogo jogo = Json.decodeValue(routingContext.getBodyAsString(), Jogo.class);
		 jogo.setId(CONTADOR.getAndIncrement());
		 jogos.put(jogo.getId(), jogo);

		routingContext.response().setStatusCode(201).putHeader(CONTENT_TYPE, CONTENT)
				.end(Json.encodePrettily(jogo));
	}

	public void obterTodos(RoutingContext routingContext) {
		routingContext.response().putHeader(CONTENT_TYPE, CONTENT)
				.end(Json.encodePrettily(jogos.values()));
	}

	public void obterPorId(RoutingContext routingContext) {
		 String id = routingContext.request().getParam("id");

		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Long idInteiro = Long.valueOf(id);
			Jogo jogo = jogos.get(idInteiro);
			
			if (jogo == null) {
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response().putHeader(CONTENT_TYPE, CONTENT)
						.end(Json.encodePrettily(jogo));
			}
		}
	}

	public void atualizar(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		JsonObject json = routingContext.getBodyAsJson();

		if (id == null || json == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Long idInteiro = Long.valueOf(id);
			Jogo jogo = jogos.get(idInteiro);
			
			if (jogo == null) {
				routingContext.response().setStatusCode(404).end();
			} else {
				jogo.setNome(json.getString("nome"));
				jogo.setGenero(json.getString("genero"));
				jogo.setPlataforma(json.getString("plataforma"));
				routingContext.response().putHeader(CONTENT_TYPE, CONTENT)
						.end(Json.encodePrettily(jogo));
			}
		}
	}

	public void deletar(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");

		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Long idInteiro = Long.valueOf(id);
			jogos.remove(idInteiro);
		}
		
		routingContext.response().setStatusCode(204).end();
	}
}
