function doRobotTurn() {
    fetch('http://localhost:8080/connectFour/doRobotTurn', {
        method: 'POST',
        redirect: 'follow' })
        .then(response => response.json())
        .then(data => {
            console.log("the robot made its decision", data);
            endGame(data, "Der Roboter hat das Spiel gewonnen.\nVersuch's ein andermal!");
            //for next turn
            document.getElementById('customText').innerText = data['settings']['name1'] + ' ist am Zug.';
        });
}

function endGame(data, output) {
    if(data['win'] == true) {
        window.alert(output);
        window.location.href = "/staticFiles/html/startGame";
    }
    else if (data['move'] == (data['ROW_QUANTITY'] * data['COLUMN_QUANTITY'])) {
        window.alert('Schade, ein Unetschieden!');
        window.location.href = "/staticFiles/html/startGame";
    }
}
