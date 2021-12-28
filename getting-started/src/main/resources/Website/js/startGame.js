function getGameMode(buttonId) {
    const btnId = buttonId;
}

const form = document.querySelector("#gameSettings");

form.addEventListener("submit", function (event) {
    // stop form submission
    event.preventDefault();

    // if valid, submit the form
    const form = document.getElementById("gameSettings");
    const name = form.elements["name"].value;
    const gameMode = form.elements["gameMode1"].value;
    const data = {name, gameMode};

    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    fetch('http://localhost:8080/connectFour/start', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: myHeaders})
        .then(response => {console.log("game has started", response.json()); });
});
