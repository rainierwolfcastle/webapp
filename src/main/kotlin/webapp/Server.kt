package webapp

import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.launch
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.dao.IntIdTable

data class Report(
    val name: String
)

object Reports : IntIdTable() {
    val name = text("name")

    fun map(row: ResultRow) = Report(
        name = row[Reports.name]
    )

    fun of(report: Report, st: UpdateBuilder<Int>) {
        st[name] = report.name
    }    
}

class Server : CoroutineVerticle() {

    override suspend fun start() {
        // JSON marshalling config
        Json.mapper.registerModule(KotlinModule())

        // Db config. There's a config library in vertx to
        // abstract this.
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/robtay01",
            user = "robtay01",
            driver = "org.postgresql.Driver"
        )

        // Set up routes
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create());
        router.get("/hello").handler({ ctx -> 
            ctx.response().end("Hello")
        })
        router.post("/reports").handler(this::handleAddReport)
    
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }
    
    // POST handler
    fun handleAddReport(ctx: RoutingContext) {
        // map body to JSON
        val body = ctx.bodyAsJson
        // map JSON to Report data class
        val report = body.mapTo(Report::class.java)
        // This transaction blocks the main thread so shouldn't
        // take too long. 
        // The whole handler could be re-written as a co-routine 
        // to not block the main thread        
        // Using the DSL API of https://github.com/JetBrains/Exposed here.
        // Could use the DAO option instead or Requery
        transaction {
            // Insert into DB
            val id = Reports.insertAndGetId {
                // of() maps the data class into a format the db
                // library likes
                Reports.of(report, it)
            }
        }
        // send a response
        ctx.response()
            .putHeader("content-type", "application/json")
            .end()        
    }
}