package com.donc.gu_utils.presentation.cardsearch

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.donc.gu_utils.data.models.ChipWithList

@Composable
fun CardSearchScreen(

) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        SearchSection()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Card Search")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchSection(){
    val filterList = listOf(
        ChipWithList(name = "God"),
        ChipWithList(name = "Mana"),
        ChipWithList(name = "Rarity"),
        ChipWithList(name = "Tribe"),
        ChipWithList(name = "Set"),
    )
    val selectedItems = mutableStateListOf<String>()
    var isSelected by remember { mutableStateOf(false) }
    LazyRow() {
        items(filterList) { item ->
            isSelected = selectedItems.contains(item.name)
            Spacer(modifier = Modifier.padding(5.dp))
            ChipWithSubItems(chipLabel = item.name, chipItems = item.subList!!)
        }
    }
}

@Composable
fun ChipWithSubItems(chipLabel: String, chipItems: List<String>) {
    var isSelected by remember { mutableStateOf(false) }
    var showSubList by remember { mutableStateOf(false) }
    var filterName by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = showSubList,
        onExpandedChange = { showSubList = !showSubList }
    ) {
        FilterChip(
            selected = isSelected,
            onClick = {
                isSelected = true
            },
            label = { Text(text = filterName.ifEmpty { chipLabel }) },
        )
        ExposedDropdownMenu(
            expanded = showSubList,
            onDismissRequest = { showSubList = false },
        ) {
            chipItems.forEach { subListItem ->
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        filterName = subListItem
                        showSubList = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (subListItem == filterName || subListItem == chipLabel) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else { Color.Transparent }
                    )
                ) {
                    Text(text = subListItem)
                }
            }
        }
    }
}