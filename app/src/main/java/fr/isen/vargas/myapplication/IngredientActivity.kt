package fr.isen.vargas.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    val priceMessage = intent.getDoubleExtra("Price", 0.0)
                    val imageMsg = intent.getStringExtra("Link")
                    LogoWithText()
                    if (extraMessage != null) {
                        displayActivityTitle(context, extraMessage, priceMessage, imageMsg)
                    }
                }
            }
        }
    }
}



@Composable
fun displayActivityTitle(
    context: Context,
    list: Serializable?,
    price: Double,
    imageMsg: String?
) {
    // Initialize a mutable state for quantity and cost
    var quantity by remember { mutableStateOf(0) }
    var cost by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Check if imageMsg is not null or blank
        if (!imageMsg.isNullOrBlank()) {
            val painter = rememberImagePainter(
                data = imageMsg,
                builder = {
                    transformations(CircleCropTransformation())
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 8.dp),
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // Spacer to add space between image and content
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Content at the top
        Text(
            "INGRÉDIENTS",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            color = Color.Black
        )

        // Spacer to add space between ingredients and buttons
        Spacer(modifier = Modifier.height(16.dp))

        // Iterate over the list and display each item separately
        if (list is ArrayList<*> && list.isNotEmpty() && list[0] is String) {
            list.forEach { item ->
                Text(
                    item.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        } else {
            // Handle the case where the list is null or of the wrong type
            Text(
                "Invalid data format",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Row containing the increment and decrement buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (quantity > 0)
                {
                    cost = cost - price
                    quantity--
                }
            }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = quantity.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                cost = cost + price
                quantity++
            }) {
                Text("+")
            }
        }

        // Spacer to add space between buttons and cost text
        Spacer(modifier = Modifier.height(20.dp))

        // Cost text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = Color(android.graphics.Color.parseColor("#FF685C"))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TOTAL: ${cost}€",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}




