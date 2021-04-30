package spiral.bit.dev.repository

import spiral.bit.dev.database.DatabaseManager
import spiral.bit.dev.entities.TodoDraft
import spiral.bit.dev.entities.TodoItem

class MySQLTodoRepository: TodoRepository {

    private val database = DatabaseManager()

    override fun getAllTodos(): List<TodoItem> {
       return database.getAllTodos()
           .map { TodoItem(it.id, it.title, it.done) }
    }

    override fun getTodoItem(id: Int): TodoItem? {
        return database.getTodo(id)
            ?.let { TodoItem(it.id, it.title, it.done) }
    }

    override fun addTodo(draft: TodoDraft): TodoItem {
        return database.addTodo(draft)
    }

    override fun removeTodo(id: Int): Boolean {
        return database.removeTodo(id)
    }

    override fun updateTodo(id: Int, draft: TodoDraft): Boolean {
        return database.updateTodo(id, draft)
    }
}