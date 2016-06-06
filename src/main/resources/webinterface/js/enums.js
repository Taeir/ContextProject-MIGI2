//The different severity levels
Severity = Object.freeze({
    Danger: "dangerBox",
    Warning: "warningBox",
    Success: "successBox"
});

//The different states the game can be in.
GameStates = Object.freeze({
    0: "WAITING",
    1: "RUNNING",
    2: "PAUSED",
    3: "ENDED"
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
    BOX: 7
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
    112: {name: "SETTEAM_UNAUTHORIZED",
          msg: "You are not in the game, and the game has already started!",
          severity: Severity.Warning},
    113: {name: "SETTEAM_TEAM_FULL",
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

    300: {name: "UNAUTHORIZED",
          msg: "You are not in the current game!",
          severity: Severity.Danger},

    500: {name: "SERVER_ERROR",
          msg: "Something went wrong",
          severity: Severity.Danger}
});

