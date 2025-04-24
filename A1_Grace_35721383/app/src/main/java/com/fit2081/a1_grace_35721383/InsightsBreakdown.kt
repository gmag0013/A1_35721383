package com.fit2081.a1_grace_35721383

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
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
import com.fit2081.a1_grace_35721383.ui.theme.A1_Grace_35721383Theme
import java.io.BufferedReader
import java.io.InputStreamReader

class InsightsBreakdown : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_Grace_35721383Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = Color.White
                    ) {
                        Greeting3()
                    }
                }
            }
        }
    }
}

fun loadCsvData(context: Context, userId: String): Map<String, String>? {
    try {
        val inputStream = context.assets.open("data1.csv")
        val reader = inputStream.bufferedReader()
        val lines = reader.readLines()
        val headers = lines.first().split(",")
        val matchedLine = lines.drop(1).firstOrNull { it.split(",")[1].trim() == userId.trim() }
        return matchedLine?.split(",")?.let { values ->
            headers.zip(values).toMap()
        }
    } catch (e: Exception) {
        return null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting3() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("UserFoodIntake", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("userId", "") ?: ""
    val csvData = remember { loadCsvData(context, userId) }
    val gender = csvData?.get("Sex")?.lowercase()

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Insights", Icons.Default.Info),
        NavItem("NutriCoach", Icons.Default.Face),
        NavItem("Settings", Icons.Default.Settings)
    )

    var selectedIndex by remember { mutableStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navItems = navItems, selectedIndex = selectedIndex, onItemSelected = { index ->
                selectedIndex = index
                if (index == 0) {
                    context.startActivity(Intent(context, HomeScreen1::class.java))
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Insights: Food Score",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            val foodComponents = listOf(
                "VegetablesHEIFAscore", "FruitHEIFAscore", "GrainsandcerealsHEIFAscore",
                "WholegrainsHEIFAscore", "MeatandalternativesHEIFAscore", "DairyandalternativesHEIFAscore",
                "WaterHEIFAscore", "UnsaturatedFatHEIFAscore", "SodiumHEIFAscore",
                "SugarHEIFAscore", "AlcoholHEIFAscore", "DiscretionaryHEIFAscore"
            )

            foodComponents.forEach { label ->
                val scoreKey = if (gender == "male") "${label}Male" else "${label}Female"
                val score = csvData?.get(scoreKey)?.toFloatOrNull() ?: 0f

                // Ensure progress is between 0f and 1f
                val progress = score / 10f // Convert to a fraction (max score is 10)

                // Use a State variable to manage progress updates
                val progressState = remember { mutableStateOf(progress) }

                // Format label text to replace unwanted terms
                val formattedLabel = when (label) {
                    "GrainsandcerealsHEIFAscore" -> "Grains & Cereals"
                    "WholegrainsHEIFAscore" -> "Whole Grains"
                    "MeatandalternativesHEIFAscore" -> "Meats & Alternatives"
                    "DairyandalternativesHEIFAscore" -> "Dairy"
                    else -> label.replace("HEIFAscore", "").replace(Regex("([a-z])([A-Z])"), "$1 $2")
                }

                // Local variable inside the Composable function
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp), // Reduced padding for smaller spacing
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Header Text on the left
                    Text(
                        text = formattedLabel,
                        fontSize = 10.sp, // Adjust font size to match design
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    // LinearProgressIndicator in the middle
                    LinearProgressIndicator(
                        progress = progressState.value,
                        modifier = Modifier
                            .weight(3f)
                            .width(100.dp)
                            .height(6.dp), // Adjusted height to fit better
                        color = Color(0xFF6200EE),
                    )

                    // Score text on the right
                    Text(
                        text = "Score: ${score}/10",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Increased space between sections

            val totalScore = if (gender == "male") {
                csvData?.get("HEIFAtotalscoreMale")?.toFloatOrNull()
            } else {
                csvData?.get("HEIFAtotalscoreFemale")?.toFloatOrNull()
            } ?: 0f

            // Local variable for totalProgress inside Composable
            val totalProgress = totalScore / 100f // Assuming max total score is 100

            // Use a State variable for total progress
            val totalProgressState = remember { mutableStateOf(totalProgress) }

            Text(
                "Total Food Quality Score",
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            // LinearProgressIndicator for total score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = totalProgressState.value,
                    modifier = Modifier
                        .weight(3f)
                        .height(6.dp), // Adjusted height to fit better
                    color = Color(0xFF6200EE),
                )

                // Score text on the right of the total progress bar
                Text(
                    text = "Total Score: ${totalScore}/100",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes buttons to the bottom

            // Buttons at the bottom
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Button color matching the design
                ) {
                    Text("Share with someone", color = Color.White, fontSize = 14.sp)
                }
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Same button color
                ) {
                    Text("Improve my diet!", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}