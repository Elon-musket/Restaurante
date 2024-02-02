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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    var mealsDictionary: MutableMap<String, MutableList<String>> by remember { mutableStateOf(mutableMapOf()) }
                    var mealImageList: List<String> by remember { mutableStateOf(emptyList()) }
                    var boolean by remember { mutableStateOf(true) }
                    fr.isen.vargas.myapplication.LogoWithText()
                    val extraMessage: String = intent.getStringExtra("EXTRA_MESSAGE") ?: ""
                    val context  = LocalContext.current

                    fetchMenu(context, extraMessage) { fetchedMeals ->
                        mealsDictionary = fetchedMeals
                        boolean = false
                    }
                    if (boolean)
                    {
                    } else
                    {
                        displayActivityTitle(context, name = extraMessage, mealsDictionary)
                    }
                }
            }
        }
    }

    private fun fetchMenu(context: Context, name: String, callback: (MutableMap<String, MutableList<String>>) -> Unit) {
        // Instantiate the RequestQueue.
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
                val dictionaryEntre: MutableMap<String, MutableList<String>> = mutableMapOf()
                val dictionaryPlat: MutableMap<String, MutableList<String>> = mutableMapOf()
                val dictionaryDessert: MutableMap<String, MutableList<String>> = mutableMapOf()

                for (i in 0 until data.length()) {
                    val meal = data.getJSONObject(i)

                    if (meal.getString("name_fr").equals("Entr\u00e9es")) {
                        val itemsArray = meal.getJSONArray("items")
                        for (j in 0 until itemsArray.length()) {
                            val item = itemsArray.getJSONObject(j)
                            val itemNameFr = item.getString("name_fr")
                            val itemIdFr = item.getString("id")
                            val itemImage = item.getString("images")
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
                            listIngredient.add(itemImage)
                            dictionaryEntre[itemNameFr] = listIngredient
                        }
                    }
                    if (meal.getString("name_fr").equals("Plats")) {
                        val itemsArray = meal.getJSONArray("items")
                        for (j in 0 until itemsArray.length()) {
                            val item = itemsArray.getJSONObject(j)
                            val itemNameFr = item.getString("name_fr")
                            val itemIdFr = item.getString("id")
                            val itemImage = item.getString("images")
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
                            listIngredient.add(itemImage)
                            dictionaryPlat[itemNameFr] = listIngredient
                        }
                    }
                    if (meal.getString("name_fr").equals("Desserts")) {
                        val itemsArray = meal.getJSONArray("items")
                        for (j in 0 until itemsArray.length()) {
                            val item = itemsArray.getJSONObject(j)
                            val itemNameFr = item.getString("name_fr")
                            val itemIdFr = item.getString("id")
                            val itemImage = item.getString("images")
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
                            listIngredient.add(itemImage)
                            dictionaryDessert[itemNameFr] = listIngredient
                        }
                    }
                }
                if(name == "ENTREES")
                    callback(dictionaryEntre)
                else if(name == "PLATS")
                    callback(dictionaryPlat)
                else if(name == "DESSERTS")
                    callback(dictionaryDessert)
            },
            { error ->
                Log.e("fetchMenu", "Error fetching meals", error)
            }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)


    }
}

@Composable
fun MealList(
    mealsDictonnary: MutableMap<String, MutableList<String>>,
    context: Context,
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align the content horizontally
    ) {
        for (category in mealsDictonnary.keys) {
            Log.d("test",category)
            Text(
                category,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { goToIngredientActivity(context,mealsDictonnary[category]) }
            )
        }
    }
}
@Composable
fun displayActivityTitle(context: Context, name: String, mealsDictonnary: MutableMap<String, MutableList<String>>) {
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

        MealList(mealsDictonnary, context, name)
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
            Text(text = data[index])
                //maxLines = 1, fontSize = 20.sp, softWrap = false, modifier = Modifier.clickable { goToIngredientActivity(context,meals) })
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


fun goToIngredientActivity(context: Context, strings: List<String>?)
{
    val intent = Intent(context, IngredientActivity::class.java)
    val nameArrayList = ArrayList(strings)
    intent.putStringArrayListExtra("EXTRA_MESSAGE", nameArrayList)
    context.startActivity(intent)
}

