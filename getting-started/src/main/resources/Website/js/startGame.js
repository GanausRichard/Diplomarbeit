let gameMode = "pve";
let difficultyLevel = 10;

//if gameMode gets set to pvp, then display inputName2 (for name input)
function getGameMode(value) {
    gameMode = value;
    if (gameMode === 'pvp') {
        document.getElementById('inputName2').style.display = 'block';
    }
    else if (gameMode === 'pve') {
        document.getElementById('inputName2').style.display = 'none';
    }
}

//set the difficulty of the game
function setDifficulty(value) {
    difficultyLevel = value;
}

function hideShowDivs(hiddenElement, shownElement) {
    document.getElementById(hiddenElement).style.display = 'none';
    document.getElementById(shownElement).style.display = 'block';
}

window.onload = function startGame() {
    const form = document.getElementById('gameSettings');
    document.getElementById('inputName2').style.display = 'none';  //hide input box for name2 at when window loaded
    document.getElementById('customText').style.display = 'none';  //hide input box for name2 at when window loaded
    document.getElementById('login').style.display = 'none';

    form.addEventListener('submit', function (event) {
        // stop form submission
        event.preventDefault();

        // submit the form
        const name1 = form.elements['name1'].value;
        const name2 = form.elements['name2'].value;
        const endNode = difficultyLevel;
        const data = {name1, name2, gameMode, endNode};

        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');

        document.getElementById('customText').style.display = 'none';

        fetch('http://' + window.location.host + '/connectFour/startGame', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: myHeaders})
            .then(response => {
                if (response.ok) {
                    console.log('game has started', response.json());
                    window.location.href = '/staticFiles/html/doTurn';
                }
                else {
                    document.getElementById('customText').style.display = 'block';
                    document.getElementById('customText').innerText = 'Es ist ein Fehler aufgetreten!';
                    //if the response status is 422 (Unprocessable Entity) then display the error message
                    if (response.status === 422) {
                        response.text().then(value => document.getElementById('customText').innerText = value)
                    }
                }
            });
    });
}