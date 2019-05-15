package webapp

// import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.json.JsonArray
// import io.vertx.ext.web.Router
// import io.vertx.ext.web.RoutingContext
// import io.vertx.ext.web.handler.BodyHandler
// import io.vertx.ext.jdbc.JDBCClient
// import io.vertx.ext.sql.SQLConnection
// import io.vertx.ext.sql.ResultSet
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection
import io.vertx.reactivex.ext.sql.SQLClientHelper;


class Server : AbstractVerticle() {
    private lateinit var client: JDBCClient

    override fun start() {
        val config = JsonObject()
            .put("url", "jdbc:postgresql://localhost:5432/robtay01")
            .put("user", "robtay01")
            .put("driver_class", "org.postgresql.Driver")
        client = JDBCClient.createShared(vertx, config)

        var router = Router.router(vertx)
        router.route().handler(BodyHandler.create());
        router.get("/hello").handler({ ctx -> 
            ctx.response().end("Hello")
        })
        router.post("/reports").handler(this::handleAddReport)

        var server = vertx.createHttpServer()
        server.requestHandler(router).listen(8080)
    }
    
    private fun handleAddReport(ctx: RoutingContext) {
        client.rxGetConnection().flatMap({ conn ->
            // var rs = conn.rxUpdate("INSERT INTO some_table (user_id, name) VALUES (DEFAULT, steev)")
            // return rs.doAfterTerminate(conn.close())
        })
        // client.getConnection({ conn ->
        //     if (conn.succeeded()) {
        //         var stmt = "INSERT INTO some_table (user_id, name) VALUES (DEFAULT, ?)"
        //         var payload = ctx.getBodyAsJson()
        //         var connection = conn.result()
        //         connection.updateWithParams(stmt, 
        //             JsonArray().add(payload.getString("name")), 
        //             { result ->
        //                 if (result.succeeded()) {
        //                     connection.close()
        //                     ctx.response().end()
        //                 } else {
        //                     connection.close()
        //                     ctx.response().end()                        
        //                 }
        //             })
        //     } else {
        //         ctx.response().end()
        //     }
        // })
    }
}