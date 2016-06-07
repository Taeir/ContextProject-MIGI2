var statusUpdateInterval;
var statusTimeoutCount = 0;
var indexRefreshInterval;

// ================================================================================================
// =========================================== INTERVAL ===========================================
// ================================================================================================
/**
 * Start the index refresh requests.
 */
function startIndexRefresh() {
    if (indexRefreshInterval === undefined) {
        indexRefreshInterval = setInterval(indexRefresh, Constants.Intervals.INDEX_REFRESH);
    }
}

/**
 * Stop the index refresh requests.
 */
function stopIndexRefresh() {
    if (indexRefreshInterval !== undefined) {
        clearInterval(indexRefreshInterval);
        indexRefreshInterval = undefined;
    }
}

/**
 * Start the status update requests.
 */
function startStatusUpdates() {
    if (supportsWebSockets === false && statusUpdateInterval === undefined) {
        statusUpdateInterval = setInterval(requestStatusUpdate, Constants.Intervals.STATUS_UPDATE);
    }
}

/**
 * Stop the status update requests.
 */
function stopStatusUpdates() {
    if (statusUpdateInterval !== undefined) {
        clearInterval(statusUpdateInterval);
        statusUpdateInterval = undefined;
    }
}

// ================================================================================================
// =========================================== REQUESTS ===========================================
// ================================================================================================

/**
 * Requests an index refresh.
 *
 * The given callback is called with either true, false, or a GameState, to indicate what the server replied.
 * true = you can still join.
 * false = you cannot join / error occurred.
 * GameState = you are already in the game, and should go to the given state view.
 *
 * The callback WILL always be called.
 *
 * @param callback
 *      function that will be called when the server has responded
 */
function requestIndexRefresh(callback) {
    $.ajax({
        type: "POST",
        url: "/indexrefresh",
        success: function(data) {
            if (data === "y") {
                callback(true);
            } else if (data === "n") {
                callback(false);
            } else {
                callback(GameStates[data]);
            }
        },
        error: function(jqxhr, status, error) {
            callback(false);
            handleGlobalRequestError(jqxhr, status, error);
        }
    });
};

/**
 * Requests the server to join the game.
 * 
 * On success, this function calls handleJoinGame.
 * On failure, this function calls handleGlobalRequestError.
 */
function requestJoinGame() {
    $.ajax({
        type: "POST",
        url: "/login",
        success: handleJoinGame,
        error: handleGlobalRequestError
    });
}

/**
 * Requests the server to set the team.
 *
 * On success, this function calls handleSetTeam.
 * On failure, this function calls handleGlobalRequestError.
 *
 * @param team
 *      the team to set
 */
function requestSetTeam(team) {
    //Send a post request to the server to set the team
    $.ajax({
        type: "POST",
        url: "/setteam",
        data: {team: team},
        success: handleSetTeam,
        error: handleGlobalRequestError
    });
}

/**
 * Requests the current status from the server.
 *
 * On success, this function calls handleStatusUpdateMessage.
 * On failure, this function calls handleStatusUpdateRequestError.
 */
function requestStatusUpdate() {
    $.ajax({
        type: "POST",
        url: "/status", 
        dataType: "json",
        success: handleStatusUpdateMessage,
        error: handleStatusUpdateRequestError
    });
}

/**
 * Requests the map from the server.
 *
 * On success, this function calls handleMap.
 * On failure, this function calls handleGlobalRequestError.
 */
function requestMap() {
    console.log("[DEBUG] GETTING MAP");
    $.ajax({
        type: "POST",
        url: "/map",
        dataType: "json",
        success: handleMap,
        error: handleGlobalRequestError
    });
}

/**
 * Sends an action request to the server.
 *
 * On success, this function calls handleActionResponse.
 * On failure, this function calls handleGlobalRequestError.
 */
function requestAction(argument) {
    //[UTIL] encode the action
    var encoded = encodeAction(argument);
    if (encoded === -1) return;
    
    console.log("[DEBUG] Requesting action: " + argument + ".");
    $.ajax({
        type: "POST",
        url: "/requestaction",
        data: {x: lastPressedX, y: lastPressedY, action: encoded},
        success: handleActionResponse,
        error: handleGlobalRequestError
    });
    
    //[GUI] Hide the menu
    hideAllButtons();
}

// ================================================================================================
// =========================================== HANDLERS ===========================================
// ================================================================================================

/**
 * Handles join game responses from the server.
 *
 * @param data
 *      the data sent from the server. Either "ALLOWED", a GameState, or an ErrorCode
 */
function handleJoinGame(data) {
    if (data === "ALLOWED") {
        //Switch to team selection
        switchTo(Views.TEAM);
    } else {
        var state = GameStates[data];
        if (state !== undefined) {
            switchTo(state.view);
        } else {
            handleError(data);
        }
    }
}

/**
 * Handles set team responses from the server.
 
 * @param data
 *      the data sent from the server. Either the ordinal of the team to switch to, or an ErrorCode
 */
function handleSetTeam(data) {
    var nTeam = Teams[data];
    if (nTeam == undefined) {
        //Switch to the INDEX view when the client is not in the current game.
        handleErrorCode(data, function() { switchTo(Views.INDEX) });
        return;
    }
    
    //Update the team only if it changed
    if (gTeam != nTeam) {
        gTeam = nTeam;
        updateTeam();
    }
}

/**
 * Handles server status update messages.
 *
 * @param data
 *      an object containing the information sent by the server
 */
function handleStatusUpdateMessage(data) {
    //Reset timeout count
    statusTimeoutCount = 0;
    
    //Check for errors
    if (data.error != undefined) {
        handleErrorCode(data.error, function() { switchTo(Views.INDEX) });
        return;
    }
    
    //Update the team
    var team = Teams[data.team];
    if (team != gTeam) {
        gTeam = team;
        updateTeam();
    }
    
    //Switch to the correct view
    var view = GameStates[data.state].view;
    if (view != gView) {
        switchTo(view);
    }
    
    //Update the game with the received information
    updateGame(data);
}

/**
 * Handles map responses from the server.
 *
 * @param data
 *      the data sent from the server
 */
function handleMap(data) {
    if (data.error !== undefined) {
        handleErrorCode(data.error);
        return;
    }
    
    exploredAll = false;
    updateMap(data);
}

/**
 * Handles action responses from the server.
 *
 * @param data
 *      the data sent from the server
 */
function handleActionResponse(data) {
    if (!data) return;
    
    handleErrorCode(data);
}

// ================================================================================================
// ======================================== ERROR HANDLERS ========================================
// ================================================================================================

/**
 * Handles global request errors.
 *
 * @param jqxhr
 *      the jqXHR object
 * @param status
 *      the status, either "timeout", "error", "abort", "parsererror" or null
 * @param error
 *      the textual portion of the HTTP status ("Not Found", "Internal Server Error")
 */
function handleGlobalRequestError(jqxhr, status, error) {
    if (status === "timeout") {
        showAlert("Something went wrong: request timed out.", Severity.Danger);
    } else if (status === "abort") {
        showAlert("Something went wrong: request was aborted.", Severity.Danger);
    } else if (status === "parsererror") {
        showAlert("Something went wrong: data from server is corrupt.", Severity.Danger);
    } else {
        showAlert("Something went wrong: " + error, Severity.Danger);
    }
}

/**
 * Handles status update request errors.
 *
 * @param jqxhr
 *      the jqXHR object
 * @param status
 *      the status, either "timeout", "error", "abort", "parsererror" or null
 * @param error
 *      the textual portion of the HTTP status ("Not Found", "Internal Server Error")
 */
function handleStatusUpdateRequestError(jqxhr, status, error) {
    if (status === "timeout") {
        //Allow 5 timeouts before switching back to the INDEX view.
        if (++statusTimeoutCount >= 5) {
            clearInterval(statusUpdateInterval);
            statusTimeoutCount = 0;

            switchTo(Views.INDEX);
            showAlert("Server is not responding!", Severity.Danger);
        }
    } else {
        handleGlobalRequestError(jqxhr, status, error);
    }
}

/**
 * Function to handle error codes from the server.
 *
 * @param code
 *      the error code
 * @param unauthorizedCallback
 *      [OPTIONAL] the callback to call when the error is the UNAUTHORIZED error
 */
function handleErrorCode(code, unauthorizedCallback) {
    var err = ErrorCodes[code];
    if (err !== undefined) {
        console.log("Error [" + err.name + "] (" + code + "): " + err.msg);
        showAlert(err.msg, err.severity);
        if (unauthorizedCallback !== undefined && err.name === "UNAUTHORIZED") {
            unauthorizedCallback();
        }
    } else {
        console.log("Error [?] (" + code + "): null");
    }
};