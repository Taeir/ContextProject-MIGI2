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
        indexRefresh();
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
    if (supportsWebSockets === false) {
        if (statusUpdateInterval === undefined) {
            requestStatusUpdate();
            statusUpdateInterval = setInterval(requestStatusUpdate, Constants.Intervals.STATUS_UPDATE);
        }
    } else if (supportsWebSockets === true && !isSocketOpen()) {
        createWebSocket();
    }
}

/**
 * Stop the status update requests.
 */
function stopStatusUpdates() {
    if (statusUpdateInterval !== undefined) {
        clearInterval(statusUpdateInterval);
        statusUpdateInterval = undefined;
    } else if (isSocketOpen()) {
        closeWebSocket();
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
            handleIndexRefreshError(jqxhr, status, error);
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
 * Requests the server to set the team, either via websocket or normally.
 *
 * @param team
 *      the team to set
 */
function requestSetTeam(team) {
    requestNormal_setTeam(team);
    
    //TODO WebSocket
}

/**
 * Requests the server to set the team normally.
 *
 * On success, this function calls handleSetTeam.
 * On failure, this function calls handleGlobalRequestError.
 *
 * @param team
 *      the team to set
 */
function requestNormal_setTeam(team) {
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
 * Requests the server to set the team via websocket. 
 *
 * @param team
 *      the team to set
 */
function requestSocket_setTeam(team) {
    gSocket.send("setteam " + team);
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
 * Requests the map from the server, either via websocket or normally.
 */
function requestMap() {
    if (isSocketOpen()) {
        requestSocket_map();
    } else {
        requestNormal_map();
    }
}

/**
 * Requests the map from the server via websocket.
 */
function requestSocket_map() {
    gSocket.send("map");
}

/**
 * Requests the map from the server.
 *
 * On success, this function calls handleMap.
 * On failure, this function calls handleGlobalRequestError.
 */
function requestNormal_map() {
    $.ajax({
        type: "POST",
        url: "/map",
        dataType: "json",
        success: handleMap,
        error: handleGlobalRequestError
    });
}

/**
 * Sends an action request to the server, either via websocket or normally.
 *
 * Afterwards, hides the buttons.
 *
 * @param action
 *      the unencoded action to request
 */
function requestAction(action) {
    var encoded = Actions[action];
    if (encoded === undefined) return;
    
    if (isSocketOpen()) {
        requestSocket_action(encoded);
    } else {
        requestNormal_action(encoded);
    }
    
    //[GUI] Hide the sidebar buttons
    hideGameButtons();
}

/**
 * Function to request an action via the websocket.
 *
 * @param action
 *      the encoded action to request
 */
function requestSocket_action(action) {
    gSocket.send(action + " " + lastPressedX + " " + lastPressedY);
}

/**
 * Sends an action request to the server.
 *
 * On success, this function calls handleActionResponse.
 * On failure, this function calls handleGlobalRequestError.
 *
 * @param action
 *      the encoded action to request
 */
function requestNormal_action(action) {
    $.ajax({
        type: "POST",
        url: "/requestaction",
        data: {x: lastPressedX, y: lastPressedY, action: action},
        success: handleActionResponse,
        error: handleGlobalRequestError
    });
}

/**
 * Sends a quit game request, either via websocket or normally.
 */
function requestQuitGame() {
    if (isSocketOpen()) {
        requestSocket_quitGame();
    } else {
        requestNormal_quitGame();
    }
}

/**
 * Function to request to quit the game via websocket.
 */
function requestSocket_quitGame() {
    gSocket.send("quit");
}

/**
 * Sends a quit game request to the server.
 *
 * On success, this function calls handleQuitGameResponse.
 * On failure, this function calls handleGlobalRequestError.
 */
function requestNormal_quitGame(action) {
    $.ajax({
        type: "POST",
        url: "/quit",
        success: handleQuitGameResponse,
        error: handleGlobalRequestError
    });
}