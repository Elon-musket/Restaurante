package fr.isen.vargas.myapplication.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.vargas.myapplication.IngredientActivity
import fr.isen.vargas.myapplication.R
import org.json.JSONArray
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
                val ListImgEntrees  = mutableListOf<String>()
                val ListImgPlats  = mutableListOf<String>()
                val ListImgDesserts  = mutableListOf<String>()

                val ListPriceEntrees  = mutableListOf<String>()
                val ListPricePlats  = mutableListOf<String>()
                val ListPriceDesserts  = mutableListOf<String>()

                for (i in 0 until data.length()) {
                    val meal = data.getJSONObject(i)

                    if (meal.getString("name_fr").equals("Entr\u00e9es")) {
                        val itemsArray = meal.getJSONArray("items")
                        for (j in 0 until itemsArray.length()) {
                            val item = itemsArray.getJSONObject(j)
                            val itemNameFr = item.getString("name_fr")
                            val itemIdFr = item.getString("id")
                            val itemImage = item.getString("images")
                            val jsonArray = JSONArray(itemImage)
                            val secondLink = jsonArray.optString(1)
                            ListImgEntrees.add(secondLink)
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            val pricesArray = item.getJSONArray("prices")
                            val priceTab = pricesArray.getJSONObject(0)
                            val price = priceTab.getString("price")
                            ListPriceEntrees.add(price)
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
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
                            val jsonArray = JSONArray(itemImage)
                            val secondLink = jsonArray.optString(1)
                            ListImgPlats.add(secondLink)
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            val pricesArray = item.getJSONArray("prices")
                            val priceTab = pricesArray.getJSONObject(0)
                            val price = priceTab.getString("price")
                            ListPricePlats.add(price)
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
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
                            val jsonArray = JSONArray(itemImage)
                            val secondLink = jsonArray.optString(1)
                            ListImgDesserts.add(secondLink)
                            val ingredientArray = item.getJSONArray("ingredients")
                            val listIngredient = mutableListOf<String>()
                            val pricesArray = item.getJSONArray("prices")
                            val priceTab = pricesArray.getJSONObject(0)
                            val price = priceTab.getString("price")
                            ListPriceDesserts.add(price)
                            for(k in 0 until ingredientArray.length())
                            {
                                val ingredient = ingredientArray.getJSONObject(k)
                                val NameFr = ingredient.getString("name_fr")
                                val IdFr = ingredient.getString("id_pizza")
                                if(IdFr == itemIdFr)
                                    listIngredient.add(NameFr)
                            }
                            dictionaryDessert[itemNameFr] = listIngredient
                        }
                    }
                }
                if(name == "ENTREES") {
                    callback(dictionaryEntre)
                    GlobalVariables.globalTab = ListImgEntrees
                    GlobalVariables.priceTab = ListPriceEntrees
                }
                else if(name == "PLATS")
                {
                    callback(dictionaryPlat)
                    GlobalVariables.globalTab = ListImgPlats
                    GlobalVariables.priceTab = ListPricePlats
                }
                else if(name == "DESSERTS")
                {
                    callback(dictionaryDessert)
                    GlobalVariables.globalTab = ListImgDesserts
                    GlobalVariables.priceTab = ListPriceDesserts
                }
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
    mealsDictionary: MutableMap<String, MutableList<String>>,
    context: Context,
    name: String
) {
    val categoryTabIterator = GlobalVariables.globalTab.iterator()
    val priceTabIterator = GlobalVariables.priceTab.iterator()
    LazyColumn(
        modifier = Modifier
            .background(color = Color(android.graphics.Color.parseColor("#F8E8E2"))),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align the content horizontally
    ) {
        for ((category, ingredientList) in mealsDictionary.entries) {
            val price = if (priceTabIterator.hasNext()) priceTabIterator.next() else ""
            val img = if(categoryTabIterator.hasNext()) categoryTabIterator.next() else ""

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            goToIngredientActivity(
                                context,
                                ingredientList,
                                price,
                                img
                            )
                        }
                        .padding(5.dp).background(color = Color(android.graphics.Color.parseColor("#8DCBAD"))),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        // Text on the left
                        Text(
                            category,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f) // Takes up available space
                                .padding(end = 10.dp)
                        )


                            Text(
                                price + "€",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            val painter = rememberImagePainter(
                                data = img,
                                builder = {
                                    transformations(CircleCropTransformation())
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .width(100.dp) // Adjust width as needed
                                    .height(100.dp) // Adjust height as needed
                                    .padding(start = 8.dp),
                            ) {
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                    }
                }

            }
        }
    }
}

@Composable
fun displayActivityTitle(context: Context, name: String, mealsDictonnary: MutableMap<String, MutableList<String>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#B73300"))),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align the content horizontally
    ) {
        Text(
            name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
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

object GlobalVariables {
    var globalTab: List<String> = emptyList()
    var priceTab: List<String> = emptyList()
    var priceMeal : Int = 0
}


fun goToIngredientActivity(context: Context, strings: List<String>?, text: String, img: String) {
    try {
        if (strings != null && text.isNotBlank()) {
            val intent = Intent(context, IngredientActivity::class.java)
            val nameArrayList = ArrayList(strings)
            intent.putStringArrayListExtra("EXTRA_MESSAGE", nameArrayList)
            intent.putExtra("Link", img)

            // Convert the string to a Double (supports decimal values)
            val price = text.toDouble()

            // Use putExtra with the correct data type
            intent.putExtra("Price", price)

            context.startActivity(intent)
        } else {
            // Handle the case where strings or text is null or empty
            // Show a Toast, log a message, or take appropriate action
        }
    } catch (e: NumberFormatException) {
        // Handle the case where text cannot be converted to a double
        // Display the error using a Toast
        Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()

        // Alternatively, you can log the error
        Log.e("GoToIngredientActivity", "Error converting text to double", e)
    }
}



