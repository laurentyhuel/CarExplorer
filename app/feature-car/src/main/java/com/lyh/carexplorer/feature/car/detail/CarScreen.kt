package com.lyh.carexplorer.feature.car.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
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
fun CarRoute(
    modifier: Modifier = Modifier,
    viewModel: CarViewModel = getViewModel(),
    onBackClick: () -> Unit,
) {
    val carResource by viewModel.car.collectAsStateWithLifecycle()

    CarScreen(
        modifier = modifier,
        carResource = carResource,
        onBackClick = onBackClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarScreen(
    modifier: Modifier = Modifier,
    carResource: Resource<CarUi>,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(id = RCore.string.app_name),
                onBackClick = onBackClick
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (carResource) {
                is ResourceError -> ErrorComponent(
                    errorText = carResource.errorMessage.getMessage(
                        LocalContext.current
                    ),
                    retry = null,
                )
                is ResourceLoading -> LoadingComponent(loadingText = stringResource(id = RCore.string.loading_data))
                is ResourceSuccess -> CarDetail(
                    car = carResource.data,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarDetail(
    modifier: Modifier = Modifier,
    car: CarUi,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { CarFooter(car) },
        topBar = { CarHeader(car) }
    ) { innerPadding ->
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = car.make,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 20.dp,
                        end = 20.dp
                    )
            )
            Text(
                text = "${car.year}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        bottom = innerPadding.calculateBottomPadding(),
                        start = 20.dp,
                        end = 20.dp
                    )
            )
        }

    }
}

@Composable
private fun CarHeader(car: CarUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = car.picture,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()

        )
        Text(
            text = car.model,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Composable
private fun CarFooter(
    car: CarUi
) {
    if (car.equipments.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
        ) {

            car.equipments.forEach { equipment ->
                Text(text = equipment)
            }
        }
    }
}
