var supportsWebSockets;

/**
 * Checks if websockets are supported.
 *
 * The given callback is called with either false, true or a socket, to indicate if the check failed or
 * passed. If the callback is called with a socket, then the server immediately responded with an "OK"
 * message, which indicates that the socket can be used as normal.
 *
 * The second argument of the callback will be set to the current GameState if the first argument is a
 * socket.
 *
 * @param callback
 *      function that will be called when the check result has been determined
 */
function doWebSocketCheck(callback) {
    if (!("WebSocket" in window)) {
        supportsWebSockets = false;
        callback(false);
        return;
    }
    
    try {
        var socket = new WebSocket("ws://" + window.location.host + "/ws/");
        
        socket.onclose = function(evt) {
            if (evt.reason === "Not Authorized") {
                supportsWebSockets = true;
                callback(true);
            }
        };
        
        socket.onerror = function(evt) {
            supportsWebSockets = false;
            callback(false);
        };
        
        socket.onmessage = function(evt) {
            supportsWebSockets = true;
            
            if (evt.data === undefined) {
                callback(true);
                return;
            }
            
            var state = GameStates[evt.data];
            
            //Server sent message that this socket can be used directly.
            //This means that the client dropped out of the game.
            if (state !== undefined) {
                callback(socket, state);
            } else {
                callback(true);
            }
        }
    } catch (ex) {
        supportsWebSockets = false;
        callback(false);
    }
};