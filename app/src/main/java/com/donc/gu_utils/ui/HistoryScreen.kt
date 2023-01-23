package com.donc.gu_utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donc.gu_utils.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.donc.gu_utils.presentation.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize()){
            SearchSection{
                viewModel.loadLatestMatches(it)
            }
            Spacer(modifier = Modifier.height(4.dp))
            MatchList()
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
fun MatchList(){

}