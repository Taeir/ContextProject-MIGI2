var gTeam;
var gEntities = null;
var gExploredAll = false;
var gWidth = 0;

// ================================================================================================
// ====================================== UPDATE INFORMATION ======================================
// ================================================================================================

/**
 * Updates the game view.
 *
 * @param data
 *      the status data sent from the server
 */
function updateGame(data) {
    var state = GameStates[data.s];
    switch (state.name) {
        case "RUNNING":
        case "WAITING":
            updateEntities(data.e);
            updateTimer(data.r);
            
            if (Teams[data.t] === "DWARFS") {
                exploreAll();
            } else if (data.x !== undefined) {
                updateExplored(data.x);
            }
            break;
        case "ENDED":
            displayWinner(data);
            break;
    }
}

/**
 * Updates the team by updating the team buttons and the exploration.
 *
 * @param nTeam
 *      the new team. Must be a team from Teams [ENUM]
 */
function updateTeam(nTeam) {
    if (gTeam === nTeam) return;
    
    gTeam = nTeam;
    
    //Clear exploration for non dwarfs.
    if (gExploredAll && nTeam !== "DWARFS") clearExploration();
    
    updateTeamButtons();
    hideGameButtons();
}

/**
 * Updates the timer to the recieved time.
 *
 * @param time
 *      the current time
 */
function updateTimer(time) {
    if (time > 3600) {
        $(".timer").html("&infin;")
    } else {
        $(".timer").html(time);
    }
}

// ================================================================================================
// ========================================= UPDATE TILES =========================================
// ================================================================================================

/**
 * Updates the map with the data.
 *
 * @param data
 *      the map data sent from the server
 */
function updateMap(data) {
    //Resize the map table
    resizeMapTable(data.width, data.height);
    
    //Update the classes
    for (var y = 0; y < data.height; y++) {
        for (var x = 0; x < data.width; x++) {
            var cell = document.getElementById("y" + y + "x" + x);
            var type = TileTypes[data.tiles[x][y]];
            
            cell.className = (type === undefined ? "" : type);
        }
    }
    
    //Exploration is no longer set
    gExploredAll = false;
}

/**
 * Resizes the map table.
 *
 * @param nWidth
 *      the new width of the table
 * @param nHeight
 *      the new height of the table
 */
function resizeMapTable(nWidth, nHeight) {
    var tableNode = document.getElementById("map");
    
    var rows = tableNode.rows;
    var oHeight = rows.length;
    var oWidth = oHeight > 0 ? rows[0].cells.length : 0;
    
    //Check if table width matches
    if (oWidth !== nWidth) {
        if (oWidth > nWidth) {
            //Shrink all existing rows
            for (var y = 0; y < oHeight; y++) {
                for (var i = nWidth; i < oWidth; i++) {
                    rows[y].deleteCell(-1);
                }
            }
        } else if (oWidth < nWidth) {
            //Add new cells to existing rows.
            for (var y = 0; y < oHeight; y++) {
                for (var x = oWidth; x < nWidth; x++) {
                    var cell = rows[y].insertCell(-1);
                    cell.id = "y" + y + "x" + x;
                    cell.onclick = createClickableFunc(x, y);
                }
            }
        }
    }
    
    //Check if table height matches
    if (oHeight !== nHeight) {
        if (oHeight > nHeight) {
            //Remove unneeded rows
            for (var y = nHeight; y < oHeight; y++) {
                tableNode.deleteRow(-1);
            }
        } else if (oHeight < nHeight) {
            //Add extra rows
            for (var y = oHeight; y < nHeight; y++) {
                var row = tableNode.insertRow(-1);
                for (var x = 0; x < nWidth; x++) {
                    var cell = row.insertCell(-1);
                    cell.id = "y" + y + "x" + x;
                    cell.onclick = createClickableFunc(x, y);
                }
            }
        }
    }
    
    gWidth = nWidth;
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
    return function() {
        lastPressedX = x;
        lastPressedY = y;
        showGameButtons();
    };
}

// ================================================================================================
// ====================================== UPDATE EXPLORATION ======================================
// ================================================================================================

/**
 * Updates the map with explored data.
 *
 * @param data
 *      the explored data sent from the server
 */
function updateExplored(data) {
    if (supportsClassList) {
        for (var x = 0; x < gWidth; x++) {
            var row = data[x];
            if (row === undefined) continue;
            
            for (var i = 0; i < row.length; i++) {
                var element = document.getElementById("y" + row[i] + "x" + x);
                if (element == null) continue;
                
                element.classList.add("explored");
            }
        }
    } else {
        for (var x = 0; x < gWidth; x++) {
            var row = data[x];
            if (row == undefined) continue;
            
            for (var i = 0; i < row.length; i++) {
                $(document.getElementById("y" + row[i] + "x" + x)).addClass("explored");
            }
        }
    }
}

/**
 * Instantly explores all tiles.
 */
function exploreAll() {
    if (gExploredAll === true) return;
    
    var rows = document.getElementById("map").rows;
    var cells;
    
    if (supportsClassList) {
        for (var y = 0; y < rows.length; y++) {
            cells = rows[y].cells;
            for (var x = 0; x < cells.length; x++) {
                cells[x].classList.add("explored");
            }
        }
    } else {
        for (var y = 0; y < rows.length; y++) {
            cells = rows[y].cells;
            for (var x = 0; x < cells.length; x++) {
                $(cells[x]).addClass("explored");
            }
        }
    }

    gExploredAll = true;
}
/**
 * Clears all exploration.
 */
function clearExploration() {
    var rows = document.getElementById("map").rows;
    var cells;
    
    if (supportsClassList) {
        for (var y = 0; y < rows.length; y++) {
            cells = rows[y].cells;
            for (var x = 0; x < cells.length; x++) {
                cells[x].classList.remove("explored");
            }
        }
    } else {
        for (var y = 0; y < rows.length; y++) {
            cells = rows[y].cells;
            for (var x = 0; x < cells.length; x++) {
                $(cells[x]).removeClass("explored");
            }
        }
    }
    
    gExploredAll = false;
}

// ================================================================================================
// ======================================== UPDATE ENTITIES =======================================
// ================================================================================================

/**
 * Updates the map with all entities.
 *
 * @param data
 *      the entity data sent from the server
 */
function updateEntities(data) {
    if (supportsClassList) {
        if (gEntities !== null) {
            //Remove the old data
            for (var i = 0, len = gEntities.length; i < len; i++) {
                var type = EntityTypes[gEntities[i].t];
                if (type === undefined) continue;
                
                var element = document.getElementById("y" + gEntities[i].y + "x" + gEntities[i].x);
                if (element == null) continue;
                
                element.classList.remove(type);
                if (type === "Bomb") {
                    element.innerHTML = "";
                }
            }
        }
        
        for (var i = 0, len = data.length; i < len; i++) {
            var type = EntityTypes[data[i].t];
            if (type === undefined) continue;
            
            var element = document.getElementById("y" + data[i].y + "x" + data[i].x);
            if (element == null) continue;
            
            element.classList.add(type);
            if (type === "Bomb") {
                element.innerHTML = data[i].d;
            }
        }
    } else {
        if (gEntities !== null) {
            //Remove the old data
            for (var i = 0, len = gEntities.length; i < len; i++) {
                var type = EntityTypes[gEntities[i].t];
                if (type === undefined) continue;
                
                $(document.getElementById("y" + gEntities[i].y + "x" + gEntities[i].x)).removeClass(type);
                if (type === "Bomb") {
                    element.innerHTML = "";
                }
            }
        }
        
        for (var i = 0, len = data.length; i < len; i++) {
            var type = EntityTypes[data[i].t];
            if (type === undefined) continue;
            
            $(document.getElementById("y" + data[i].y + "x" + data[i].x)).addClass(type);
            if (type === "Bomb") {
                element.innerHTML = data[i].d;
            }
        }
    }
    
    gEntities = data;
}