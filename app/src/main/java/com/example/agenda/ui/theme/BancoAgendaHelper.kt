package com.example.agenda.ui.theme


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.agenda.Atividade
import com.example.agenda.Compromisso

class BancoAgendaHelper(context: Context) :
    SQLiteOpenHelper(context, "agenda.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val criarTabelaCompromissos = """
            CREATE TABLE compromissos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                descricao TEXT NOT NULL,
                data TEXT NOT NULL,
                hora TEXT NOT NULL
            )
        """

        val criarTabelaAtividades = """
            CREATE TABLE atividades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                feito INTEGER NOT NULL -- sql nao tem boolean entao fica um inteiro 0 e 1 
            )
        """

        db.execSQL(criarTabelaCompromissos)
        db.execSQL(criarTabelaAtividades)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS compromissos")
        db.execSQL("DROP TABLE IF EXISTS atividades")
        onCreate(db)
    }


    fun inserirCompromisso(compromisso: Compromisso): Long {
        val db = writableDatabase

        val valores = ContentValues().apply {
            put("titulo", compromisso.titulo)
            put("descricao", compromisso.descricao)
            put("data", compromisso.data)
            put("hora", compromisso.hora)
        }

        return db.insert("compromissos", null, valores)
    }

    fun inserirAtividade(atividade: Atividade): Long {
        val db = writableDatabase

        val valores = ContentValues().apply {
            put("titulo", atividade.titulo)
            put("feito", if (atividade.feito) 1 else 0)
        }

        return db.insert("atividades", null, valores)
    }

    fun listarCompromissos(): List<Compromisso> {
        val lista = mutableListOf<Compromisso>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM compromissos",
            null
        )

        while (cursor.moveToNext()) {
            val compromisso = Compromisso(
                id = cursor.getInt(0),
                titulo = cursor.getString(1),
                descricao = cursor.getString(2),
                data = cursor.getString(3),
                hora = cursor.getString(4)
            )

            lista.add(compromisso)
        }

        cursor.close()
        return lista
    }

    fun listarAtividades(): List<Atividade> {
        val lista = mutableListOf<Atividade>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM atividades",
            null
        )

        while (cursor.moveToNext()) {
            val atividade = Atividade(
                id = cursor.getInt(0),
                titulo = cursor.getString(1),
                feito = cursor.getInt(2) == 1
            )

            lista.add(atividade)
        }

        cursor.close()
        return lista
    }

    fun atualizarAtividade(atividade: Atividade): Int {
        val db = writableDatabase

        val valores = ContentValues().apply {
            put("feito", if (atividade.feito) 1 else 0)
        }

        return db.update(
            "atividades",
            valores,
            "id = ?",
            arrayOf(atividade.id.toString())
        )
    }

    fun removerAtividade(id: Int): Int {
        val db = writableDatabase

        return db.delete(
            "atividades",
            "id = ?",
            arrayOf(id.toString())
        )
    }

    fun removerCompromisso(id: Int): Int {
        val db = writableDatabase

        return db.delete(
            "compromissos",
            "id = ?",
            arrayOf(id.toString())
        )
    }

}


