package hung.com.vertx;


import io.vertx.codegen.annotations.ProxyClose;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@VertxGen // Generate the clients  ở các language khác (cần add Dependency for language in Maven).
@ProxyGen // Generate service proxies
public interface MyDatabaseConnection {

	void insert(JsonObject someData);

	void commit(Handler<AsyncResult<Void>> resultHandler);

	@ProxyClose
	void close();
}