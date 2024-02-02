package fr.isen.vargas.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vargas.myapplication.ui.theme.MenuActivity
import fr.isen.vargas.myapplication.ui.theme.MyApplicationTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Column {
                    val context  = LocalContext.current
                    LogoWithText()
                    displayMenu(context)
                }
            }
        }
    }
}

@Composable
fun LogoWithText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo restauranté",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(90.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Text(
                    "RESTAURANT DE PÈRE EN FILS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .weight(1f) // This makes the Text take the remaining horizontal space
                )
            }
            // Line at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

@Composable
fun displayMenu(context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent) ) {
        Image(
            painter = painterResource(id = R.drawable.tour),
            contentDescription = "logo restauranté",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(1f) // 80% of the width
                .fillMaxHeight(1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.38f)
                .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(15.dp))
                .align(Alignment.Center))
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Button(
                    onClick = {
                        goToActivity(context, "ENTREES")},
                    border = BorderStroke(1.dp, Color.Green),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Green),
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // 80% of the width
                        .padding(10.dp)
                ) {
                    Text("Entrées", fontSize = 20.sp)
                }
                Button(
                    onClick = {
                        goToActivity(context, "PLATS")},
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // 80% of the width
                        .padding(10.dp)
                ) {
                    Text("Plats", fontSize = 20.sp)
                }
                Button(
                    onClick = {
                        goToActivity(context, "DESSERTS")},
                    border = BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // 80% of the width
                        .padding(10.dp)
                ) {
                    Text("Désserts", fontSize = 20.sp)
                }
            }
        }
    }
}

fun goToActivity(context: Context, name: String)
{
    Toast.makeText(context, "Vous avez cliqué sur plats", Toast.LENGTH_SHORT).show()
    val intent = Intent(context, MenuActivity::class.java)
    intent.putExtra("EXTRA_MESSAGE", name)
    context.startActivity(intent)
}