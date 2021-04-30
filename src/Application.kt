package spiral.bit.dev

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.content.*
import spiral.bit.dev.entities.TodoDraft
import spiral.bit.dev.entities.TodoItem
import spiral.bit.dev.repository.MySQLTodoRepository
import spiral.bit.dev.repository.TodoRepoImpl
import spiral.bit.dev.repository.TodoRepository

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    install(ContentNegotiation) { gson { setPrettyPrinting() } }

    routing {
        val repository: TodoRepository = MySQLTodoRepository()

        get("/") {
            call.respondText("Hello TodoList!")
        }

        get("/todos") { //return all todoitems
            call.respond(repository.getAllTodos())
        }

        get("/todos/{id}") { //
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID parameter must be a number!")
                return@get
            }

            val todo = repository.getTodoItem(id)
            if (todo == null) {
                call.respond(HttpStatusCode.NotFound, "found no one todo item fo the id #$id")
            } else {
                call.respond(todo)
            }
        }

        post("/todos") { //for add a todoitem
            val todoDraft = call.receive<TodoDraft>()
            val todo = repository.addTodo(todoDraft)
            call.respond(todo)
        }

        put("/todos/{id}") {
            val todoDraft = call.receive<TodoDraft>()
            val todoId = call.parameters["id"]?.toIntOrNull()
            if (todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "id parameter must be a number!")
                return@put
            }

            val updated = repository.updateTodo(todoId, todoDraft)
            if (updated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound, "Found no one item with id #$todoId")
        }

        delete("/todos/{id}") {
            val todoId = call.parameters["id"]?.toIntOrNull()
            if (todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "Id parameter must be a number!")
                return@delete
            }

            val removed = repository.removeTodo(todoId)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound, "Found no one item with id #$todoId")
        }
    }
}