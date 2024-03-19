package ru.tic_tac_toe.zoomparty.presentation.sreens

import android.graphics.BlendModeColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.tic_tac_toe.zoomparty.domain.Configuration
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import ru.tic_tac_toe.zoomparty.presentation.ServiceViewModel
import ru.tic_tac_toe.zoomparty.presentation.navigation.Route
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeLarge
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeSmall
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.nUiPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.sUiPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleAboutTextBold
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleLargeText
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.supPadding


@Composable
fun SettingScreen(serviceViewModel: ServiceViewModel, navController: NavHostController) {
    var isMasterProfile by remember { mutableStateOf(Configuration.profileDevice == WorkProfile.MASTER) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sUiPadding.dp, end = sUiPadding.dp, top = nUiPadding.dp, bottom = nUiPadding.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectMasterSlave(callback = { value -> isMasterProfile = value })
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val workProfile = if (isMasterProfile) WorkProfile.MASTER else WorkProfile.SLAVE
                serviceViewModel.connectionWithRemoteService(workProfile, null)
                navController.navigate(Route.Game.name)
            },
            modifier = Modifier.padding(bottom = sUiPadding.dp)
        ) {
            Text(text = "Далее")
        }
    }
}

@Composable
fun SelectMasterSlave(callback: (Boolean) -> Unit) {
    val radioOptions = listOf(WorkProfile.MASTER.mName, WorkProfile.SLAVE.mName)
    val selected = if (Configuration.profileDevice == WorkProfile.MASTER) 0 else 1
    var selectedOption by remember { mutableStateOf(radioOptions[selected]) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Это устройство выступит как: ", style = styleLargeText)
        Spacer(modifier = Modifier.size(sUiPadding.dp))
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            radioOptions.forEach { choiceName ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        modifier = Modifier.size(supPadding.dp),
                        selected = (choiceName == selectedOption),
                        onClick = {
                            selectedOption = choiceName
                            val isMasterProfile = choiceName == WorkProfile.MASTER.mName
                            callback.invoke(isMasterProfile)
                        }
                    )
                    Text(
                        text = choiceName,
                        fontSize = fontSizeLarge.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = nUiPadding.dp)
                    )
                    Spacer(modifier = Modifier.size(sUiPadding.dp))
                }
            }
        }

    }
}