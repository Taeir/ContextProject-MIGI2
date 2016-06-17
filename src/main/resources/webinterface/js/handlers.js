var gCanJoin = true;

// ================================================================================================
// =========================================== HANDLERS ===========================================
// ================================================================================================

function handleIndexRefresh(result) {
    if (result === true || result === false) {
        if (gCanJoin !== result) {
            gCanJoin = result;
            updateJoinButtons();
        }
    } else if (result !== undefined) {
        if (gCanJoin !== true) {
            gCanJoin = true;
            updateJoinButtons();
        }
        
        switchTo(result.view);
    }
}


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
            handleErrorCode(data);
        }
    }
}

/**
 * Handles quit game responses from the server.
 *
 * @param data
 *      the data sent from the server. Can only be an ErrorCode
 */
function handleQuitGameResponse(data) {
    switchTo(Views.INDEX);
    if (!data) return;
    
    handleErrorCode(data);
}

/**
 * Handles set team responses from the server.
 
 * @param data
 *      the data sent from the server. Either the ordinal of the team to switch to, or an ErrorCode
 */
function handleSetTeam(data) {
    var nTeam = Teams[data];
    if (nTeam === undefined) {
        //Switch to the INDEX view when the client is not in the current game.
        handleErrorCode(data, function() { switchTo(Views.INDEX) });
        return;
    }
    
    //Update the team only if it changed
    updateTeam(nTeam);
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
    if (data.error !== undefined) {
        handleErrorCode(data.error, function() { switchTo(Views.INDEX) });
        return;
    }
    
    //Update the team
    updateTeam(Teams[data.t]);
    
    //Switch to the correct view
    switchTo(GameStates[data.s].view);
    
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
// ======================================== SOCKET HANDLERS =======================================
// ================================================================================================

/**
 * Called when a socket connection is opened.
 */
function handleSocketOpen() { }

/**
 * Called when the client receives a message from the server via a socket.
 *
 * @param evt
 *      the event containing the received message
 */
function handleSocketMessage(evt) {
    if (evt.data === undefined) {
        //No data in message!
        return;
    }
    
    if (evt.data.length === 1) {
        var state = GameStates[evt.data];
        if (state !== undefined) {
            switchTo(state.view);
            return;
        }
    } else if (evt.data.length === 3) {
        if (ErrorCodes[evt.data] !== undefined) {
            handleErrorCode(evt.data);
            return;
        }
    }
    
    try {
        var data = JSON.parse(evt.data);
        
        if (data.type === "map") {
            handleMap(data);
        } else {
            handleStatusUpdateMessage(JSON.parse(evt.data));
        }
    } catch (ex) {
        handleErrorCode(500);
    }
};
/**
 * Called when a socket connection is closed.
 */
function handleSocketClose() {
    gSocket = null;
    
    setTimeout(function() { showAlert("Server closed the connection.", Severity.Success); }, 1000);
    switchTo(Views.INDEX);
};

/**
 * Called when an error occurs on the socket connection.
 */
function handleSocketError(evt) {
    showAlert("Something went wrong", Severity.Danger, 2000);
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
 * Handles index refresh errors.
 *
 * @param jqxhr
 *      the jqXHR object
 * @param status
 *      the status, either "timeout", "error", "abort", "parsererror" or null
 * @param error
 *      the textual portion of the HTTP status ("Not Found", "Internal Server Error")
 */
function handleIndexRefreshError(jqxhr, status, error) {
    showAlert("Server is not responding. Trying again in 5 seconds...", Severity.Warning, Constants.Intervals.INDEX_REFRESH - 300);
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
        //Allow 3 timeouts before switching back to the INDEX view.
        if (++statusTimeoutCount >= 3) {
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
        showAlert(err.msg, err.severity);
        if (unauthorizedCallback !== undefined && err.name === "UNAUTHORIZED") {
            unauthorizedCallback();
        }
    } else {
        showAlert("An unknown error occurred!", Severity.Danger);
    }
}