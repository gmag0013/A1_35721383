package com.fit2081.a1_grace_35721383

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.a1_grace_35721383.ui.theme.A1_Grace_35721383Theme
import java.time.format.TextStyle
import androidx.compose.runtime.*

data class NavItem(val label: String, val icon: ImageVector)

class HomeScreen1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_Grace_35721383Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White
                    ) {
                        //functions
                        FirstPage()
                    }
                }
            }
        }
    }
}
/*
I used ChatGPT (https://chatgpt.com) and Claude (https://claude.ai/) to format this page so the UI fits onto one page with cards.
I used AI to help create a bottom navigation bar.
 */
@Composable
fun FoodScoreCard(title: String, subtitle: String = "", description: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = subtitle, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navItems: List<NavItem>, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        navItems.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                label = { Text(text = navItem.label) }
            )
        }
    }
}

@Composable
fun ContentSection(context: Context) {
    val sharedPref = context.getSharedPreferences("UserFoodIntake", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("userId", "") ?: ""
    val foodQualityScore = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val inputStream = context.assets.open("data1.csv")
            val lines = inputStream.bufferedReader().useLines { it.toList() }

            val matchedLine = lines.drop(1).firstOrNull { line ->
                val tokens = line.split(",")
                tokens.size > 4 && tokens[1].trim() == userId.trim()
            }

            matchedLine?.let { line ->
                val tokens = line.split(",")
                val gender = tokens[2].lowercase()
                val score = if (gender == "male") tokens[3] else tokens[4]
                foodQualityScore.value = "$score / 100"
            }
        } catch (e: Exception) {
            foodQualityScore.value = "Error loading score"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello, Grace!",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You've already filled in your Food Intake Questionnaire.\nBut you can change details here:",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, FoodIntakeQuestions::class.java))
            },
            modifier = Modifier
                .height(50.dp)
                .width(280.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.teal_700))
        ) {
            Text(text = "Edit", color = Color.White, fontSize = 18.sp)
        }

        Image(
            painter = painterResource(id = R.drawable.monash_food),
            contentDescription = "Daily Food Plate",
            modifier = Modifier
                .size(250.dp)
                .padding(10.dp)
        )

        FoodScoreCard(
            title = "My Score:",
            subtitle = "Your Food Quality Score: ${foodQualityScore.value ?: "..."}",
            description = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines."
        )
    }
}

@Composable
fun FirstPage() {
    val context = LocalContext.current

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Insights", Icons.Default.Info),
        NavItem("NutriCoach", Icons.Default.Face),
        NavItem("Settings", Icons.Default.Settings)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navItems = navItems, selectedIndex = selectedIndex, onItemSelected = { index ->
                selectedIndex = index

                if (index == 1) {
                    context.startActivity(Intent(context, InsightsBreakdown::class.java))
                }
                // Add more navigation logic for NutriCoach or Settings if needed
            })
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFFF5F5F5)
        ) {
            if (selectedIndex == 0) {
                ContentSection(context = context)
            }
        }
    }
}
