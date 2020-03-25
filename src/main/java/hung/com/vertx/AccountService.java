package hung.com.vertx;


import java.util.List;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyClose;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;


@VertxGen // Generate the clients  ở các language khác (cần add Dependency for language in Maven).
@ProxyGen // Generate service proxies
public interface AccountService {

  /**
   * The name of the event bus service.
   */
  String SERVICE_NAME = "user-account-eb-service";

  /**
   * The address on which the service is published.
   */
  String SERVICE_ADDRESS = "service.user.account";

  /**
   * dùng java reflextion để lấy tên function
   */
  @Fluent
  AccountService initializePersistence(Handler<AsyncResult<Void>> resultHandler);

  /**
   * dùng java reflextion để lấy tên function và biến account
   */
  @Fluent
  AccountService addAccount(Account account, Handler<AsyncResult<Void>> resultHandler);

  /**
   * dùng java reflextion để lấy tên function và tên biến account
   */
  @Fluent
  AccountService retrieveAccount(String id, Handler<AsyncResult<Account>> resultHandler);


  @Fluent
  AccountService retrieveByUsername(String username, Handler<AsyncResult<Account>> resultHandler);


  @Fluent
  AccountService retrieveAllAccounts(Handler<AsyncResult<List<Account>>> resultHandler);


  @Fluent
  AccountService updateAccount(Account account, Handler<AsyncResult<Account>> resultHandler);


  @Fluent
  AccountService deleteAccount(String id, Handler<AsyncResult<Void>> resultHandler);


  @Fluent
  AccountService deleteAllAccounts(Handler<AsyncResult<Void>> resultHandler);

}
