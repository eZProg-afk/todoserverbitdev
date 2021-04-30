package spiral.bit.dev.database

import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import spiral.bit.dev.entities.TodoDraft
import spiral.bit.dev.entities.TodoItem

class DatabaseManager {

    //9:44 (6)
    private val hostName = "localhost"
    private val dbName = "tododb"
    private val userName = "root"
    private val password = "tukzarchik"

    //database
    private val ktormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostName:3306/$dbName?serverTimezone=Europe/Moscow&user=$userName&password=$password&useSSL=false"
        ktormDatabase = Database.connect(jdbcUrl)
    }

    fun getAllTodos(): List<DBTodoEntity> {
        return ktormDatabase.sequenceOf(DBTodoTable).toList()
    }

    fun getTodo(id: Int): DBTodoEntity? {
        return ktormDatabase.sequenceOf(DBTodoTable)
            .firstOrNull { it.id eq id }
    }

    fun addTodo(draft: TodoDraft): TodoItem {
        val insertedId = ktormDatabase.insertAndGenerateKey(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
        } as Int

        return TodoItem(insertedId, draft.title, draft.done)
    }

    fun updateTodo(id: Int, draft: TodoDraft): Boolean {
       val updatedRows = ktormDatabase.update(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            where { it.id eq id }
        }
        return updatedRows > 0
    }

    fun removeTodo(id: Int): Boolean {
        val deletedRows = ktormDatabase.delete(DBTodoTable) {
            it.id eq id
        }
        return deletedRows > 0
    }
}