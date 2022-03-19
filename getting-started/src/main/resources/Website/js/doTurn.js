function getColumn(columnId) {
		buttonsDisable(true);

	const column = document.getElementById("column" + columnId).value;
	const data = {column};

	const myHeaders = new Headers();
	myHeaders.append('Content-Type', 'application/json');

	fetch('http://' + window.location.host + '/connectFour/doTurn', {
		method: 'POST',
		headers: myHeaders,
		body: JSON.stringify(data)
	})
		.then(response => response.json())
		.then(data => {doThisEachTurn(data)});
}

function doRobotTurn() {
	fetch('http://' + window.location.host + '/connectFour/doRobotTurn', {
		method: 'POST',
		redirect: 'follow' })
		.then(response => response.json())
		.then(data => {
			console.log("the robot made its decision", data);
			displayPlayingField(data);

			if (endGame(data, "Der Roboter hat das Spiel gewonnen.\nVersuch's ein andermal!") === true) {
				waitUntilAcknowledged();
			}
			else {
				//for next turn
				document.getElementById('customText').innerText = 'Du bist am Zug.';
				buttonsDisable(false, data);
			}
		});
}

function waitUntilAcknowledged() {
	//write function waitForAcknowledge in ConnnectFourImplV3
	fetch('http://' + window.location.host + '/connectFour/waitForInitialState', {
		method: 'POST',
		redirect: 'follow' })
		.then(response => response.json())
		.then(data => {
			document.getElementById('endGameBtn').style.display = 'inline';
			document.getElementById('endGameBtn').style.alignSelf = 'center';
		});
}

function doThisEachTurn(data) {
	//console log
	console.log("players turn has finished", data);
	displayPlayingField(data);

	//Single player
	if (data.settings.gameMode === 'pve') {
		if (endGame(data, 'Du hast das Spiel gewonnen.\nNicht schlecht!') === true) {
			waitUntilAcknowledged();
		}
		else {
			//for next turn
			document.getElementById('customText').innerText = 'Der Roboter ist am Zug.';
			doRobotTurn();
		}
	}
	//Multiplayer
	else if (data.settings.gameMode === 'pvp') {
		//enables buttons
		buttonsDisable(false, data);
		if (endGame(data, getWinnersName(data) + ' hat das Spiel gewonnen.\nGratulation!') === true) {
			waitUntilAcknowledged();
		}
		else {
			//for next turn
			document.getElementById('customText').innerText = getNameForNextTurn(data) + ' ist am Zug.';
		}
	}
}

function buttonsDisable(disabled, data) {
	for (let i = 1; i < 8; i++) {
		//disable or enable all buttons
		document.getElementById("column" + i).disabled = disabled;
		//disable a button if column is already filled
		if (data) {
			let lastRow = data['ROW_QUANTITY'] - 1;
			let column = i - 1;
			if (data.matrix[lastRow][column] !== 0) {
				document.getElementById("column" + i).disabled = true;
			}
		}
	}
}

function endGame(data, output) {
	var endGame = false;

	if (data.win === true) {
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('customText').innerText = output;
		endGame = true;
	}
	else if (data.move === (data.ROW_QUANTITY * data.COLUMN_QUANTITY)) {
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('customText').innerText = 'Schade, ein Unetschieden!';
		endGame = true;
	}
	return endGame;
}

function displayPlayingField(data) {
	let column = 0;
	do {
		let row = 0;
		do {
			if (data.matrix[row][column] === 0) {
				document.getElementById('playingFieldC' + column + 'R' + row).src = '../img/emptyPlayingField.png';
			}
			else if (data.matrix[row][column] === 1) {
				document.getElementById('playingFieldC' + column + 'R' + row).src = '../img/playstoneYellow.png';
			}
			else {
				document.getElementById('playingFieldC' + column + 'R' + row).src = '../img/playstoneRed.png';
			}
			row++;
		} while(row < data.ROW_QUANTITY)
		column++;
	} while(column < data.COLUMN_QUANTITY)
}

function getWinnersName(data) {
	if ((data.move % 2) === 1) {
		return data['settings']['name1'];
	}
	else if ((data.move % 2) === 0) {
		return data.settings.name2;
	}
}

function getNameForNextTurn(data) {
	if ((data.move % 2) === 1) {
		return data.settings.name2;
	}
	else if ((data.move % 2) === 0) {
		return data.settings.name1;
	}
}


