// the getColumn function is executed when a button is clicked
// the function sends a request to the server (the chosen column gets transmitted)
function getColumn(columnId) {
	// disable the buttons for column transmission (to avoid errors during doRobotTurn)
	disableButtons(true);

	// save column in data for fetch request
	const column = document.getElementById("column" + columnId).value;
	const data = {column};

	// create a header
	const myHeaders = new Headers();
	myHeaders.append('Content-Type', 'application/json');

	// request resource .../connectFour/doTurn
	fetch('http://' + window.location.host + '/connectFour/doTurn', {
		method: 'POST',
		headers: myHeaders,
		body: JSON.stringify(data)
	})
		.then(response => response.json())
		.then(data => {doThisEachTurn(data)});
}

function doRobotTurn() {
	// request resource .../connectFour/doRobotTurn
	fetch('http://' + window.location.host + '/connectFour/doRobotTurn', {
		method: 'POST',
		redirect: 'follow' })
		.then(response => response.json())
		// when response received => execute the following code
		.then(data => {
			// console log the response object with the message "the robot made its decision"
			console.log("the robot made its decision", data);
			// display the current state of the game
			displayPlayingField(data);

			// if the game is finished => wait until the gaming pieces are at their initial position
			if (endGame(data, "Der Roboter hat das Spiel gewonnen.\nVersuch's ein andermal!") === true) {
				waitUntilAcknowledged();
			}
			else {
				// display message and disable buttons
				document.getElementById('customText').innerText = 'Du bist am Zug.';
				disableButtons(false, data);
			}
		});
}

function waitUntilAcknowledged() {
	// request resource .../connectFour/waitForInitialState
	fetch('http://' + window.location.host + '/connectFour/waitForInitialState', {
		method: 'POST',
		redirect: 'follow' })
		.then(response => response.json())
		// when response received => display the endGameBtn button (onclick => restart game)
		.then(data => {
			document.getElementById('endGameBtn').style.display = 'inline';
			document.getElementById('endGameBtn').style.alignSelf = 'center';
		});
}

// this function gets executed each turn when a response is received
function doThisEachTurn(data) {
	// console log the response object with the message "players turn has finished"
	console.log("players turn has finished", data);
	// display the current state of the game
	displayPlayingField(data);

	// single player
	if (data.settings.gameMode === 'pve') {
		// if the game is finished => wait until the gaming pieces are at their initial position
		if (endGame(data, 'Du hast das Spiel gewonnen.\nNicht schlecht!') === true) {
			waitUntilAcknowledged();
		}
		else {
			// display message and start doRobotTurn
			document.getElementById('customText').innerText = 'Der Roboter ist am Zug.';
			doRobotTurn();
		}
	}
	// multiplayer
	else if (data.settings.gameMode === 'pvp') {
		// enables buttons
		disableButtons(false, data);
		// if the game is finished => wait until the gaming pieces are at their initial position
		if (endGame(data, getWinnersName(data) + ' hat das Spiel gewonnen.\nGratulation!') === true) {
			waitUntilAcknowledged();
		}
		else {
			// display message
			document.getElementById('customText').innerText = getNameForNextTurn(data) + ' ist am Zug.';
		}
	}
}

// the disableButtons function disables/enables the buttons which are used for column input
function disableButtons(disable, data) {
	for (let i = 1; i < 8; i++) {
		// disable or enable all 7 buttons
		document.getElementById("column" + i).disabled = disable;
		// disable a button if column is already filled
		if (data) {
			let lastRow = data['ROW_QUANTITY'] - 1;
			let column = i - 1;
			if (data.matrix[lastRow][column] !== 0) {
				document.getElementById("column" + i).disabled = true;
			}
		}
	}
}

// when the game is finished => this function returns true
function endGame(data, output) {
	var endGame = false;

	if (data.win === true) {
		// the input buttons get hidden and a message is displayed
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('customText').innerText = output;
		endGame = true;
	}
	else if (data.move === (data.ROW_QUANTITY * data.COLUMN_QUANTITY)) {
		// the input buttons get hidden and a message is displayed
		document.getElementById('inputBox').style.display = 'none';
		document.getElementById('customText').innerText = 'Schade, ein Unetschieden!';
		endGame = true;
	}
	return endGame;
}

// the displayPlayingField function loads images to
// display the current state of the game
function displayPlayingField(data) {
	let column = 0;

	// iterate over all positions in the matrix and load a specific image for each player
	do {
		let row = 0;
		do {
			// if player 1 placed a gaming piece => display .../img/playstoneYellow.png at the correct position
			if (data.matrix[row][column] === 1) {
				document.getElementById('playingFieldC' + column + 'R' + row).src = '../img/playstoneYellow.png';
			}
			// if player 1 placed a gaming piece => display .../img/playstoneYellow.png at the correct position
			else if (data.matrix[row][column] === 2) {
				document.getElementById('playingFieldC' + column + 'R' + row).src = '../img/playstoneRed.png';
			}
			row++;
		} while(row < data.ROW_QUANTITY)
		column++;
	} while(column < data.COLUMN_QUANTITY)
}

// this function returns the name of the winner
function getWinnersName(data) {
	if ((data.move % 2) === 1) {
		return data['settings']['name1'];
	}
	else if ((data.move % 2) === 0) {
		return data.settings.name2;
	}
}

// this function returns the name of the player who is up next
function getNameForNextTurn(data) {
	if ((data.move % 2) === 1) {
		return data.settings.name2;
	}
	else if ((data.move % 2) === 0) {
		return data.settings.name1;
	}
}


