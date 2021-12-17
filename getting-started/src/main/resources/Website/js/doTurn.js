function getColumn(columnId) {
	const column = document.getElementById("column" + columnId).value;
	const data = {column};
  
	const myHeaders = new Headers();
	myHeaders.append('Content-Type', 'application/json');
  
	fetch('http://localhost:8080/connectFour/doTurn', {
		method: 'POST',
		headers: myHeaders,
		body: JSON.stringify(data) })
	.then(response => {console.log(response.json()); });
}

