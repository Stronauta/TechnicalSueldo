package com.example.technical.presentation.Tecnico

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.technical.Screen
import com.example.technical.data.local.entities.TechnicalEntity
import com.example.technical.ui.theme.TechnicalTheme
import com.example.technical.presentation.Componets.TopAppBar
import com.example.technical.presentation.Componets.Notificacion
import com.example.technical.presentation.Componets.FloatingButton
import kotlinx.coroutines.launch

@Composable
fun TechnicalListScreen(
    viewModel: TecnicoViewModel,
    onTechnicalClickVer: (TechnicalEntity) -> Unit,
    onAddTechnical: () -> Unit,
    navController: NavHostController
   // onTechnicalDelete: (TechnicalEntity) -> Unit
){
    val technical by viewModel.technicals.collectAsStateWithLifecycle()
    TechnicalListBody(
        technical = technical,
        onTechnicalClickVer = onTechnicalClickVer,
        onAddTechnical = onAddTechnical,
        navController = navController,

    )
}

@Composable
fun TechnicalListBody(
    technical: List<TechnicalEntity>,
    onTechnicalClickVer: (TechnicalEntity) -> Unit,
    onAddTechnical: () -> Unit,
    navController: NavHostController,
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var technicalDelete by remember { mutableStateOf<TechnicalEntity?>(null) }

    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp))
            {
                Text("Lista de tipo de Técnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de tipo de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TechnicalListScreen) },
                    icon = {
                        Icon(
                            imageVector = Icons.TwoTone.Person,
                            contentDescription = "Lista de tipo de tecnicos"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ){
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Tecnicos",
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingButton(onAddTechnical)
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(4.dp)
            ) {

                if (technical.isNotEmpty()) {

                }

                ElevatedCard {
                    Row {
                        Text(
                            text = "#",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.100f)
                        )

                        Text(
                            text = "Nombre",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.25f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Sueldo por Hora",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.40f)
                        )

                        Text(
                            text = "Tipo",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.10f)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(technical) { Technical ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = if ((Technical.tecnicoId ?: 0) % 2 == 0)
                                        Color(0x8FFFFFFF)
                                    else
                                        Color(0xFFFFFFFF)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTechnicalClickVer(Technical) }
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = Technical.tecnicoId.toString() + ". ",
                                    modifier = Modifier.weight(0.100f)
                                )
                                Text(text = Technical.tecnicoName, modifier = Modifier.weight(0.25f))
                                Text(
                                    text = "RD " + Technical.monto.toString(),
                                    modifier = Modifier.weight(0.25f)
                                )
                                Text(
                                    text = Technical.tipo,
                                    modifier = Modifier.weight(0.10f)
                                )

                                IconButton(
                                    onClick = {
                                        showDialog = true
                                        technicalDelete = Technical
                                    },
                                    modifier = Modifier.height(23.dp),
                                ) {
                                    Icon(
                                        Icons.TwoTone.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color(0xFFC95050)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Gray)
                        )
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = Color(0xFFDAA504)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    "Eliminar Técnico",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        },
                        text = {
                            Text(
                                "¿Esta seguro de eliminar el técnico ?",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    // onTechnicalDelete(technicalDelete!!)
                                    showDialog = false
                                    Toast.makeText(context, "Técnico eliminado", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.Red
                                )
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }

        }

    }
}

@Preview
@Composable
 fun TechnicalListPreview(){
    val technical = listOf(
        TechnicalEntity(
            tecnicoId = 1,
            tecnicoName = "Samir",
            monto = 1000.0
        )
    )
    TechnicalTheme{
        val technical = listOf(
            TechnicalEntity(
                tecnicoId = 1,
                tecnicoName = "Samir",
                monto = 1000.0
            )
        )
        TechnicalListBody(technical = technical,
            onTechnicalClickVer = {},
            onAddTechnical = {},
            navController = rememberNavController()
        )
    }
}




