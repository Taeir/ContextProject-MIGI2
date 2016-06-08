var gSocket = null;

// ================================================================================================
// ========================================== WEBSOCKETS ==========================================
// ================================================================================================

/**
 * Creates a new websocket connection to the server.
 */
function createWebSocket() {
    var old = gSocket;
    if (old !== null && old.readyState === WebSocket.OPEN) return;
    
    useWebSocket(new WebSocket("ws://" + window.location.host + "/ws/"));
}

/**
 * Closes the websocket connection.
 */
function closeWebSocket() {
    var socket = gSocket;
    if (socket === null) return;
    
    socket.close();
}

/**
 * Method to use the given socket as the WebSocket connection to the server.
 *
 * @param socket
 *      the WebSocket to use
 */
function useWebSocket(socket) {
    socket.onopen = handleSocketOpen;
    socket.onclose = handleSocketClose;
    socket.onerror = handleSocketError;
    
    gSocket = socket;
    
    socket.onmessage = handleSocketMessage;
}

/**
 * Checks if the websocket is usable.
 * 
 * @return
 *      true if the websocket is open, false otherwise
 */
function isSocketOpen() {
    var socket = gSocket;
    return socket !== null && socket.readyState === WebSocket.OPEN;
}
