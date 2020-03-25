package hung.com.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.serviceproxy.ServiceProxyBuilder;

/**
 * các ví dụ về cách dùng Vertx-service-proxy để classload Implement Class đc gen bởi vertx-codegen => xem pom.xml
 * vertx-codegen sẽ gen implement Class dùng Eventbus (ko phải database, http,...).
 * Phần ClassLoad ko hay bằng java SPI (Service Provider Interface).
 * 
 * Mục đích project này: để hiểu Vertx-service-proxy vận hành thế nào => nhằm đọc hiểu source code của Vertx sample.
 */
public class Examples {

	/**
	 * vd về tạo request/response eventbus => ko dùng vertx-service-proxy
	 */
	public void example1(Vertx vertx) {
		// Assume database service is already deployed somewhere....
		// Save some data in the database
		JsonObject messageBody = new JsonObject();
		messageBody.put("collection", "mycollection")
		.put("document", new JsonObject().put("name", "tim"));

		/**
		 * Message = Header + body
		 */
		DeliveryOptions options = new DeliveryOptions().addHeader("action", "save"); // add Header like http header

		// request gửi đi có response asynchronous Handler
		vertx.eventBus().request("database-service-address", messageBody, options, new Handler<AsyncResult<Message<JsonObject>>>() {

			@Override
			public void handle(AsyncResult<Message<JsonObject>> event) {
				if(event.succeeded()){
					Message message = event.result();
					JsonObject body = (JsonObject) message.body();
				}else{ // event.failed()

				}		
			}

		});
	}

	/**
	 * vd về lấy Implement Class của Interface từ ClassLoad bằng Vertx-Service-proxy lib  => xem pom.xml
	 */
	public AccountService proxyCreation(Vertx vertx, DeliveryOptions options) {
		ServiceProxyBuilder builder = new ServiceProxyBuilder(vertx).setAddress("database-service-address");
		
		/**
		 * Get Implement Class từ java ClassLoad tương ứng với MyDatabaseConnection at runtime
		 * => rất giống với Java SPI (Service Provider Interface)
		 */
		AccountService service = builder.build(AccountService.class);
		// or with delivery options:
//		MyDatabaseConnection service2 = builder.setOptions(options).build(MyDatabaseConnection.class);
		
		return service;
	}

	/**
	 * vd dùng lib của Vertx-Service-proxy đăng ký 1 consumer trên eventbus tương ứng với MyDatabaseConnection  => xem pom.xml
	 */
	public void register(Vertx vertx, DeliveryOptions options) {
		// Create an instance of your service implementation
		AccountService service = proxyCreation(vertx, options);
		
		/**
		 * Consumer khi nhận đc Message sẽ kích hoạt Handler mà Vertx-Service-proxy gen tự động tương ứng với MyDatabaseConnection.class
		 */
		MessageConsumer<JsonObject> consumer = new ServiceBinder(vertx)
				.setAddress("database-service-address")				// address on Eventbus
				.register(AccountService.class, service);   // tạo một comsumer với address = "database-service-address" tren eventbus
	}

	public void unregister(Vertx vertx, DeliveryOptions options) {
		ServiceBinder binder = new ServiceBinder(vertx);

		// Create an instance of your service implementation
		AccountService service = proxyCreation(vertx, options);

		/**
		 * Consumer khi nhận đc Message sẽ kích hoạt Handler mà Vertx-Service-proxy gen tự động tương ứng với MyDatabaseConnection.class
		 */
		MessageConsumer<JsonObject> consumer = binder
				.setAddress("database-service-address")          // address on Eventbus
				.register(AccountService.class, service);  // tạo một comsumer với address = "database-service-address" tren eventbus

		// ....

		//================ Unregister consumer tren EventBus =================
		binder.unregister(consumer);
	}


}
