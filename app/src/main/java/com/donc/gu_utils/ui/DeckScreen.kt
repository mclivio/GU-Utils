package com.donc.gu_utils.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.donc.gu_utils.R
import com.donc.gu_utils.data.models.Record
import com.donc.gu_utils.presentation.CardSearchViewModel
import com.donc.gu_utils.util.DeckBuilder

@Composable
fun DeckScreen(
    navController: NavController,
    viewModel: CardSearchViewModel = hiltViewModel()
) {
    val cardList = viewModel.deck.value.cardList.sortedByDescending { it.name }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize()){
            HeaderSection(
                onNewDeck = {viewModel.newDeck(it)},
                onEncode = {
                    val deckString = DeckBuilder.getDeck()
                        ?.let { it1 -> DeckBuilder.encodeDeck(it1) } ?: ""
                    if (deckString.isBlank()) {
                        Toast.makeText(context, context.resources.getString(R.string.button_encodefails), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, context.resources.getString(R.string.button_encodesuccess), Toast.LENGTH_SHORT).show()
                        clipboardManager.setText(AnnotatedString((deckString)))
                    }
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            DeckSection(navController = navController, deck = cardList as MutableList<Record>, onDelete = {viewModel.deck.value.removeCard(it)})
        }
    }
}

@Composable
fun HeaderSection(onNewDeck: (String) -> Unit, onEncode: () -> Unit){
    val showDialog = remember {mutableStateOf(false)}
    GodDialog(
        showDialog = showDialog.value,
        dismissDialog = {showDialog.value = false},
        onConfirm = {
            onNewDeck(it)
        //When a god is selected in the radioGroup the value will be hoisted up to the Dialog,
        //once the dialog "confirm button" is pressed, it will hoist it up again and use the value
        //of the selected god to call the newDeck function of the viewModel with that string as a parameter
            showDialog.value = false
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                text = stringResource(R.string.deck_header),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
            IconButton(
                onClick = { onEncode() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_content_copy_24),
                    contentDescription = stringResource(id = R.string.description_copy)
                )
            }
        }
        Button(onClick = {
            showDialog.value = true
        }) {
            Text(
                text = stringResource(R.string.button_newdeck),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GodDialog(
    showDialog: Boolean,
    dismissDialog: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedGod = remember {mutableStateOf("nature")}
    if (showDialog) {
        selectedGod.value = "nature"
        Dialog(
            onDismissRequest = { dismissDialog() },
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.92f),
                color = Color.Transparent
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(percent = 10)
                            )
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))
                        Text("Seleccione un Dios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        RadioGroup(onOptionSelected = {
                            selectedGod.value = it
                        })
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = { dismissDialog() }) {
                                Text("Cancelar")
                            }
                            Button(onClick = { onConfirm(selectedGod.value) }) {
                                Text("Aceptar")
                                //When clicked, it will call the onConfirm() method with the god selected
                                //from the radioGroup as a parameter
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 65.dp), contentAlignment = Alignment.TopCenter){
                        Image(
                            painter = painterResource(id = godRoute(selectedGod.value)),
                            contentDescription = stringResource(id = R.string.description_god),
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .align(Alignment.TopCenter)
                        )
                    }
                    //The godRoute function returns a different resource according to the
                    //selected god, which causes the Image in the dialog to be dynamically changed
                }
            }
        }
    }
}

fun godRoute(selectedGod: String): Int {
    var route = 0
    when (selectedGod) {
        "nature" -> route = R.drawable.nature
        "war" -> route = R.drawable.war
        "magic" -> route = R.drawable.magic
        "deception" -> route = R.drawable.deception
        "light" -> route = R.drawable.light
        "death" -> route = R.drawable.death
    }
    return route
}

@Composable
fun RadioGroup(
    onOptionSelected: (String) -> Unit,
)
 {
    val gods = listOf("nature", "war", "magic", "deception", "light", "death")
    val selectedOption = remember { mutableStateOf(gods[0]) }
     Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
         gods.forEach{
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .selectable(
                         selected = (it == selectedOption.value),
                         onClick = {
                             onOptionSelected(it)
                             selectedOption.value = it
                         }
                     )
                     .padding(horizontal = 12.dp),
                 verticalAlignment = Alignment.CenterVertically
             ){
                 RadioButton(
                     selected = (it == selectedOption.value),
                     onClick = {
                         onOptionSelected(it)
                         selectedOption.value = it
                     },
                     modifier = Modifier.padding(6.dp)
                 )
                 Text(text = it)
             }
         }
     }
     //Since RadioButtons in compose don't have a "text" attribute you need to put them in a row
     //with a text that works as a label. The selected options of the buttons/rows is a state
     //that is hoisted outside of the composable so that it can be used in a function that is
     //further up in the stack of calls. This is the equivalent of saying that it "returns" the
     //selected god
}

@Composable
fun DeckSection(
    navController: NavController,
    deck: MutableList<Record>,
    onDelete: (Record) -> Unit
){
    LazyColumn(modifier = Modifier.fillMaxSize(1F)) {
        items(deck.size){ item ->
            CardRow(navController, deck[item], onDelete = {onDelete(it)})
        }
    }
}

@Composable
fun CardRow(
    navController: NavController,
    entry: Record,
    onDelete: (Record) -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clickable { navController.navigate(route = "details/${entry.id}") },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        ){
        Text(
            text = entry.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
        IconButton(onClick = { onDelete(entry) }) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(R.string.description_remove),
            )
        }
    }
}