package com.fahim.towerofhanoi.ui.hanoi

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fahim.towerofhanoi.R
import com.fahim.towerofhanoi.game.HanoiState
import com.fahim.towerofhanoi.game.MIN_DISKS
import com.fahim.towerofhanoi.game.TapEvent
import com.fahim.towerofhanoi.game.hint
import com.fahim.towerofhanoi.game.pegLabel
import com.fahim.towerofhanoi.game.tap
import com.fahim.towerofhanoi.game.undo
import kotlinx.coroutines.delay

private const val TICK_MS = 1_000L
private const val TOAST_DURATION_MS = 1_800L
private val MaxContentWidth = 480.dp

@Composable
fun HanoiScreen(modifier: Modifier = Modifier) {
    var game by remember { mutableStateOf(HanoiState.new(MIN_DISKS)) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var soundOn by remember { mutableStateOf(true) }
    var toast by remember { mutableStateOf<ToastMessage?>(null) }
    val view = LocalView.current

    val timerRunning = game.moveCount > 0 && !game.isSolved
    LaunchedEffect(timerRunning) {
        while (timerRunning) {
            delay(TICK_MS)
            elapsedSeconds++
        }
    }
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(TOAST_DURATION_MS)
            toast = null
        }
    }

    fun click() {
        if (soundOn) view.playSoundEffect(SoundEffectConstants.CLICK)
    }

    fun startNewGame(diskCount: Int) {
        game = HanoiState.new(diskCount)
        elapsedSeconds = 0
        toast = null
    }

    fun onPegTap(peg: Int) {
        val result = game.tap(peg)
        game = result.state
        when (result.event) {
            TapEvent.MOVED -> click()
            TapEvent.SOLVED -> {
                click()
                toast = ToastMessage(R.string.toast_solved)
            }
            TapEvent.INVALID -> toast = ToastMessage(R.string.toast_invalid_move)
            TapEvent.IGNORED, TapEvent.SELECTED, TapEvent.DESELECTED -> Unit
        }
    }

    fun onUndo() {
        val previous = game.undo()
        if (previous == null) {
            toast = ToastMessage(R.string.toast_nothing_to_undo)
        } else {
            game = previous
            click()
        }
    }

    fun onHint() {
        val move = game.hint()
        toast = if (move == null) {
            ToastMessage(R.string.toast_already_solved)
        } else {
            ToastMessage(R.string.toast_hint, pegLabel(move.from), pegLabel(move.to))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HanoiColors.Surface)
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            Modifier
                .widthIn(max = MaxContentWidth)
                .fillMaxSize(),
        ) {
            HanoiTopBar(
                soundOn = soundOn,
                onToggleSound = { soundOn = !soundOn },
                onShowRules = { toast = ToastMessage(R.string.toast_goal) },
            )
            HanoiBody(
                game = game,
                elapsedSeconds = elapsedSeconds,
                toast = toast,
                onSelectDifficulty = ::startNewGame,
                onPegTap = ::onPegTap,
                onReset = { startNewGame(game.diskCount) },
                onUndo = ::onUndo,
                onHint = ::onHint,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Scrollable on short screens; otherwise spreads header, board and controls over the height. */
@Composable
private fun HanoiBody(
    game: HanoiState,
    elapsedSeconds: Int,
    toast: ToastMessage?,
    onSelectDifficulty: (Int) -> Unit,
    onPegTap: (Int) -> Unit,
    onReset: () -> Unit,
    onUndo: () -> Unit,
    onHint: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ScoreBoard(
                    moveCount = game.moveCount,
                    optimalMoves = game.optimalMoves,
                    elapsedSeconds = elapsedSeconds,
                    efficiencyPercent = game.efficiencyPercent,
                )
                DifficultySelector(
                    selectedDiskCount = game.diskCount,
                    onSelect = onSelectDifficulty,
                )
            }
            HanoiBoard(
                state = game,
                toast = toast,
                onPegTap = onPegTap,
                modifier = Modifier.padding(vertical = 12.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionControls(onReset = onReset, onUndo = onUndo, onHint = onHint)
                RulesCard()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HanoiScreenPreview() {
    HanoiScreen()
}
