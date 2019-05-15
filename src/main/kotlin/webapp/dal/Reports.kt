package webapp.dal

import webapp.model.Report
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Reports : Table() {

    val id = integer("id").primaryKey().autoIncrement()
    val name = text("name")

    fun map(row: ResultRow) = Report(
        id = row[Reports.id],
        name = row[Reports.name]
    )

    fun of(report: Report, st: UpdateBuilder<Int>) {
        st[id] = report.id
        st[name] = report.name
    }
}