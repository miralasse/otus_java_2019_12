let stompClient = null;

function initWebSocket() {
    stompClient = Stomp.over(new SockJS('/user-websocket'));
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/user', (msg) => {
            console.log('response from /topic/user: ' + JSON.parse(msg.body));
            getUsers();
        });

        stompClient.subscribe('/topic/allUsers', (msg) => {
            console.log('response from /topic/allUsers: ' + JSON.parse(msg.body));
            showAllUsers(JSON.parse(msg.body));
        });
    });

    setTimeout(function () {
        getUsers();
    }, 1000);
}

function createUser() {
    console.log('createUser');
    stompClient.send("/app/createUser", {}, JSON.stringify({
        'name': document.getElementById("name").value,
        'login': document.getElementById("login").value,
        'password': document.getElementById("password").value,
    }));
}

function getUsers() {
    console.log('getUsers');
    stompClient.send("/app/getAllUsers", {}, null);
}

function showAllUsers(usersData) {
    console.log(usersData)
    if (Object.keys(usersData).length > 0) {
        let table = document.getElementById("users");
        table.innerHTML = "";
        let row, cell;

        for (let i = 0; i < usersData.length; i++) {
            row = table.insertRow();
            cell = row.insertCell();
            cell.textContent = usersData[i].id;
            cell = row.insertCell();
            cell.textContent = usersData[i].name;
            cell = row.insertCell();
            cell.textContent = usersData[i].login;
            cell = row.insertCell();
            cell.textContent = usersData[i].password;
        }
    }
}