function getColumn(columnId) {
	const column = document.getElementById("column" + columnId).value;
	const data = {column};
  
	const myHeaders = new Headers();
	myHeaders.append('Content-Type', 'application/json');
  
	fetch('http://localhost:8080/connectFour/doTurn', {
		method: 'POST',
		headers: myHeaders,
		body: JSON.stringify(data) })
		.then(response => response.json())
		.then(data => { doThisEachTurn(data)});
}

function doThisEachTurn(data) {
	//console log
	console.log("players turn has finished", data);

	//Single player
	if (data['settings']['gameMode'] === 'pve') {
		if (endGame(data, 'Du hast das Spiel gewonnen.\nNicht schlecht!') === true) {
			return;
		}
		else {
			//for next turn
			document.getElementById('customText').innerText = 'Der Roboter ist am Zug.';
			doRobotTurn();
		}
	}
	//Multiplayer
	else if (data['settings']['gameMode'] === 'pvp') {
		if (endGame(data, getWinnersName(data) + ' hat das Spiel gewonnen.\nGratulation!') === true) {
			return;
		}
		else {
			//for next turn
			document.getElementById('customText').innerText = getNameForNextTurn(data) + ' ist am Zug.';
		}
	}
}

function doRobotTurn() {
	fetch('http://localhost:8080/connectFour/doRobotTurn', {
		method: 'POST',
		redirect: 'follow' })
		.then(response => response.json())
		.then(data => {
			console.log("the robot made its decision", data);
			if (endGame(data, "Der Roboter hat das Spiel gewonnen.\nVersuch's ein andermal!") === true) {
				return;
			}
			else {
				//for next turn
				document.getElementById('customText').innerText = 'Du bist am Zug.';
			}
		});
}

function endGame(data, output) {
	if(data['win'] === true) {
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('endGameBtn').style.display = 'inline';
		document.getElementById('endGameBtn').style.alignSelf = 'center';
		document.getElementById('customText').innerText = output;
		return true;
	}
	else if (data['move'] === (data['ROW_QUANTITY'] * data['COLUMN_QUANTITY'])) {
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('endGameBtn').style.display = 'inline';
		document.getElementById('endGameBtn').style.alignItems = 'center';
		document.getElementById('customText').innerText = 'Schade, ein Unetschieden!';
		return true;
	}
	return false;
}

function getWinnersName(data) {
	if ((data['move'] % 2) === 1) {
		return data['settings']['name1'];
	}
	else if ((data['move'] % 2) === 0) {
		return data['settings']['name2'];
	}
}

function getNameForNextTurn(data) {
	if ((data['move'] % 2) === 1) {
		return data['settings']['name2'];
	}
	else if ((data['move'] % 2) === 0) {
		return data['settings']['name1'];
	}
}


