function doRobotTurn() {
    fetch('http://localhost:8080/connectFour/doRobotTurn', {
        method: 'POST',
        redirect: 'follow' })
        .then(response => {console.log("the robot made its decision", response.json()); });
}
