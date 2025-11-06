package me.jorgeluquetta.unabstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


data class Producto(

    val nombre: String = "",
    val cantidad: Int = 0,
    val precio: Double = 0.0
)

@Composable
fun FormularioAgregarProducto() {
    val db = Firebase.firestore
    var nombre by remember() { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
        TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })

        Button(onClick = {
            val producto = hashMapOf(
                "nombre" to nombre,
                "cantidad" to cantidad.toInt(),
                "precio" to precio.toDoubleOrNull()
            )
            db.collection("productos").add(producto).addOnSuccessListener {
                nombre = ""
                cantidad = ""
                precio = ""
                onProductoAgregado()
            }
        }) {
            Text("Agregar Producto")
        }
    }
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(onClickLogout: () -> Unit = {}) {
    val auth = Firebase.auth
    val user = auth.currentUser

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Unab Shop",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Notifications, "Notificaciones")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ShoppingCart, "Carrito")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salida")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFF9900),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("HOME SCREEN", fontSize = 30.sp)
                if(user != null){
                    Text(user.email.toString())
                }else{
                    Text("No hay usuario")
                }
                Button(onClick = {
                    auth.signOut()
                    onClickLogout()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9900)
                    )
                ){
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}