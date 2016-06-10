//The different severity levels
Severity = Object.freeze({
    Danger: "dangerBox",
    Warning: "warningBox",
    Success: "successBox"
});

//The different views the client can be in.
Views = Object.freeze({
    INDEX: 0,
    INSTRUCTION: 1,
    TEAM: 2,
    GAME: 3,
    PAUSED: 4,
    ENDED: 5,
    TUTORIAL: 6
});

//The different states the game can be in.
GameStates = Object.freeze({
    0: {name: "TUTORIAL", view: Views.TUTORIAL},
    1: {name: "WAITING", view: Views.TEAM},
    2: {name: "RUNNING", view: Views.GAME},
    3: {name: "PAUSED",  view: Views.PAUSED},
    4: {name: "ENDED",   view: Views.ENDED}
});

Teams = Object.freeze({
    0: "DWARFS",
    1: "ELVES",
    2: "NONE"
});

//The different actions that we can perform.
Actions = Object.freeze({
    BOMB: 0,
    PIT: 1,
    MINE: 2,
    ENEMY: 3,
    BAIT: 4,
    TILE: 5,
    GATE: 6,
    CRATE: 7
});

//The different entity types --> their CSS class
EntityTypes = Object.freeze({
    1:  "Bomb",
    2:  "Landmine",
    3:  "Pitfall",
    
    4:  "Door",
    5:  "Key",
    6:  "Gate",
    7:  "Crate",
    
    8:  "Carrot",
    9:  "Killer_Bunny",
    
    10: "Platform",
    11: "Invisible_Wall",
    12: "Damaged_Wall",
    
    13:  "Player",
    14:  "Player_Trigger"
});

//The different tile types --> their CSS class
TileTypes = Object.freeze({
    1: "Floor",
    2: "Wall",
    3: "Corridor"
});

//The different error codes that can be sent by the server
ErrorCodes = Object.freeze({
    100: {name: "AUTHENTICATE_FAIL_IN_PROGRESS",
          msg: "You cannot join this game because it has already started.",
          severity: Severity.Warning},
    101: {name: "AUTHENTICATE_FAIL_FULL",
          msg: "You cannot join because the game is full.",
          severity: Severity.Warning},

    110: {name: "SETTEAM_STARTED",
          msg: "You cannot switch teams when the game has started!",
          severity: Severity.Warning},
    111: {name: "SETTEAM_INVALID_TEAM",
          msg: "Stop cheating!",
          severity: Severity.Danger},
    112: {name: "SETTEAM_TEAM_FULL",
          msg: "This team is full!",
          severity: Severity.Warning},

    200: {name: "ACTION_ILLEGAL_LOCATION",
          msg: "You cannot perform this action at this location.",
          severity: Severity.Warning},
    201: {name: "ACTION_COOLDOWN",
          msg: "This action is on cooldown",
          severity: Severity.Warning},
    202: {name: "ACTION_ILLEGAL",
          msg: "Stop cheating! This action is not possible in your current team.",
          severity: Severity.Danger},
    203: {name: "ACTION_RADIUS",
          msg: "You cannot do this so close to the player.",
          severity: Severity.Warning},

    300: {name: "UNAUTHORIZED",
          msg: "You are not in the current game!",
          severity: Severity.Danger},

    500: {name: "SERVER_ERROR",
          msg: "Something went wrong",
          severity: Severity.Danger}
});

//An object defining different constant values.
Constants = Object.freeze({
    Intervals: {
        //Interval of Index refresh Requests, in ms.
        INDEX_REFRESH: 5000,
        
        //Interval of Status update Requests, in ms.
        STATUS_UPDATE: 800,
        
        //Default time that alerts are shown for, in ms.
        ALERT: 4000
    }

});