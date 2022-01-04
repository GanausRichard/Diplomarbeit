let gameMode = "pve";

function getGameMode(value) {
    gameMode = value;
    if (gameMode == 'pvp') {
        document.getElementById('inputBox2').style.display = 'block';
    }
    else if (gameMode == 'pve') {
        document.getElementById('inputBox2').style.display = 'none';
    }
}

window.onload = function startGame() {
    const form = document.getElementById('gameSettings');
    document.getElementById('inputBox2').style.display = 'none';  //hide input box for name2 at when window loaded

    form.addEventListener('submit', function (event) {
        // stop form submission
        event.preventDefault();

        // submit the form
        const name1 = form.elements['name1'].value;
        const name2 = form.elements['name2'].value;
        const data = {name1, name2, gameMode};

        const myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');

        fetch('http://localhost:8080/connectFour/start', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: myHeaders})
            .then(response => {
                console.log('game has started', response.json());
                window.location.href = '/staticFiles/html/doTurn';
            });
    });
}

