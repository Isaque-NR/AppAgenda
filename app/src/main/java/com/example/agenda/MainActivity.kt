package com.example.agenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agenda.ui.theme.AgendaTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import com.example.agenda.ui.theme.VerdeAgenda
import com.example.agenda.ui.theme.AzulAgenda
import com.example.agenda.ui.theme.AmareloAgenda
import com.example.agenda.ui.theme.BancoAgendaHelper
import com.example.agenda.ui.theme.VermelhoAgenda
import com.example.agenda.ui.theme.BrancoAgenda
import com.example.agenda.ui.theme.Purple40
import com.example.agenda.ui.theme.TextoEscuroAgenda
import com.example.agenda.ui.theme.VermelhoClaroAgenda

data class Compromisso(
    val id: Int =0,
    val titulo: String,
    val descricao: String,
    val data: String,
    val hora: String
)

data class Atividade(
    val id: Int =0,
    val titulo: String,
    var feito: Boolean
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val banco = BancoAgendaHelper(this)

        setContent {
            val navController = rememberNavController()
            val listaCompromissos = remember {
                mutableStateListOf<Compromisso>().apply {
                    addAll(banco.listarCompromissos())
                }
            }
            val listaAtividades= remember {
                mutableStateListOf<Atividade>().apply {
                    addAll(banco.listarAtividades())
                }
            }


            AgendaTheme {
                    NavHost(
                        navController = navController,
                        startDestination = "principal"
                    ){
                        composable("principal") {
                            TelaPrincipal(navController,
                                listaCompromissos,
                                listaAtividades,
                                banco)
                        }

                        composable("novoCompromisso") {
                            TelaNovoCompromisso(navController,
                                listaCompromissos,
                                banco)
                        }

                        composable("novaAtividade") {
                            TelaNovaAtividade(navController,
                                listaAtividades,
                                banco)
                        }

                        composable("removerItem") {
                            TelaRemoverItem(navController,
                                listaAtividades,
                                listaCompromissos,
                                banco)
                        }
                    }
            }
        }
    }
}

@Composable
fun TelaPrincipal(navController : NavController,
                  listaCompromissos: SnapshotStateList<Compromisso>,
                  listaAtividades: SnapshotStateList<Atividade>,
                  banco: BancoAgendaHelper
) {
    val progresso = if (listaAtividades.isNotEmpty()) {
        listaAtividades.count { it.feito }.toFloat() /
                listaAtividades.size
    } else {
        0f
    }

    Column (
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ){
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "App Agenda",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            //color = AmareloAgenda
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Progresso do Dia",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        LinearProgressIndicator(
            progress = { progresso },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            color = VerdeAgenda,
            trackColor = VermelhoClaroAgenda
        )
        Text(
            text = "${(progresso * 100).toInt()}%",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(30.dp))

        Row(
                modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cazul),
                    contentDescription = "Compromissos",
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Compromissos:",
                    fontSize = 24.sp,

                )
                Spacer(modifier = Modifier.height(15.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(listaCompromissos) { compromisso ->

                        Text(text = compromisso.titulo,
                            fontSize = 20.sp)
                        Text(text = compromisso.descricao,
                            fontSize = 15.sp)
                        Text(text = compromisso.data,
                            fontSize = 15.sp)
                        Text(text = compromisso.hora,
                            fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tverde),
                    contentDescription = "A Fazer",
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = "A Fazer:",
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(15.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    itemsIndexed(listaAtividades) { indice,atividade ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(atividade.titulo)

                            Checkbox(
                                checked = atividade.feito,
                                onCheckedChange = { marcado ->

                                    val atividadeAtualizada = atividade.copy(
                                        feito = marcado
                                    )

                                    banco.atualizarAtividade(atividadeAtualizada)

                                    listaAtividades[indice] = atividadeAtualizada
                                },
                                    colors = CheckboxDefaults.colors(
                                     checkedColor = VerdeAgenda,
                                     uncheckedColor = VermelhoAgenda
                                )
                            )
                        }
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {
                    navController.navigate("novoCompromisso")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAgenda,
                    contentColor = BrancoAgenda,
                    disabledContainerColor = Purple40,
                    disabledContentColor = TextoEscuroAgenda
                )
            ) {
                Text("Adicionar Compromisso")
            }

            Button(
                onClick = {
                    navController.navigate("novaAtividade")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmareloAgenda,
                    contentColor = TextoEscuroAgenda,
                    disabledContentColor = TextoEscuroAgenda
                )
            ) {
                Text("Adicionar Atividade")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Image( painter = painterResource(id = R.drawable.vermlixo),
            contentDescription = "Remover Item",
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .size(45.dp)
                .clickable{
                 navController.navigate("removerItem")
                }
        )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Remover Item",
                modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun TelaNovoCompromisso(
    navController: NavController,
    listaCompromissos: SnapshotStateList<Compromisso>,
    banco: BancoAgendaHelper
) {
    var titulo by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    var hora by remember {
        mutableStateOf("")
    }
    var mostrarDatePicker by remember {
        mutableStateOf(false)
    }

    var data by remember {
        mutableStateOf("")
    }

    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            text = "Novo Compromisso",
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título do Compromisso")
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = descricao,
            onValueChange = {
                descricao = it
            },
            label = {
                Text("Descrição")
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = hora,
            onValueChange = {
                hora = it
            },
            label = {
                Text("Hora do Compromisso ")
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                mostrarDatePicker = true
            }
        ) {
            Text("Abrir Calendario")
        }
        Text(
                text = "Data: $data"
                )
        if (mostrarDatePicker) {

            DatePickerDialog(
                onDismissRequest = {
                    mostrarDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val millis =
                                datePickerState.selectedDateMillis

                            if (millis != null) {

                                val formatter =
                                    SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    )

                                data = formatter.format(Date(millis))
                            }

                            mostrarDatePicker = false

                        }
                    ) {
                        Text("Ok")
                    }

                },
                dismissButton = {

                }
            ) {

                DatePicker(
                    state = datePickerState
                )

            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        val criacaoValida = titulo.isNotBlank() && descricao.isNotBlank() && hora.isNotBlank() && data.isNotBlank()
        Text("Preencha todos os campos para poder criar")
        Button(
            enabled = criacaoValida,
            onClick = {
                val novoCompromisso =
                    Compromisso(
                        titulo = titulo,
                        descricao = descricao,
                        data = data,
                        hora = hora
                    )
                val idGerado = banco.inserirCompromisso(novoCompromisso)

                if (idGerado != -1L) {
                    listaCompromissos.add(
                        novoCompromisso.copy(
                            id = idGerado.toInt()
                        )
                    )
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeAgenda,
                contentColor = BrancoAgenda
            )
        ) {

            Text("Criar  Compromisso")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VermelhoAgenda,
                contentColor = BrancoAgenda
            )
        ) {

            Text("Voltar")

        }

    }

}
@Composable
fun TelaNovaAtividade(
    navController: NavController,
    listaAtividades: SnapshotStateList<Atividade>,
    banco: BancoAgendaHelper

) {
    var titulo by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            text = "Nova Atividade",
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título da Atividade")
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text("Preencha o titulo para poder criar")
        val criacaoValida = titulo.isNotBlank()
        Button(
            enabled = criacaoValida,
            onClick = {
                val novaAtividade =
                    Atividade(
                        titulo = titulo,
                        feito = false,
                    )

                val idGerado = banco.inserirAtividade(novaAtividade)

                if (idGerado != -1L) {
                    listaAtividades.add(
                        novaAtividade.copy(
                            id = idGerado.toInt()
                        )
                    )

                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeAgenda,
                contentColor = BrancoAgenda
            )
        ) {

            Text("Criar Atividade")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VermelhoAgenda,
                contentColor = BrancoAgenda
            )
        ) {

            Text("Voltar")

        }


    }

}
@Composable
fun TelaRemoverItem(navController: NavController,
                    listaAtividades: SnapshotStateList<Atividade>,
                    listaCompromissos: SnapshotStateList<Compromisso>,
                    banco: BancoAgendaHelper
) {
    val compromissosSelecionados =
        remember {
            mutableStateListOf<Compromisso>()
        }

    val atividadesSelecionadas =
        remember {
            mutableStateListOf<Atividade>()
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "Atividades Listadas: ",
            fontSize = 35.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn {

            items(listaAtividades) { atividade ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked =
                            atividadesSelecionadas.contains(atividade),

                        onCheckedChange = { marcado ->

                            if (marcado)
                                atividadesSelecionadas.add(atividade)
                            else
                                atividadesSelecionadas.remove(atividade)
                        }
                    )

                    Text(atividade.titulo)
                }
            }
        }

        LazyColumn {

            items(listaCompromissos) { compromisso ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked =
                            compromissosSelecionados.contains(compromisso),

                        onCheckedChange = { marcado ->

                            if (marcado)
                                compromissosSelecionados.add(compromisso)
                            else
                                compromissosSelecionados.remove(compromisso)
                        }
                    )

                    Text(compromisso.titulo)
                }
            }
        }
        Button(
            onClick = {

                compromissosSelecionados.forEach { compromisso ->
                    banco.removerCompromisso(compromisso.id)
                }

                atividadesSelecionadas.forEach { atividade ->
                    banco.removerAtividade(atividade.id)
                }

                listaCompromissos.removeAll(compromissosSelecionados)
                listaAtividades.removeAll(atividadesSelecionadas)

                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = AmareloAgenda,
                contentColor = BrancoAgenda
            )
        ) {
            Text("Remover Selecionados")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VermelhoAgenda,
                contentColor = BrancoAgenda
            )
        ) {

            Text("Voltar")

        }

    }
}