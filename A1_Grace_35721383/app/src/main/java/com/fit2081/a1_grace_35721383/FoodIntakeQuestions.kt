package com.fit2081.a1_grace_35721383

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MenuAnchorType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.fit2081.a1_grace_35721383.ui.theme.A1_Grace_35721383Theme
import java.util.Calendar

class FoodIntakeQuestions : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_Grace_35721383Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        FoodIntakeScreen()
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoodIntakeScreen() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("UserFoodIntake", Context.MODE_PRIVATE)

    val foodOptions = listOf("Fruits", "Vegetables", "Grains", "Red Meat", "Seafood", "Poultry")
    val personaOptions = listOf("Health Devotee", "Mindful Eater", "Wellness Striver", "Balance Seeker", "Health Procrastinator", "Food Carefree")

    // Define the Persona data with names, descriptions, and placeholder image resource IDs
    data class Persona(val name: String, val description: String, val imageRes: Int)

    val personas = listOf(
        Persona(
            name = "Health Devotee",
            description = "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
            imageRes = R.drawable.persona1  // placeholder image
        ),
        Persona(
            name = "Mindful Eater",
            description = "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
            imageRes = R.drawable.persona2
        ),
        Persona(
            name = "Wellness Striver",
            description = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
            imageRes = R.drawable.persona3
        ),
        Persona(
            name = "Balance Seeker",
            description = "I try to live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
            imageRes = R.drawable.persona4
        ),
        Persona(
            name = "Health Procrastinator",
            description = "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant at the moment. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
            imageRes = R.drawable.persona5
        ),
        Persona(
            name = "Food Carefree",
            description = "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat.",
            imageRes = R.drawable.persona6
        )
    )

    // UI state: currently selected persona and the persona for which info dialog is shown
    var selectedPersona by remember { mutableStateOf<Persona?>(null) }
    var infoDialogPersona by remember { mutableStateOf<Persona?>(null) }

    val scrollState = rememberScrollState()

    // Track the state of the checkboxes (selected or not)
    val checkboxes = remember { mutableStateListOf(*Array(foodOptions.size) { sharedPref.getBoolean("food_$it", false) }) }

    val time1 = remember { mutableStateOf(sharedPref.getString("time1", "") ?: "") }
    val time2 = remember { mutableStateOf(sharedPref.getString("time2", "") ?: "") }
    val time3 = remember { mutableStateOf(sharedPref.getString("time3", "") ?: "") }

    // Dropdown menu expanded state
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownSelectedPersona by remember {
        mutableStateOf(sharedPref.getString("dropdownPersona", ""))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Food Intake Questionnaire", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        Text("Tick all the food categories you can eat:", fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))

        // Food Categories Checkboxes
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            itemsIndexed(foodOptions) { index, food ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkboxes[index],
                        onCheckedChange = {
                            checkboxes[index] = it
                            with(sharedPref.edit()) {
                                putBoolean("food_$index", it)
                                apply()
                            }
                        }
                    )
                    Text(
                        text = food,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // Persona Selection (Buttons combined for Select and More Info)
        Text("Click on each button below to find out the different types, and select the type that best fits you.", fontSize = 12.sp)
        Spacer(Modifier.height(4.dp))

        // Persona Info Buttons in a 3x2 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(personas) { persona ->
                Button(
                    onClick = {
                        selectedPersona = persona
                        infoDialogPersona = persona
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(persona.name, fontSize = 10.sp, textAlign = TextAlign.Center)
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
            OutlinedTextField(
                value = dropdownSelectedPersona ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Persona") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                personaOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            dropdownSelectedPersona = option
                            with(sharedPref.edit()) {
                                putString("dropdownPersona", option)
                                apply()
                            }
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Time Pickers
        Text("What time do you?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimePickerColumn("Eat your biggest meal", time1)
            TimePickerColumn("Go to sleep", time2)
            TimePickerColumn("Wake up", time3)
        }

        Spacer(Modifier.height(8.dp))

        // Save Button
        Button(onClick = {
            // Save selected persona, checkboxes, and timings to SharedPreferences
            with(sharedPref.edit()) {
                putString("selectedPersona", selectedPersona?.name)
                foodOptions.forEachIndexed { index, _ ->
                    putBoolean("food_$index", checkboxes[index])
                }
                putString("time1", time1.value)
                putString("time2", time2.value)
                putString("time3", time3.value)
                putString("dropdownPersona", dropdownSelectedPersona)
                apply()

                // Navigate to the HomeScreen1 activity
                context.startActivity(Intent(context, HomeScreen1::class.java))
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }

    // Info Modal Dialog: shows when infoDialogPersona is not null
    if (infoDialogPersona != null) {
        AlertDialog(
            onDismissRequest = { infoDialogPersona = null },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Placeholder image representing the persona
                    Image(
                        painter = painterResource(id = infoDialogPersona!!.imageRes),
                        contentDescription = "${infoDialogPersona!!.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Persona description text
                    Text(text = infoDialogPersona!!.description)
                }
            },
            confirmButton = {
                TextButton(onClick = { infoDialogPersona = null }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
fun TimePickerColumn(label: String, timeState: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            timeState.value = String.format("%02d:%02d", hour, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 4.dp)
    ) {
        Text(text = label, fontSize = 14.sp)
        Button(
            onClick = { timePickerDialog.show() },
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text("Select", fontSize = 10.sp)
        }
        Text(text = timeState.value, fontSize = 12.sp)
    }
}