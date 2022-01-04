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
		.then(data => { doThisEachTurn(data); });
}

function doThisEachTurn(data) {
	//console log
	console.log("players turn has finished", data);

	//Single player
	if (data['settings']['gameMode'] == 'pve') {
		endGame(data, 'Du hast das Spiel gewonnen.\nNicht schlecht!');
		//for next turn
		document.getElementById('customText').innerText = 'Der Roboter ist am Zug.';
	}
	//Multiplayer
	else if (data['settings']['gameMode'] == 'pvp') {
		endGame(data, getWinnersName(data) + ' hat das Spiel gewonnen.\nGratulation!');
		//for next turn
		document.getElementById('customText').innerText = getNameForNextTurn(data) + ' ist am Zug.';
	}
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

function getWinnersName(data) {
	if ((data['move'] % 2) == 1) {
		return data['settings']['name1'];
	}
	else if ((data['move'] % 2) == 0) {
		return data['settings']['name2'];
	}
}

function getNameForNextTurn(data) {
	if ((data['move'] % 2) == 1) {
		return data['settings']['name2'];
	}
	else if ((data['move'] % 2) == 0) {
		return data['settings']['name1'];
	}
}


