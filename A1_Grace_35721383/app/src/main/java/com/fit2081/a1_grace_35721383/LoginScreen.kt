package com.fit2081.a1_grace_35721383

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.a1_grace_35721383.ui.theme.A1_Grace_35721383Theme
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginScreen : ComponentActivity() {
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
                        LoginScreen(this@LoginScreen)
                    }
                }
            }
        }
    }
}
fun getAllIdPhonePairs(context: Context): List<Pair<String, String>> {
    val fileName = "data1.csv"
    val assets = context.assets
    val idPhonePairs = mutableListOf<Pair<String, String>>()

    try {
        val inputStream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // Skip header
                val values = line.split(",")
                if (values.size >= 2) {
                    val phoneNumber = values[0].trim()
                    val id = values[1].trim()
                    idPhonePairs.add(Pair(id, phoneNumber))
                }
            }
        }
    } catch (e: Exception) {
        // Handle errors if needed
    }

    return idPhonePairs
}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    // Example: Phone number must be all digits and 10 to 15 characters long
    return phoneNumber.all { it.isDigit() } && phoneNumber.length in 10..15
}

fun isValidID(id: String): Boolean {
    // Example: ID must not be empty
    return id.isNotBlank()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(context: Context) {
    val idPhonePairs = remember { getAllIdPhonePairs(context) }
    val idOptions = idPhonePairs.map { it.first }

    var selectedId by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    var idError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val sharedPref = context.getSharedPreferences("UserFoodIntake", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // ✅ enable scrolling
        verticalArrangement = Arrangement.Top, // ✅ better for scrollable layout
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Log in",
            style = TextStyle(fontSize = 40.sp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This app is only for pre-registered users.\nPlease have your ID and phone number handy before continuing.",
            style = TextStyle(fontSize = 18.sp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ID Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedId,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select ID") },
                isError = idError,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                idOptions.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            selectedId = id
                            expanded = false
                        }
                    )
                }
            }
        }

        if (idError) {
            Text(
                text = "Please select an ID",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Field
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                phoneNumberError = !isValidPhoneNumber(it)
            },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = phoneNumberError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (phoneNumberError) {
            Text(
                text = "Invalid Phone Number",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                idError = selectedId.isBlank()
                phoneNumberError = !isValidPhoneNumber(phoneNumber)

                if (!idError && !phoneNumberError) {
                    val isValidLogin = idPhonePairs.any { (id, phone) ->
                        id == selectedId && phone == phoneNumber
                    }

                    if (isValidLogin) {
                        sharedPref.edit().putString("userId", selectedId).apply()
                        context.startActivity(Intent(context, FoodIntakeQuestions::class.java))
                    } else {
                        loginError = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        if (loginError) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ID and Phone Number do not match.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // bottom padding
    }
}