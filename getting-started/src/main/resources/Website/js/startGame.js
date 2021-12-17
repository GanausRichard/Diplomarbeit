form.addEventListener("submit", function (event) {
    // stop form submission
    event.preventDefault();

    // validate the form

    // if valid, submit the form
    const form = document.getElementById("gameSettings");
    const name = form.elements["name"].value;
    const gameMode = form.elements["gameMode"].value;
    const data = {name, gameMode};

    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    fetch('http://localhost:8080/connectFour/startGame', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: myHeaders})
        .then(response => {console.log('Success:', response.json); });
});
