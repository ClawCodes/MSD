package com.example.maraca.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomBanner(
    modifier: Modifier = Modifier,
    onDeleteN: (Int?) -> Unit,
    onDeleteAll: () -> Unit
) {
    var textValue by remember { mutableStateOf("") }
    var enteredInteger by remember { mutableStateOf<Int?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    textValue = newValue
                    enteredInteger = newValue.toIntOrNull()
                }
            },
            label = { Text("Num to delete") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(0.5f)
        )
        Button(onClick = {
            textValue = ""
            onDeleteN(enteredInteger)
        }) {
            Text("Delete")
        }
        Button(onClick = {
            onDeleteAll()
        }) {
            Text("Delete All Records")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBannerPreview() {
    BottomBanner(
        onDeleteN = { number ->
            if (number != null) {
                println("Entered number: $number")
            } else {
                println("No valid number entered.")
            }
        },
        onDeleteAll = {
            println("Delete All pressed")
        }
    )
}
