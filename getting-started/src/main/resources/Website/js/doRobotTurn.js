function doRobotTurn() {
    fetch('http://localhost:8080/connectFour/doRobotTurn', {
        method: 'POST',
        redirect: 'follow' })
        .then(response => response.json())
        .then(data => {
            console.log("the robot made its decision", data);
            if(data['win'] == true)
            {
                window.alert("Der Roboter hat das Spiel gewonnen. Versuch's ein andermal!");
            }
        });
}
