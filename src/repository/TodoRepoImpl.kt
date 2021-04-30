package spiral.bit.dev.repository

import spiral.bit.dev.entities.TodoDraft
import spiral.bit.dev.entities.TodoItem

class TodoRepoImpl : TodoRepository {

    private val todos = mutableListOf<TodoItem>()

    override fun getAllTodos(): List<TodoItem> = todos

    override fun getTodoItem(id: Int): TodoItem? = todos.firstOrNull { it.id == id }

    override fun addTodo(draft: TodoDraft): TodoItem {
        val todo = TodoItem(
            id = todos.size + 1,
            title = draft.title,
            done = draft.done
        )
        todos.add(todo)
        return todo
    }

    override fun removeTodo(id: Int): Boolean = todos.removeIf { it.id == id }

    override fun updateTodo(id: Int, draft: TodoDraft): Boolean {
        val todo = todos.firstOrNull { it.id == id } ?: return false
        todo.title = draft.title
        todo.done = draft.done
        return true
    }
}