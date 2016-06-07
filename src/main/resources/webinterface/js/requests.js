/**
 * Sends an authentication request to the server.
 * 
 * @param onSuccess
 *      function that will be called on a successful authentication
 */
function authenticate(onSuccess) {
    $.post("/login", function(data, status) {
        if (status == "success") {
            if (data == "0" || data == "") {
                //Successfully authenticated
                gAuth = true;
                
                onSuccess();
            } else {
                gAuth = false;
                handleError(data);
            }
        } else {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
        }
    });
}

/**
 * Checks if the given data implies that we are authorized.
 * If not, this method will attempt to authenticate and will return false.
 *
 * @param data
 *      the json data sent from the server
 */
function checkAuthorized(data) {
    if (data.auth == false) {
        //We are not authenticated, so we will try to do so.
        gAuth = false;
        authenticate();
        return false;
    }
    
    return true;
}

/**
 * Requests the server to set the team.
 *
 * @param team
 *      the team to set. Must be in [ELVES, DWARFS, NONE]
 */
function requestSetTeam(team) {
    hideAllButtons(0);

    //Check if the team is valid
    if (team != "ELVES" && team != "DWARFS" && team != "NONE") throw "Invalid team!";

    //Send a post request to the server to set the team
    $.post("/setteam", {team: team}, function(data, status) {
        if (status == "success") {
            var nTeam = Team[data];
            if (nTeam == undefined) {
                handleError(data);
                return;
            }
            
            gTeam = nTeam;
            updateTeam();
        } else {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
        }
    });
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
    
    //Set the correct team
    gTeam = Team[data.team];
    
    //Switch to the correct state
    if (data.state == "WAITING") {
        switchTo("WAITING");
    } else if (data.state == "RUNNING") {
        switchTo("RUNNING");
    } else if (data.state == "PAUSED") {
        switchTo("PAUSED");
    } else if (data.state == "ENDED") {
        switchTo("ENDED");
    }
    
    //If we are authorized, update the game with the received information
    updateGame(data);
};

/**
 * Function to handle error codes from the server.
 */
function handleError(code) {
    var err = ErrorCodes[code];
    if (err != undefined) {
        console.log("Error [" + err.name + "] (" + code + "): " + err.msg);
        showError(err.msg);
        //showAlert(err.msg, err.severity);
    } else {
        console.log("Error [?] (" + code + "): null");
    }
};