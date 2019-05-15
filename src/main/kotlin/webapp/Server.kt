package webapp

import webapp.model.Report
import webapp.dal.Reports
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

class Server : CoroutineVerticle() {

    override suspend fun start() {
        Json.mapper.registerModule(KotlinModule())

        Database.connect(
            url = "jdbc:postgresql://localhost:5432/robtay01",
            user = "robtay01",
            driver = "org.postgresql.Driver"
        )

        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create());
        router.get("/hello").handler({ ctx -> 
            ctx.response().end("Hello")
        })
        router.post("/reports").handler(this::handleAddReport)
    
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }
    
    fun handleAddReport(ctx: RoutingContext) {        
        val reportJson = ctx.bodyAsJson        
        val report = reportJson.mapTo(Report::class.java)
        transaction {
            Reports.insert { Reports.of(report, it) }
        }
        ctx.response()
            .putHeader("content-type", "application/json")
            .end(JsonObject.mapFrom(report).encode())
    }
}