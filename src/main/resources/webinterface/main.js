var gAuth = false;
var gTeam;
var gView;
var gPlayer = {
    x: 0,
    y: 0,
    z: 0,
    hp: 0,
    bombs: 0,
    keys: []
};

/**
 * Sends an authentication request to the server.
 */
function authenticate() {
    $.post("/login", function(data, status) {
        if (status == "success") {
            if (data == "AUTHENTICATED" || data == "UPDATED") {
                //Successfully authenticated
                gAuth = true;
                
                //Switch to team selection
                switchTo("WAITING");
            } else if (data == "IN_PROGRESS") {
                //Game is already in progress
                gAuth = false;
                showError("You cannot join this game because it has already started.");
            } else if (data == "FULL") {
                //Game is full
                gAuth = false;
                showError("You cannot join this game because it is full.");
            } else {
                //Unknown server response
                gAuth = false;
                showError("Unknown server response: " + data);
            }
        } else {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
        }
    });
}

/**
 * Requests the current status from the server.
 */
function requestStatus() {
    $.post("/status", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Request was successful
        if (data.auth == false) {
            //We are not authenticated, so we will try to do so.
            gAuth = false;
            authenticate();
            return;
        }
        
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
        if (data.state == "WAITING") {
            switchTo("WAITING");
        } else if (data.state == "RUNNING") {
            switchTo("RUNNING");
            updateGame(data);
        } else if (data.state == "PAUSED") {
            switchTo("PAUSED");
        } else if (data.state == "ENDED") {
            switchTo("ENDED");
        }
    }, "json");
}

/**
 * Displays the given error message to the user.
 *
 * @param msg
 *      the message to display
 */
function showError(msg) {
    //TODO Change to better error dialog
    alert(msg);
}

/**
 * Switches to the given view.
 *
 * @param view
 *      the view to switch to. Must be in [WAITING, RUNNING, PAUSED, ENDED]
 */
function switchTo(view) {
    //We don't need to switch to our current state.
    if (gView == view) return;
    
    console.log("[DEBUG] Switching to " + view);
    gView = view;
    switch (view) {
        case "WAITING":
            //TODO
            break;
        case "RUNNING":
            //TODO
            break;
        case "PAUSED":
            //TODO
            break;
        case "ENDED":
            //TODO
            break;
    }
}

/**
 * Requests the server to set the team.
 *
 * @param team
 *      the team to set. Must be in [ELVES, DWARFS, NONE]
 */
function requestSetTeam(team) {
    //Check if the team is valid
    if (team != "ELVES" && team != "DWARFS" && team != "NONE") throw "Invalid team!";
    
    //Send a post request to the server to set the team
    $.post("/setteam", {team: team}, function(data, status) {
        if (status == "success") {
            if (data != team) {
                showError("Game has already started!");
            }
            
            if (data == "DWARFS") {
                //Team was set to dwarfs
                gTeam = "DWARFS";
                updateTeam();
                //TODO check start game
            } else if (data == "ELVES") {
                //Team was set to elves
                gTeam = "ELVES";
                updateTeam();
                //TODO check start game
            } else if (data == "NONE") {
                //Team was unset
                gTeam = undefined;
                updateTeam();
                //TODO check start game
            } else if (data == "INVALID") {
                //Invalid team was sent
                showError("Invalid team!");
            } else {
                //Unknown server response
                showError("Unknown server response: " + data);
            }
        } else {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
        }
    });
}

/**
 * Updates the team view.
 */
function updateTeam() {
    if (gTeam == undefined) {
        $(".team").html("No team");
    } else {
        $(".team").html(gTeam);
    }
}

/**
 * Updates the game view.
 *
 * @param data
 *      the status data sent from the server
 */
function updateGame(data) {
    if (data.player != undefined) {
        //TODO
    }
}