var gAuth = false;
var gTeam;
var gView;
var gMap = null;
var gExplored = null;
var gEntities = null;
var lastPressedX = 0;
var lastPressedY = 0;

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
 * Requests the current status from the server.
 */
function requestStatus() {
    $.post("/status", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
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
    requestMap();
    switch (view) {
        case "WAITING":
            //TODO
            break;
        case "RUNNING":
            $(document.getElementById("selectTeam")).hide();
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
    hideAllButtons(0);
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
            } else if (data == "UNAUTHORIZED") {
                //Not in the current game, and game is running
                showError("You are not in the game, and the game has already started!");
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

/**
 * Requests the map from the server.
 */
function requestMap() {
    console.log("[DEBUG] GETTING MAP");
    
    $.post("/map", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
        //Update the map
        updateMap(data);
    }, "json");
    setInterval(requestEntities, 1000);
	setInterval(requestExplored, 1000);
}

/**
 * Requests locations of explored tiles from the server.
 */
function requestExplored() {
    console.log("[DEBUG] GETTING EXPLORED");
    
    $.post("/explored", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
        //Update the map
        updateExplored(data);
    }, "json");
}

/**
 * Request entities from the server.
 */
function requestEntities() {
    console.log("[DEBUG] GETTING ENTITIES");
    $.post("/entities", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showError("Something went wrong: [" + status + "] " + data);
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
        //Update the map with the entities
        updateEntities(data);
    }, "json");
}

/**
 * Send a message to the server telling it to place a bomb at
 * the given location.
 */
function requestAction(argument) {
    console.log("[DEBUG] Requesting action: " + argument + ".");
    $.post("/requestaction", {x: lastPressedX, y: lastPressedY, action: argument},  function(data, status) {
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

/**
 * Method that allows the generation of functions in a loop.
 *
 * @param x
 *      the x coordinate of the cell
 * @param y
 *      the y coordinate of the cell
 */
function createClickableFunc(x, y) {
    return function() 
        {
            console.log("Cell y" + y + "x" + x + " clicked.");
            lastPressedX = x;
            lastPressedY = y;
            showButtons();
        };
}

function showButtons() {
    if (gTeam === "DWARFS") {
        $(document.getElementById('dwarvesButtons')).show(250);
    } else if (gTeam === "ELVES") {
        $(document.getElementById('elvesButtons')).show(250);
    } else {
        console.log("[DEBUG] No team selected, buttons not shown.");
    }
}

function hideAllButtons(time) {
    $(document.getElementById('dwarvesButtons')).hide(time);
    $(document.getElementById('elvesButtons')).hide(time);
}

/**
 * Updates the map with the data.
 *
 * @param data
 *      the map data sent from the server
 */
function updateMap(data) {
    if (gMap == null || gMap.width != data.width || gMap.height != data.height) {
        var jqTableNode = $("#map");
        
        //Clear the map
        jqTableNode.empty();
        
        //Extract from jQuery into javascript for better performance
        var tableNode = jqTableNode[0];
        for (y = 0; y < data.height; y++) {
            var row = document.createElement("tr");
            
            //Set the row's id to the y coordinate
            row.id = "y" + y;
            
            for (x = 0; x < data.width; x++) {
                //Create a new cell
                var cell = document.createElement("td");
                
                //Set the cell's id to the x coordinate
                cell.id = "y" + y + "x" + x;

                cell.onclick = createClickableFunc(x, y);
                
                //Set the classname to the type of the tile
                cell.className = getClassForTileType(data.tiles[x][y]);
                
                //Add the cell to the row
                row.appendChild(cell);
            }
            
            //Add the row to the table
            tableNode.appendChild(row);
        }
        
        gMap = data;
    } else {
        //Map dimensions agree, so we can perform an update
        for (y = 0; y < data.height; y++) {
            for (x = 0; x < data.width; x++) {
                //get the cell
                var cell = document.getElementById("y" + y + "x" + x);
                
                //Update the className
                cell.className = getClassForTileType(data.tiles[x][y]);
            }
        }
        
        gMap = data;
    }
}

/**
 * Updates the map with explored data.
 *
 * @param data
 *      the explored data sent from the server
 */
function updateExplored(data) {
    for (x = 0; x < gMap.width; x++) {
        var row = data[x];
        if (row == undefined) continue;
        
        for (i = 0; i < row.length; i++) {
            $(document.getElementById("y" + row[i] + "x" + x)).addClass("explored");
        }
    }
    
    gExplored = data;
}

/**
 * Updates the map with all entities.
 *
 * @param data
 *      the entity data sent from the server
 */
function updateEntities(data) {
    if (gEntities != null) {
        for (i = 0; i < gEntities.entities.length; i++) {
            $(document.getElementById("y" + gEntities.entities[i].y + "x" + gEntities.entities[i].x))
                .removeClass(getClassForEntityType(gEntities.entities[i].type));
        }
    }
    
    for (i = 0; i < data.entities.length; i++) {
        $(document.getElementById("y" + data.entities[i].y + "x" + data.entities[i].x))
            .addClass(getClassForEntityType(data.entities[i].type));
    }
    
    gEntities = data;
}

/**
 * Converts the entity type id to the correct css class.
 *
 * @param entityType
 *      the entityType id sent from the server
 */
function getClassForEntityType(entityType) {
    switch (entityType) {
        case 0:
            return "unknown";
        case 1:
            return "bomb";
        case 2:
            return "door";
        case 3:
            return "key";
        case 4:
            return "vrplayer";
        case 5:
            return "playertrigger";
        case 6:
            return "pitfall";
        default:
            showError("Invalid tile type: " + entityType);
            throw "Invalid tile type: " + entityType;
    }
}

/**
 * Converts the tile type id to the correct css class.
 *
 * @param tileType
 *      the tileType id sent from the server
 */
function getClassForTileType(tileType) {
    switch (tileType) {
        case 0:
            return "";
        case 1:
            return "floor";
        case 2:
            return "wall";
        case 3:
            return "corridor";
        case 4:
            return "invisible_wall";
        default:
            showError("Invalid tile type: " + tileType);
            throw "Invalid tile type: " + tileType;
    }
}

/**
 * Function to toggle the webpage to fullscreen.
 * This method was copied from https://developer.mozilla.org/en-US/docs/Web/API/Fullscreen_API
 */
function toggleFullscreen() {
    if (!document.fullscreenElement && !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {
        if (document.documentElement.requestFullscreen) {
            document.documentElement.requestFullscreen();
        } else if (document.documentElement.msRequestFullscreen) {
            document.documentElement.msRequestFullscreen();
        } else if (document.documentElement.mozRequestFullScreen) {
            document.documentElement.mozRequestFullScreen();
        } else if (document.documentElement.webkitRequestFullscreen) {
            document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
        }
    } else {
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
}