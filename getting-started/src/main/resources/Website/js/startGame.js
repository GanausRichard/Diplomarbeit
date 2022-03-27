// set global variables
let gameMode = "pve";
let difficultyLevel = 10;

// if gameMode gets set to pvp, then display inputName2 div
function getGameMode(value) {
    gameMode = value;
    if (gameMode === 'pvp') {
        document.getElementById('inputName2').style.display = 'block';
    }
    else if (gameMode === 'pve') {
        document.getElementById('inputName2').style.display = 'none';
    }
}

// if a button gets clicked => this function gets executed
// it sets the difficulty of the game (in pve mode)
function setDifficulty(value) {
    difficultyLevel = value;
}

// hideShowDivs is a function for design purposes to display specific elements
function hideShowDivs(hiddenElement, shownElement) {
    document.getElementById(hiddenElement).style.display = 'none';
    document.getElementById(shownElement).style.display = 'block';
}

// the startGame function gets executed on window onload
// if all game settings are entered => fetch the data
// (error message gets displayed if response not ok)
window.onload = function startGame() {
    // initialise a new form
    const form = document.getElementById('gameSettings');
    // hide input box for name2
    document.getElementById('inputName2').style.display = 'none';
    // hide customText element (text box for error messages)
    document.getElementById('customText').style.display = 'none';

    // listen for submit events
    form.addEventListener('submit', function (event) {
        // stop form submission
        event.preventDefault();

        // read input data from form (game settings)
        const name1 = form.elements['name1'].value;
        const name2 = form.elements['name2'].value;
        const endNode = difficultyLevel;
        const data = {name1, name2, gameMode, endNode};

        // create a header
        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');

        // request resource .../connectFour/startGame
        fetch('http://' + window.location.host + '/connectFour/startGame', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: myHeaders})
            .then(response => {
                // if response is ok => console log the response object with the message
                // "game has started" and redirect to .../staticFiles/html/doTurn
                if (response.ok) {
                    console.log('game has started', response.json());
                    window.location.href = '/staticFiles/html/doTurn';
                }
                // if error occurs => display customText div and print error message
                else {
                    document.getElementById('customText').style.display = 'block';
                    document.getElementById('customText').innerText = 'Es ist ein Fehler aufgetreten!';
                    // if the response status is 422 (Unprocessable Entity) then display the error message
                    if (response.status === 422) {
                        response.text().then(value => document.getElementById('customText').innerText = value)
                    }
                }
            });
    });
}