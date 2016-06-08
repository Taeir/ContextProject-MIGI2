/**
 * Function that is called on load of the page.
 */
function onLoad() {
    //Check for websocket support
    doWebSocketCheck(function(result, state) {
        if (result === false) {
            showAlert("Our game runs slow in your browser. If possible, try using Google Chrome.", Severity.Success, 10000);
            
            //Client does not support websockets, so perform the indexRefresh check.
            indexRefresh(true);
        } else if (result === true) {
            //Client supports websockets, and is a new client.
            switchTo(Views.INDEX);
        } else {
            //Client dropped out of the game, switch to the correct view.
            useWebSocket(result);
            switchTo(state.view);
        }
    });
}

/**
 * Refreshes the INDEX and INSTRUCTION view Join game buttons, based on server response.
 *
 * @param init
 *      if true, this method will always switch the view.
 */
function indexRefresh(init) {
    if (init === true) {
        requestIndexRefresh(function(result) {
            if (result === true || result === false) {
                switchTo(Views.INDEX);
            }
            
            handleIndexRefresh(result);
        });
    } else if (gView !== Views.INDEX && gView !== Views.INSTRUCTION) {
        stopIndexRefresh();
    } else {
        requestIndexRefresh(handleIndexRefresh);
    }
}

function resetEverything() {
    gExploredAll = false;
    
    //requests.js
    stopIndexRefresh();
    stopStatusUpdates();
    statusTimeoutCount = 0;
    
    closeWebSocket();
    
    //Switch to the INDEX
    switchTo(Views.INDEX);
}
