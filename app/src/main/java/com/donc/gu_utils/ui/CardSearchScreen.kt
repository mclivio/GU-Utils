package com.donc.gu_utils.ui

import android.os.Handler
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.donc.gu_utils.R
import com.donc.gu_utils.data.models.ChipWithList
import com.donc.gu_utils.data.models.Record
import com.donc.gu_utils.presentation.CardSearchViewModel
import com.donc.gu_utils.util.Constants.BASE_PIC_URL

@Composable
fun CardSearchScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize()){
            SearchSection()
            Spacer(modifier = Modifier.height(4.dp))
            CardList(navController = navController)
        }
    }
}

@Composable
fun SearchSection(viewModel: CardSearchViewModel = hiltViewModel()){
    val filterList = listOf(
        ChipWithList(
            name = "God",
            subList = listOf("light", "death", "nature", "war", "magic", "deception"),
            selected = false
        ),
        ChipWithList(
            name = "Mana",
            subList = listOf("0", "1" , "2", "3", "4", "5", "6", "7", "8", "9", "10"),
            selected = false
        ),
        ChipWithList(
            name = "Rarity",
            subList = listOf("common", "rare", "epic", "legendary", "mythic"),
            selected = false
        ),
        ChipWithList(
            name = "Tribe",
            subList = listOf("nether", "aether", "atlantean", "viking", "olympian", "anubian", "amazon"),
            selected = false
        ),
    )
    var selectedChips = remember {mutableStateListOf<ChipWithList>()}
    Column{
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(top = 4.dp)
        ) {
            //For each of those titles and lists of items, a "Chip with sub-items" will be created
            filterList.forEach { chip ->
                var selected = selectedChips.contains(chip)
                //Since this value depends on the selectedChips list, if the list is cleared it will
                //be set to false on recomposition, thus allowing the use of a "clear filters" button
                //which should just clear the selectedChips list
                var showSubList by remember { mutableStateOf(false) }
                var filterName by remember { mutableStateOf(chip.name) }
                //Setting default text to the chip's name
                var color = MaterialTheme.colorScheme.primary
                if (selected) {
                    color = MaterialTheme.colorScheme.secondary
                }
                Box(contentAlignment = Alignment.Center){
                    FilterChip(
                        selected = selected,
                        onClick = {
                            showSubList = true
                            //When clicked, the showSubList state will be set to true and a dropdownmenu will show
                        },
                        label = { Text(text = if(selected) filterName else chip.name) },
                        //If it is selected, it will just use the filterName which changes according
                        //to the selected item and otherwise, it would just use the chip name.
                        colors = FilterChipDefaults.filterChipColors( selectedLabelColor =  color, selectedContainerColor = MaterialTheme.colorScheme.background),
                        border = FilterChipDefaults.filterChipBorder(borderColor = color, selectedBorderColor = color)
                    )
                    DropdownMenu(
                        expanded = showSubList,
                        onDismissRequest = { showSubList = false })
                    //After clicking a FilterChip, showSubList will be true and the menu will expand
                    //When clicking outside the menu showSublist will be set to false so it will close
                    {
                        chip.subList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    showSubList = false
                                    if (!selectedChips.contains(chip)) selectedChips.add(chip)
                                    selected = true
                                    filterName = item
                                    viewModel.updateFilters(chip.name, item)
                                    viewModel.clearCardRecords()
                                    viewModel.loadCardsPaginated()
                                    //If an item is selected, it will also change showSubList to false, closing the menu
                                    //In addition, it sets a new filterName's value and isSelected to true changing both the label
                                    //and the color of the FilterChip as well as adding it to the list of selectedChips if it
                                    //isn't there. Since this will mean that a filter has been selected, it will be updated in the
                                    //viewModel and a search with the selected filters will be triggered.
                                }
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(10.dp, 0.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End
        )
        {
            Text(
                text = stringResource(R.string.clickable_clearfilter),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        selectedChips.clear()
                        //Clearing the list, since the values are being remembered and observed,
                        //it will trigger a recomposition with the updated list which is empty.
                        viewModel.clearFilters()
                        viewModel.clearCardRecords()
                        viewModel.loadCardsPaginated()
                    }
                )
            )
        }
    }
}

@Composable
fun CardList(
    navController: NavController,
    viewModel: CardSearchViewModel = hiltViewModel()
){
    val cardList by remember {viewModel.cardRecords}
    val endReached by remember {viewModel.endReached}
    val isLoading by remember {viewModel.isLoading}
    val loadError by remember {viewModel.loadError}
    val cardsAmount by remember {viewModel.cardsAmount}

    var toast = Toast.makeText(LocalContext.current, "$cardsAmount "+ stringResource(R.string.toast_cardsfound), Toast.LENGTH_SHORT)
    LaunchedEffect(cardsAmount) {
        toast.show()
        val handler : Handler = Handler()
        handler.postDelayed({ toast.cancel() }, 500)
        //On each recomposition the toast will show and after half a second it will disappear.
        //While usually one would just use Toast.LENGTH_SHORT, it has a duration of 2000ms or 2 seconds;
        //this means that if one were to select all 4 filters very fast then 4 Toasts would show up
        //and last for a total of 8 seconds on the screen which is A LOT. Since it is just used to show
        //the number of cards found reducing the duration by cancelling them after 500ms seems good.
    }


    LazyColumn(contentPadding = PaddingValues(12.dp, 0.dp)){
        val itemCount = if (cardList.size % 2 == 0) {
            cardList.size / 2
        } else {
            cardList.size / 2 +1
        }
        items(itemCount) {
            if (it >= itemCount -1 && !endReached && !isLoading){
                viewModel.loadCardsPaginated()
                //The reason isLoading is there is because these are async, which means the user could
                //scroll down while loading is still in progress, the current page wouldn't be already
                //updated and the same request would be send again to the API. This would cause it to show
                //duplicates of cards that are already in the screen, just not already loaded.
            }
            //If it isn't already loading anything and it hasn't reached the end of the list
            //then there are still more cards to show, so it loads them and makes a new row
            CardRow(rowIndex = it, cardList, navController)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCardsPaginated()
            }
        }
        //If it is still loading it will show it, otherwise if it finished loading and there was
        //an error, a retry button will appear which will call the card loading function from the
        //viewmodel one more time.
    }
}

@Composable
fun CardRow(
    rowIndex: Int,
    entries: List<Record>,
    navController: NavController
) {
    Column{
        Row{
            //Shows a card on the Left part of the row
            CardEntry(
                entry = entries[rowIndex *2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            //Shows it on the right if there are more cards or this is the last one and a 2nd one on the row
            if (entries.size >= rowIndex * 2 + 2) {
                CardEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
                //Otherwise, the last one was on the left part already and fills the right part
                //of the row with a space
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CardEntry (
    entry: Record,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { navController.navigate(route = "card_details/${entry.id}") }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
        //There is a clickable section of the same size as shape as the card, so clicking anywhere
        //inside it, would send the user to a new screen with the card details.
        , shape = RoundedCornerShape(10.dp)
    ){
        Box{
            Column{
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(BASE_PIC_URL + entry.id)
                        .placeholder(CircularProgressDrawable(LocalContext.current))
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.description_card) + entry.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                )
                Text(
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    text = entry.name.uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp, 0.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 2.dp)
                ) {
                    //TODO Deck Building controls
                    var quantity by remember {mutableStateOf(0)}
                    FilledIconButton(
                        onClick = { quantity-- },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_remove_24),
                            contentDescription = stringResource(id = R.string.description_remove)
                        )
                    }
                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .align(CenterVertically)
                        )
                    FilledIconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.description_add)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = MaterialTheme.colorScheme.error, fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.button_retry))
        }
    }
}