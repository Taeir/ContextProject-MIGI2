var gSocket = null;

/**
 * Called when a socket connection is opened.
 */
function onSocketOpen = function() {
    console.log("[WebSocket] Connected!");
};

/**
 * Called when the client receives a message from the server via a socket.
 *
 * @param evt
 *      the event containing the received message
 */
function onSocketMessage = function(evt) {
    if (evt.data != undefined && evt.data.lastIndexOf("{", 0) === 0) {
        handleServerMessage(JSON.parse(evt.data));
    } else {
        handleServerMessage(evt.data);
    }
};
/**
 * Called when a socket connection is closed.
 */
function onSocketClose = function() {
    console.log("[WebSocket] Disconnected!");
    gSocket = null;
};

/**
 * Called when an error occurs on the socket connection.
 */
function onSocketError = function() {
    console.log("[WebSocket] Error!");
};

/**
 * Connects with a new websocket to the server.
 */
function connectWebSocket() {
    useWebSocket(new WebSocket("ws:// " + window.location + "/ws/"));
};

/**
 * Method to use the given socket as the WebSocket connection to the server.
 *
 * @param socket
 *      the WebSocket to use
 */
function useWebSocket(socket) {
    gSocket = socket;
    
    gSocket.onopen = onSocketOpen;
    gSocket.onmessage = onSocketMessage;
    gSocket.onclose = onSocketClose;
    gSocket.onerror = onSocketError;
}

/**
 * Handles messages from the server
 */
function handleServerMessage(data) {

}

/**
 * Handles server status update messages.
 */
function handleStatusUpdateMessage(data) {
    //Check for errors
    if (data.error != undefined) {
        handleError(data.error);
        return;
    }
    
    //Check if we are authorized
    if (!checkAuthorized(data)) return;
    
    //If we are authorized, update the game with the received information
    updateGame(data);
    
    //Set the correct team
    if (data.team == undefined || data.team == "NONE") {
        //We don't have a team, so we switch to team selection
        gTeam = undefined;
        switchTo("WAITING");
        return;
    } else if (data.team == "DWARFS") {
        //We are in team DWARFS
        gTeam = "DWARFS";
    } else if (data.team == "ELVES") {
        //We are in team ELVES
        gTeam = "ELVES";
    }
    
    //Switch to the correct state
    switchTo(data.state);
};

/**
 * Function to handle error codes from the server.
 */
function handleError(code) {
    var err = ErrorCodes[code];
    if (err != undefined) {
        console.log("Error [" + err.name + "] (" + code + "): " + err.msg);
        showError(err.msg);
    } else {
        console.log("Error [?] (" + code + "): null");
    }
};


/**
 * Send a message to the server telling it to place a bomb at
 * the given location.
 */
function requestAction(argument) {
    var encoded = encodeAction(argument);
    if (encoded === -1) return;
    
    console.log("[DEBUG] Requesting action: " + argument + ".");
    $.post("/requestaction", {x: lastPressedX, y: lastPressedY, action: encoded},  function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
    }, "json");
    hideAllButtons(250);
}