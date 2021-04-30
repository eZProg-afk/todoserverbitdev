package spiral.bit.dev.repository

import spiral.bit.dev.entities.TodoDraft
import spiral.bit.dev.entities.TodoItem

interface TodoRepository {
    fun getAllTodos(): List<TodoItem>

    fun getTodoItem(id: Int): TodoItem?

    fun addTodo(draft: TodoDraft): TodoItem

    fun removeTodo(id: Int): Boolean

    fun updateTodo(id: Int, draft: TodoDraft): Boolean
}
