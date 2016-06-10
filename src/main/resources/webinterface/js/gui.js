var gView;
var lastPressedX;
var lastPressedY;

// ================================================================================================
// ======================================== VIEW SWITCHING ========================================
// ================================================================================================

/**
 * Switches to the given view.
 *
 * @param view
 *      the view to switch to. Must be an element of Views [ENUM]
 */
function switchTo(view) {
    //We don't need to switch to our current state.
    if (gView === view) return;
    gView = view;
    
    //Some things we need to do for all switches.
    hideGameButtons();
    
    switch (view) {
        case Views.INDEX:
            switchToIndex();
            break;
        case Views.INSTRUCTION:
            switchToInstructions();
            break;
        case Views.TEAM:
            switchToTeam();
            break;
        case Views.GAME:
            switchToGame();
            break;
        case Views.PAUSED:
            switchToPaused();
            break;
        case Views.ENDED:
            switchToEnded();
            break;
        case Views.TUTORIAL:
            switchToTutorial();
            break;
        default:
            showAlert("Unknown view: " + view, Severity.Danger, 5000);
            break;
    }
}

/**
 * Manages switching to the index view.
 */
function switchToIndex() {
    startIndexRefresh();
    stopStatusUpdates();
    
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("gameView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("indexView")).show();
}

/**
 * Manages switching to the instructions view.
 */
function switchToInstructions() {
    startIndexRefresh();
    stopStatusUpdates();
    
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("gameView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("tutorialView")).hide();
    $(document.getElementById("instructionsView")).show();
}

/**
 * Manages switching to the team view.
 */
function switchToTeam() {
    stopIndexRefresh();
    startStatusUpdates();
    
    updateTeam(undefined);
    
    requestMap();
    
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("tutorialView")).hide();
    $(document.getElementById("gameView")).show();
    $(document.getElementById("teamView")).show();
}

/**
 * Manages switching to the tutorial view.
 */
function switchToTutorial() {
    stopIndexRefresh();
    startStatusUpdates();
    
    requestMap();
    
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("gameView")).hide();
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("tutorialView")).show();
}

/**
 * Manages switching to the game view.
 */
function switchToGame() {
    stopIndexRefresh();
    startStatusUpdates();

    requestMap();
    
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("tutorialView")).hide();
    $(document.getElementById("gameView")).show();
}

/**
 * Manages switching to the paused view.
 */
function switchToPaused() {
    stopIndexRefresh();
    startStatusUpdates();
    
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("gameView")).hide();
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("endedView")).hide();
    $(document.getElementById("tutorialView")).hide();
    $(document.getElementById("pausedView")).show();
}

/**
 * Manages switching to the ended view.
 */
function switchToEnded() {
    stopIndexRefresh();
    startStatusUpdates();
    
    $(document.getElementById("gameView")).hide();
    $(document.getElementById("teamView")).hide();
    $(document.getElementById("indexView")).hide();
    $(document.getElementById("instructionsView")).hide();
    $(document.getElementById("pausedView")).hide();
    $(document.getElementById("tutorialView")).hide();
    $(document.getElementById("endedView")).show();
}

// ================================================================================================
// ============================= INDEX AND INSTRUCTION - JOIN BUTTONS =============================
// ================================================================================================

function updateJoinButtons() {
    var btnJoin = $(".btnJoinGame");

    if (gCanJoin === true) {
        btnJoin.removeClass("disabled");
    } else if (gCanJoin === false) {
        btnJoin.addClass("disabled");
    }
}

// ================================================================================================
// ================================== TEAM - CHOOSE TEAM BUTTONS ==================================
// ================================================================================================

/**
 * Updates the team view.
 */
function updateTeamButtons() {
    var elvesButton = $(document.getElementById("elvesButton"));
    var dwarfsButton = $(document.getElementById("dwarfsButton"));
    var noneButton = $(document.getElementById("noneButton"));
    
    elvesButton.removeClass("btn-success");
    dwarfsButton.removeClass("btn-success");
    noneButton.removeClass("btn-success");
    
    elvesButton.addClass("btn-default");
    dwarfsButton.addClass("btn-default");
    noneButton.addClass("btn-default");
    
    if (gTeam === "DWARFS") {
        dwarfsButton.removeClass("btn-default");
        dwarfsButton.addClass("btn-success");
    } else if (gTeam === "ELVES") {
        elvesButton.removeClass("btn-default");
        elvesButton.addClass("btn-success");
    } else {
        noneButton.removeClass("btn-default");
        noneButton.addClass("btn-success");
    }
}

// ================================================================================================
// ==================================== GAME - SIDEBAR BUTTONS ====================================
// ================================================================================================

/**
 * Shows the game button menu depending on what team we are in.
 */
function showGameButtons() {
    editLegend();
    
    if (gTeam === "DWARFS") {
        $("#sidebar-wrapper-dwarfs").css("visibility", "visible");
        $("#wrapper").toggleClass("toggled", true);
    } else if (gTeam === "ELVES") {
        $("#sidebar-wrapper-elves").css("visibility", "visible");
        $("#wrapper").toggleClass("toggled", true);
    } else {
        showAlert("You are not in a team!", Severity.Warning);
    }
}

/**
 * Changes the legend to represent the currently selected tile.
 */
function editLegend() {
    var selected;
    
    if (gTeam === "DWARFS") {
        selected = document.getElementById("clickedDwarfs");
    } else if (gTeam === "ELVES") {
        selected = document.getElementById("clickedElves");
    } else {
        return;
    }
    
    var classes = document.getElementById("y" + lastPressedY + "x" + lastPressedX).className.split(" ");

    if ((classes.indexOf("explored") === -1) && (classes.length < 2)) {
        selected.innerHTML = "Selected: Unexplored";
    } else {
        var newInner = classes[classes.length - 1];
        if (newInner === "explored") newInner = classes[classes.length - 2];
        if (newInner == null) newInner = "Void";
        selected.innerHTML = "Selected: " + newInner.replace("_", " ");
    }
}

/**
 * Hide the game buttons (sidebar).
 */
function hideGameButtons() {
    $("#wrapper").toggleClass("toggled", false);
    $("#sidebar-wrapper-dwarfs").css("visibility", "hidden");
    $("#sidebar-wrapper-elves").css("visibility", "hidden");
}

// ================================================================================================
// ======================================== ENDED - WINNER ========================================
// ================================================================================================

/**
 * Displays the winner of the game.
 *
 * @param data
 *      the data sent from the server
 */
function displayWinner(data) {
    var winHeader = document.getElementById("endedWinners");
    var winMessage = document.getElementById("endedMessage");
    var winner = (data.winner ? "ELVES" : "DWARFS");
    winHeader.innerHTML = (data.winner ? "Elves won!" : "Dwarfs won!");
    
    if (winner === Teams[data.team]) {
        winMessage.innerHTML = "Congratulations!";
    } else {
        winMessage.innerHTML = "Better luck next time!";
    }
}

// ================================================================================================
// ============================================ ERRORS ============================================
// ================================================================================================

/**
 * Displays the given error message to the user.
 *
 * @param msg
 *      the message to display
 * @param severity
 *      the severity of the error. Must be from the Severity enum
 * @param time
 *      [OPTIONAL] the amount of time in ms to show the message for
 */
function showAlert(msg, severity, time) {
    var element = document.getElementById(severity);
    element.innerHTML = msg;
    
    $(element).fadeIn();
    window.setTimeout(function() {
       $(element).fadeOut(300) 
    }, time === undefined ? Constants.Intervals.ALERT : time);
}

// ================================================================================================
// ========================================== FULLSCREEN ==========================================
// ================================================================================================

/**
 * Function to toggle the webpage to fullscreen.
 * The enableFullScreen and disableFullScreen methods used by this method were
 * copied from https://developer.mozilla.org/en-US/docs/Web/API/Fullscreen_API
 */
function toggleFullscreen() {
    if (!document.fullscreenElement && !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {
        enableFullScreen();
    } else {
        disableFullScreen();
    }
    hideGameButtons();
}

/**
 * Enables fullscreen.
 */
function enableFullScreen() {
    if (document.documentElement.requestFullscreen) {
        document.documentElement.requestFullscreen();
    } else if (document.documentElement.msRequestFullscreen) {
        document.documentElement.msRequestFullscreen();
    } else if (document.documentElement.mozRequestFullScreen) {
        document.documentElement.mozRequestFullScreen();
    } else if (document.documentElement.webkitRequestFullscreen) {
        document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
    }
}

/**
 * Disables fullscreen.
 */
function disableFullScreen() {
    if (document.exitFullscreen) {
        document.exitFullscreen();
    } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
    }
}