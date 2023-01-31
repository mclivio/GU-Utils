package com.donc.gu_utils.ui

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donc.gu_utils.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.donc.gu_utils.data.models.RecordMatch
import com.donc.gu_utils.presentation.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val matchList = viewModel.matchRecords.sortedByDescending { it.end_time }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize()){
            SearchSection{
                viewModel.loadLatestMatches(it)
            }
            Spacer(modifier = Modifier.height(4.dp))
            MatchList(matchList)
        }
    }
}

@Composable
fun SearchSection(
    onSearch: (String) -> Unit
){
    val text = remember {mutableStateOf("")}
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = {text.value = it},
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            decorationBox = { innerTextField ->
                /* Added only because BasicTextField doesn't have an Icon and the classic TextField
                 has a default design that doesn't seem good for a search bar */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onSearch(text.value) }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.description_search),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        if (text.value.isEmpty()) Text(
                            stringResource(R.string.hint_searchbyid),
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                fontStyle = FontStyle.Italic
                            )
                        )
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun MatchList(
    history: List<RecordMatch>
) {
    val columnsPlayersWeight = .4f
    val columnTurnsWeight = .2f
    LazyColumn(modifier = Modifier.fillMaxSize(1F)) {
        // These are the "Header" cells
        item {
            Row(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 6.dp)
            ) {
                Cell(text = "Winner", weight = columnsPlayersWeight, withPic = false, "")
                Cell(text = "Loser", weight = columnsPlayersWeight, withPic = false, "")
                Cell(text = "Turns", weight = columnTurnsWeight, withPic = false, "")
            }
        }
        // These are the actual values
        items(history.size) { item ->
            Row(
                Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Cell(
                    text = history[item].player_info[0].user_id.toString(),
                    weight = columnsPlayersWeight,
                    withPic = true,
                    history[item].player_info[0].god
                )
                Cell(
                    text = history[item].player_info[1].user_id.toString(),
                    weight = columnsPlayersWeight,
                    withPic = true,
                    history[item].player_info[1].god
                )
                Cell(
                    text = history[item].total_turns.toString(),
                    weight = columnTurnsWeight,
                    withPic = false,
                    ""
                )
            }
        }
    }
}

@Composable
fun RowScope.Cell(
    text: String,
    weight: Float,
    withPic: Boolean,
    selectedGod: String,
) {
    Row(
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .weight(weight)
            .padding(6.dp)){
        if (withPic){
            Image(
                painter = painterResource(id = godRoute(selectedGod)),
                contentDescription = stringResource(id = R.string.description_god),
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .align(Alignment.CenterVertically)
            )
            //If its a cell which should have a Pic, it would receive a "God" used by the player
            //and according to the selectedGod it chooses a route to the corresponding pic
        }
        Text(text = text, modifier = Modifier.padding(6.dp))
    }
}