package fr.isen.vargas.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import fr.isen.vargas.myapplication.ui.theme.MyApplicationTheme
import java.io.Serializable


class IngredientActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                Column {
                    val context  = LocalContext.current
                    val extraMessage = intent.getStringArrayListExtra("EXTRA_MESSAGE")
                    LogoWithText()
                    if (extraMessage != null) {
                        displayActivityTitle(context, extraMessage)
                    }
                }
            }
        }
    }
}



@Composable
fun displayActivityTitle(context: Context, list: Serializable?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "INGREDIENTS",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            color = Color.Black
        )
        Text(
            list.toString().dropLast(3),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            color = Color.Black
        )
        val imagelink = list.toString()[list.toString().length-2]
        val painter = rememberImagePainter(
            data ="",
            builder = {
                transformations(CircleCropTransformation())
            }
        )

        // Use the Image composable to display the loaded image
        Image(
            painter = painter,
            contentDescription = null, // Provide content description if needed
            modifier = Modifier.fillMaxSize()
        )
    }
}