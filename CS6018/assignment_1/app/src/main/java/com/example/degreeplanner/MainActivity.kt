package com.example.degreeplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.degreeplanner.ui.theme.DegreePlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DegreePlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ){
                        var emailText by remember{ mutableStateOf("") }
                        var user by remember { mutableStateOf("")}
                        var domain by remember{ mutableStateOf("")}

                        OutlinedTextField(
                            modifier = Modifier.padding(16.dp),
                            label = {Text("email address")},
                            onValueChange = { emailText = it},
                            value = emailText,
                        )
                        Row{
                            Text(modifier = Modifier.padding(8.dp), text ="User")
                            Text(modifier = Modifier.padding(8.dp), text =user)
                        }
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                val words = emailText.split("@").filter {
                                    !it.isEmpty()
                                }
                                if(words.size == 2){
                                    user = words[0]
                                    domain = words[1]
                                    emailText = ""
                                }

                            }){
                            Text("Split Email")
                        }


                    }
                }
            }
        }
    }
}

//@Composable
//fun Header(value: String, modifier: Modifier = Modifier) {
//    Text(
//        text = value,
//        modifier = modifier,
//        textAlign = TextAlign.Center
//    )
//}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            BasicInteractionTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column(
//                        modifier = Modifier.padding(innerPadding)
//                    ){
//                        var emailText by remember{ mutableStateOf("") }
//                        var user by remember { mutableStateOf("")}
//                        var domain by remember{ mutableStateOf("")}
//
//                        OutlinedTextField(
//                            modifier = Modifier.padding(16.dp),
//                            label = {Text("email address")},
//                            onValueChange = { emailText = it},
//                            value = emailText,
//                        )
//                        Button(
//                            modifier = Modifier.padding(16.dp),
//                            onClick = {
//                                val words = emailText.split("@").filter {
//                                    !it.isEmpty()
//                                }
//                                if(words.size == 2){
//                                    user = words[0]
//                                    domain = words[1]
//                                    emailText = ""
//                                }
//
//                            }){
//                            Text("Split Email")
//                        }
//
//                        Row{
//                            Text(modifier = Modifier.padding(8.dp), text ="User")
//                            Text(modifier = Modifier.padding(8.dp), text =user)
//                        }
//
//
//                        Row{
//                            Text(modifier = Modifier.padding(8.dp), text ="Domain")
//                            Text(modifier = Modifier.padding(8.dp), text =domain)
//                        }
//
//
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BasicInteractionTheme {
//        Greeting("Android")
//    }
//}
