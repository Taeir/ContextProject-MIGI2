var gAuth = false;
var gTeam;
var gView;
var gMap = null;
var gExplored = null;
var gEntities = null;
var lastPressedX;
var lastPressedY;
var exploredAll = false;

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
                showAlert("You cannot join this game because it has already started.", "Warning");
            } else if (data == "FULL") {
                //Game is full
                gAuth = false;
                showAlert("You cannot join this game because it is full.", "Warning");
            } else {
                //Unknown server response
                gAuth = false;
                showAlert("Unknown server response: " + data, "Danger");
            }
        } else {
            //HTTP Error
            showAlert("Something went wrong: [" + status + "] " + data, "Danger");
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
            showAlert("Something went wrong: [" + status + "] " + data, "Danger");
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
        //Set the correct team
        if (data.team == "NONE") {
            gTeam = undefined;
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
        } else if (data.state == "PAUSED") {
            switchTo("PAUSED");
        } else if (data.state == "ENDED") {
            switchTo("ENDED");
        }
        
        //If we are authorized, update the game with the recieved information
        updateGame(data);
    }, "json");
}

/**
 * Displays the given error message to the user.
 *
 * @param msg
 *      the message to display
 * @param severity
 *      the serverity of the error
 */
function showAlert(msg, severity) {
    var selected;
    switch (severity) {
        case "Danger":
            selected = "dangerBox";
            break;
        case "Warning":
            selected = "warningBox";
            break;
        case "Success":
            selected = "successBox";
            break;
        default:
            selected = "dangerBox";
            break;
    }
    
    document.getElementById(selected).innerHTML = msg;
    $("#" + selected).fadeIn();
    window.setTimeout(function () {
       $("#" + selected).fadeOut(300) 
    }, 3000);
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
    exploredAll = false;
    
    console.log("[DEBUG] Switching to " + view);
    gView = view;
    switch (view) {
        case "WAITING":
            requestMap();
            $(document.getElementById("selectTeam")).show();
            $(document.getElementById("playing")).show();
            $(document.getElementById("ended")).css("visibility", "hidden");
            $(document.getElementById("paused")).css("visibility", "hidden");
            break;
        case "RUNNING":
            requestMap();
            $(document.getElementById("selectTeam")).hide();
            $(document.getElementById("playing")).show();
            $(document.getElementById("ended")).css("visibility", "hidden");
            $(document.getElementById("paused")).css("visibility", "hidden");
            break;
        case "PAUSED":
            $(document.getElementById("selectTeam")).hide();
            $(document.getElementById("playing")).hide();
            $(document.getElementById("ended")).css("visibility", "hidden");
            $(document.getElementById("paused")).css("visibility", "visible");
            break;
        case "ENDED":
            $(document.getElementById("selectTeam")).hide();
            $(document.getElementById("playing")).hide();
            $(document.getElementById("ended")).css("visibility", "visible");
            $(document.getElementById("paused")).css("visibility", "hidden");
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
    hideAllButtons();
    //Check if the team is valid
    if (team != "ELVES" && team != "DWARFS" && team != "NONE") throw "Invalid team!";
    
    //Send a post request to the server to set the team
    $.post("/setteam", {team: team}, function(data, status) {
        if (status == "success") {
            if (data != team) {
                showAlert("Game has already started!", "Warning");
            }
            
            if (data == "DWARFS") {
                //Team was set to dwarfs
                gTeam = "DWARFS";
                updateTeam();
                exploreAll();
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
                showAlert("Invalid team!", "Danger");
            } else if (data == "UNAUTHORIZED") {
                //Not in the current game, and game is running
                showAlert("You are not in the game, and the game has already started!", "Warning");
            } else {
                //Unknown server response
                showAlert("Unknown server response: " + data, "Danger");
            }
        } else {
            //HTTP Error
            showAlert("Something went wrong: [" + status + "] " + data, "Danger");
        }
    });
}

/**
 * Updates the team view.
 */
function updateTeam() {
    var elvesButton = $(document.getElementById("elvesButton"));
    var dwarfsButton = $(document.getElementById("dwarfsButton"));
    var noneButton = $(document.getElementById("noneButton"));
    
    elvesButton.removeClass("btn-success");
    dwarfsButton.removeClass("btn-success");
    noneButton.removeClass("btn-success");
    
    elvesButton.addClass("btn-default");
    dwarfsButton.addClass("btn-default");
    noneButton.addClass("btn-default");
    
    if (gTeam == undefined) {
        noneButton.removeClass("btn-default");
        noneButton.addClass("btn-success");
    } else if (gTeam === "DWARFS") {
        dwarfsButton.removeClass("btn-default");
        dwarfsButton.addClass("btn-success");
    } else {
        elvesButton.removeClass("btn-default");
        elvesButton.addClass("btn-success");
    }
}

/**
 * Updates the game view.
 *
 * @param data
 *      the status data sent from the server
 */
function updateGame(data) {
    if (data.state === "RUNNING" || data.state === "WAITING") {
        updateEntities(data.entities);
        if (data.team === "DWARFS" && exploredAll === false) {
            exploreAll();
        } else {
            updateExplored(data.explored);
        }
    } else if (data.state === "ENDED") {
        displayWinner(data);
    }
}

function displayWinner(data) {
    var winHeader = document.getElementById("endedWinners");
    var winMessage = document.getElementById("endedMessage");
    var winner = (data.winner ? "ELVES" : "DWARFS");
    winHeader.innerHTML = (data.winner? "Elves won!" : "Dwarfs won!");
    
    if (winner === data.team) {
        winMessage.innerHTML = "Congratulations!";
    } else {
        winMessage.innerHTML = "Better luck next time!";
    }
}

/**
 * Instantly explores all tiles.
 */
function exploreAll() {
    for (x = 0; x < gMap.width; x++) {
        for (y = 0; y < gMap.height; y++){
            $(document.getElementById("y" + y + "x" + x)).addClass("explored");
        }
    }
    exploredAll = true;
}

/**
 * Requests the map from the server.
 */
function requestMap() {
    console.log("[DEBUG] GETTING MAP");
    
    $.post("/map", function(data, status) {
        if (status != "success") {
            //HTTP Error
            showAlert("Something went wrong: [" + status + "] " + data, "Danger");
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
        //Update the map
        updateMap(data);
    }, "json");
    exploredAll = false;
}

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
            showAlert("Something went wrong: [" + status + "] " + data, "Danger");
            return;
        }

        //Check if we are authorized
        if (!checkAuthorized(data)) return;
        
    }, "json");
    hideAllButtons();
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

/**
 * Shows the button menu depending on what team you are in.
 */
function showButtons() {
    editLegend();
    if (gTeam === "DWARFS") {
        $("#sidebar-wrapper-dwarfs").css("visibility", "visible");
        $("#wrapper").toggleClass("toggled", true);
    } else if (gTeam === "ELVES") {
        $("#sidebar-wrapper-elves").css("visibility", "visible");
        $("#wrapper").toggleClass("toggled", true);
    } else {
        console.log("[DEBUG] No team selected, buttons not shown.");
    }
    
}

/**
 * Changes the legend to represent the currently selected tile.
 */
function editLegend() {
    var selected;
    var classes = document.getElementById("y" + lastPressedY + "x" + lastPressedX).className.split(" ");
    
    if (gTeam === "DWARFS") {
        selected = document.getElementById("clickedDwarfs");
    } else if (gTeam === "ELVES") {
        selected = document.getElementById("clickedElves");
    } else {
        return;
    }

    if ((classes.indexOf("explored") === -1) && (classes.length < 2)) {
        selected.innerHTML = "Selected: Unexplored";
    } else {
        var newInner = classes[classes.length - 1];
        if (newInner === "explored") newInner = classes[classes.length - 2];
        if (newInner == null) newInner = "Void";
        selected.innerHTML = "Selected: " + newInner.replace("_", " ");
    }
}

/**
 * Hide both button divs.
 */
function hideAllButtons() {
    $("#wrapper").toggleClass("toggled", false);
    $("#sidebar-wrapper-dwarfs").css("visibility", "hidden");
    $("#sidebar-wrapper-elves").css("visibility", "hidden");
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
    exploredAll = false;
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
        for (i = 0; i < gEntities.length; i++) {
            $(document.getElementById("y" + gEntities[i].y + "x" + gEntities[i].x))
                .removeClass(getClassForEntityType(gEntities[i].type));
        }
    }
    
    for (i = 0; i < data.length; i++) {
        $(document.getElementById("y" + data[i].y + "x" + data[i].x))
            .addClass(getClassForEntityType(data[i].type));
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
            return "";
        case 1:
            return "Bomb";
        case 2:
            return "Door";
        case 3:
            return "Key";
        case 4:
            return "Player";
        case 5:
            return "Player_Trigger";
        case 6:
            return "Pitfall";
        case 7:
            return "Landmine";
        case 8:
            return "Carrot";
        case 9:
            return "Killer_Bunny";
        case 10:
            return "Platform";
        case 11:
            return "Invisible_Wall";
        case 12:
            return "Damaged_Wall";
        case 14:
        	return "Gate";
        default:
            showAlert("Invalid tile type: " + entityType, "Danger");
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
            return "Floor";
        case 2:
            return "Wall";
        case 3:
            return "Corridor";
        default:
            showAlert("Invalid tile type: " + tileType, "Danger");
            throw "Invalid tile type: " + tileType;
    }
}

/**
 * Encode an action.
 *
 * @param action
 *      the action to encode
 */
function encodeAction(action) {
    switch (action) {
        case "placebomb":
            return 0;
        case "placepitfall":
            return 1;
        case "placemine":
            return 2;
        case "spawnenemy":
            return 3;
        case "dropbait":
            return 4;
        case "placetile":
            return 5;
        case "opengate":
        	return 6;
        default:
            return -1;
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
        disableFullScreen();
    }
    hideAllButtons();
}

/**
 * Disables fullscreen.
 */
function disableFullScreen() {
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
