package com.lyh.carexplorer.feature.car.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lyh.carexplorer.feature.car.R
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.core.Resource
import com.lyh.carexplorer.feature.core.ResourceError
import com.lyh.carexplorer.feature.core.ResourceLoading
import com.lyh.carexplorer.feature.core.ResourceSuccess
import com.lyh.carexplorer.feature.core.ui.AppTopBar
import com.lyh.carexplorer.feature.core.ui.ErrorComponent
import com.lyh.carexplorer.feature.core.ui.LoadingComponent
import org.koin.androidx.compose.getViewModel
import com.lyh.carexplorer.feature.core.R as RCore

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CarListRoute(
    modifier: Modifier = Modifier,
    onNavigateToCar: (carId: String) -> Unit,
    viewModel: CarListViewModel = getViewModel()
) {

    val state by viewModel.cars.collectAsStateWithLifecycle()

    CarListScreen(
        modifier = modifier,
        viewModel = viewModel,
        state,
        retry = viewModel::triggerCars,
        onNavigateToCar = onNavigateToCar
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun CarListScreen(
    modifier: Modifier = Modifier,
    viewModel: CarListViewModel,
    state: Resource<List<CarUi>>,
    retry: () -> Unit,
    onNavigateToCar: (carId: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(id = RCore.string.app_name),
                onBackClick = null
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
            ) {
                val search = viewModel.textSearch.collectAsStateWithLifecycle()

                OutlinedTextField(
                    value = search.value,
                    onValueChange = { viewModel.setSearchText(it) },
                    placeholder = {
                        Text(
                            stringResource(id = R.string.search)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = RCore.drawable.search),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingIcon = {
                        if (search.value.isNotEmpty()) {
                            Icon(
                                painter = painterResource(id = RCore.drawable.cross),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.clickable { viewModel.setSearchText("") }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                )
                when (state) {
                    is ResourceError -> ErrorComponent(
                        errorText = state.errorMessage.getMessage(
                            LocalContext.current
                        ),
                        retry = retry,
                        modifier = Modifier.padding(10.dp)
                    )
                    is ResourceLoading -> LoadingComponent(
                        loadingText = stringResource(id = RCore.string.loading_data),
                        modifier = Modifier.padding(10.dp)
                    )
                    is ResourceSuccess -> CarList(
                        cars = state.data,
                        onNavigateToCar = onNavigateToCar,
                        search = search,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CarList(
    modifier: Modifier = Modifier,
    cars: List<CarUi>,
    search: State<String>,
    onNavigateToCar: (carId: String) -> Unit
) = LazyColumn(
    modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
) {
    cars.forEach {
        item(key = it.id) {
            CarItemCard(carUi = it, onClick = onNavigateToCar, search = search)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarItemCard(
    modifier: Modifier = Modifier,
    carUi: CarUi,
    search: State<String>,
    onClick: (carId: String) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = CardDefaults.elevatedShape,
        onClick = { onClick(carUi.id.toString()) },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            AsyncImage(
                model = carUi.picture,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Column(

                modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            ) {
                Text(
                    HighlightText(carUi.model, search.value),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    HighlightText(carUi.make, search.value),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun HighlightText(text: String, query: String) =
    buildAnnotatedString {
        val query = query.lowercase()
        var start = 0
        while (text.lowercase().indexOf(query.lowercase(), start, ignoreCase = true) != -1 && query.isNotBlank()
        ) {
            val firstIndex = text.lowercase().indexOf(query.lowercase(), start, true)
            val end = firstIndex + query.length
            append(text.substring(start, firstIndex))
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onErrorContainer)) {
                append(text.substring(firstIndex, end))
            }
            start = end
        }
        append(text.substring(start, text.length))
        toAnnotatedString()
    }
