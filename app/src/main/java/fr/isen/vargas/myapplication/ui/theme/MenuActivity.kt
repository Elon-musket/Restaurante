package fr.isen.vargas.myapplication.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.vargas.myapplication.IngredientActivity
import fr.isen.vargas.myapplication.R
import org.json.JSONObject

class MenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Column {
                    val meals = remember { mutableStateOf(listOf<String>()) }
                    fr.isen.vargas.myapplication.LogoWithText()
                    val extraMessage: String = intent.getStringExtra("EXTRA_MESSAGE") ?: ""
                    val context  = LocalContext.current
                    fetchMenu(extraMessage) { fetchedMeals ->
                        meals.value = fetchedMeals
                    }
                    displayActivityTitle(context, name = extraMessage, meals.value)

                }
            }
        }
    }
}

@Composable
fun MealList(meals: List<String>, context: Context, name: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align the content horizontally
    ) {
        for (meal in meals)
        {
                Text(
                    meal,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )// Log each meal to the console
        }
    }
}
@Composable
fun displayActivityTitle(context: Context, name: String, meals: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align the content horizontally
    ) {
        Text(
            name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),

        )

        MealList(meals, context, name)
    }
}

@Composable
fun extractDataFromXml(context: Context, name: String) {
    val data = stringArrayResource(id = getResourceIdByName(name))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(data.size) { index ->
            Text(text = data[index],
                maxLines = 1, fontSize = 20.sp, softWrap = false, modifier = Modifier.clickable { goToIngredientActivity(context,"") })
        }
    }
}


private fun getResourceIdByName(name: String): Int {
    return try {
        // Assuming the resources are in the 'R.array' namespace
        val field = R.array::class.java.getField(name)
        field.getInt(null)
    } catch (e: Exception) {
        e.printStackTrace()
        0 // Return 0 if the resource ID is not found
    }
}


fun goToIngredientActivity(context: Context, name: String)
{
    val intent = Intent(context, IngredientActivity::class.java)
    intent.putExtra("EXTRA_MESSAGE", name)
    context.startActivity(intent)
}


data class Menu(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val type: String
)
@Composable
private fun fetchMenu(name: String, callback: (List<String>) -> Unit) {
    // Instantiate the RequestQueue.
    val context  = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val url = "http://test.api.catering.bluecodegames.com/menu"

    val jsonObject = JSONObject()
    jsonObject.put("id_shop", "1")

    // Request a string response from the provided URL.
    val stringRequest = JsonObjectRequest(
        POST, url, jsonObject,
        { response ->
            // Parse the response
            val jsonResponse = JSONObject(response.toString())
            val data = jsonResponse.getJSONArray("data")
            val mealsEntree = mutableListOf<String>()
            val mealsPlats = mutableListOf<String>()
            val mealsDessert = mutableListOf<String>()
            for (i in 0 until data.length()) {
                val meal = data.getJSONObject(i)

                if (meal.getString("name_fr").equals("Entr\u00e9es")) {
                    val itemsArray = meal.getJSONArray("items")
                    for (j in 0 until itemsArray.length()) {
                        val item = itemsArray.getJSONObject(j)
                        val itemNameFr = item.getString("name_fr")
                        mealsEntree.add(itemNameFr)  // Or process itemNameFr as needed
                        Log.d("DebugRecomposition", itemNameFr)
                    }
                }
                if (meal.getString("name_fr").equals("Plats")) {
                    val itemsArray = meal.getJSONArray("items")
                    for (j in 0 until itemsArray.length()) {
                        val item = itemsArray.getJSONObject(j)
                        val itemNameFr = item.getString("name_fr")
                        mealsPlats.add(itemNameFr)  // Or process itemNameFr as needed
                        Log.d("DebugRecomposition", itemNameFr)
                    }
                }
                if (meal.getString("name_fr").equals("Desserts")) {
                    val itemsArray = meal.getJSONArray("items")
                    for (j in 0 until itemsArray.length()) {
                        val item = itemsArray.getJSONObject(j)
                        val itemNameFr = item.getString("name_fr")
                        mealsDessert.add(itemNameFr)  // Or process itemNameFr as needed
                        Log.d("DebugRecomposition", itemNameFr)
                    }
                }
            }
            if(name == "ENTREES")
                callback(mealsEntree)
            else if(name == "PLATS")
                callback(mealsPlats)
            else if(name == "DESSERTS")
                callback(mealsDessert)
        },
        { error ->
            Log.e("fetchMenu", "Error fetching meals", error)
        }
    )

    // Add the request to the RequestQueue.
    queue.add(stringRequest)


}