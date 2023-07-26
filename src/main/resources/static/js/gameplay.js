const chatContentElm = document.getElementById('chat-content')
const chatInputElm = document.getElementById('chat-input');

const gameplayContentElm = document.getElementById("gameplay-content")
const gameplayInputElm = document.getElementById("gameplay-input")

const PATH_API = "/api/v1"
const roomId = $('#room-id').text()

const turnSound = new Audio("/sound/turn.wav")
const mistakeSound = new Audio("/sound/wrong.wav")
const bingbongSound = new Audio("/sound/bingbong.mp3")

var stompClient = null;

var firstPlayerId = null;
var firstPlayerColor = null;
var firstPlayerName = null;
var secondPlayerId = null;
var secondPlayerColor = null;
var secondPlayerName = 'Waiting';
var currPlayerName = null;

var currPlayerId = null;
var youselfId = null;
$(document).ready(function (){
    initChatInputAction();
    initWordsInputAction();
    connectSocket();
    getAllPlayerInfor();
    disableGameplay();

    if (secondPlayerId == null)
        youselfId = firstPlayerId;
    else
        youselfId = secondPlayerId;
    console.log(youselfId)
})
function updateAllPlayerInfor(message){
    firstPlayerId = message.toWhom.id
    firstPlayerName = message.toWhom.name
    firstPlayerColor ='#'+ message.toWhom.color

    secondPlayerId = message.fromWho.id
    secondPlayerName = message.fromWho.name
    secondPlayerColor = '#'+ message.fromWho.color

    setSecondPlayerInfor();
}
function getAllPlayerInfor(message){
    firstPlayerId = $('#first-player').attr("playerId");
    firstPlayerName = $('#first-player').text();
    firstPlayerColor =$('#first-player').attr("color");

    secondPlayerId = $('#second-player').attr("playerId");
    secondPlayerName = $('#second-player').text();
    secondPlayerColor = $('#second-player').attr("color");

    setSecondPlayerInfor();
}
function setSecondPlayerInfor(){
    $('#second-player').text(secondPlayerName)
    $('#second-player').css('color', secondPlayerColor);
}
// --------------------

window.addEventListener('beforeunload', function (e) {
    let confirmationMessage = "Custom Changes you made may not be saved. Are you sure you want to leave this page?";
    e.returnValue = confirmationMessage;

    let idGame = window.location.href.match(/\/gameplay\/(\d+)/);
    let url = PATH_API + `/quit-game/${idGame[1]}`
    $.get(url, function (response){
    })

    return confirmationMessage;
});

// Handle websocket
function connectSocket(){
    var socket = new SockJS("/websocket-room");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame){
        stompClient.subscribe("/gameplay/room/" + roomId, function (messageOutput){
            handleOutput (messageOutput.body);
        })
        startGame();
    });
}

function handleOutput(messageOutput) {
    jsonMessage = jQuery.parseJSON(messageOutput)
    switch (jsonMessage.type){
        case "JOIN":
            handleJoinGame(jsonMessage)
            break;
        case "QUIT":
            break;
        case "GAME":
            handleWords(jsonMessage);
            break;
        case "CHAT":
            handleChat(jsonMessage);
            break;
        case "START":
            handleStartGame(jsonMessage);
            break;
    }
}

function sendPostRequestToServer(url, value){
    $.ajax({
        url: url,
        type: 'POST',
        data:  value,
        contentType: "text/plain",
    })
}


// ---------- handle chat-----------------
function handleChat(message){
    addNewMessage(message);
}
function sendMessageToServer() {
    let message = chatInputElm.value;
    chatInputElm.value = '';

    if (checkValidMessage(message) == true) {
        let url = PATH_API + `/gameplay/${roomId}/chat`
        sendPostRequestToServer (url, message);
    }

}

function addNewMessage(message){
    var newChatElement = document.createElement('div');
    newChatElement.classList.add('mb-2', 'chat-element');
    newChatElement.style.fontSize = '14px'
    newChatElement.innerHTML = createChatElm(getCurrentTime(), message.fromWho.name, message.message, '#'+message.fromWho.color)
    chatContentElm.appendChild(newChatElement);

    chatContentElm.scrollTop = chatContentElm.scrollHeight
}

function createChatElm(time, author, message, color) {
    var newChatElm =
        `
        <span class="fw-bold">${time}</span>
        <span style="color: ${color}" class="fw-bold me-2">${author}: </span>
        <span>${message}</span>
    `;
    return newChatElm;
}

function getCurrentTime() {
    var currentTime = new Date();
    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes();
    var suffix = hours >= 12 ? "PM" : "AM";
    hours = hours % 12 || 12; // Chuyển đổi sang chuẩn 12 giờ

    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    var formattedTime = hours + ":" + minutes + " " + suffix;
    return formattedTime
}

function initChatInputAction() {
    chatInputElm.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            let message = chatInputElm.value;

            if (checkValidMessage(message) == true) {
                sendMessageToServer(message);
            }

            chatInputElm.value = '';
        }
    });
}
function checkValidMessage(value) {
    if (value == '' || value == undefined)
        return false;
    return true;
}
// ---------- end handle chat-----------------


// ---------- start words -------------------
function handleWords(message){
    addNewWords(message);
}
function sendWordsToServer(){
    let message = gameplayInputElm.value;

    if (checkIfValueExists(message) == true) {
        mistakeSound.play();
        alert(`'${message}' already exits`);
    }
    else if (checkValidWords(message) == false){
        mistakeSound.play();
        alert("Enter valid value, please")
    }
    else if (checkStartLetter(message) == false){
        mistakeSound.play();
        let lastWord = getLastWord();
        alert(`You must start with '${lastWord.charAt(lastWord.length-1)}' letter`)
    }
    else if (checkValidWords(message) == true) {
        let url = PATH_API + `/gameplay/${roomId}/words`
        sendPostRequestToServer (url, message);
    }
}
function checkStartLetter(message){

    let lastWord = getLastWord();
    if (lastWord == undefined || lastWord == null)
        return true;

    if (lastWord.charAt(lastWord.length-1) != message.charAt(0))
        return false;

    return true;
}
function getLastWord(){
    let lastElm = document.querySelector('#gameplay-content .gameplay-word:last-child');
    if (lastElm != null || lastElm != undefined) {
        return lastElm.querySelector('span').textContent;
    }
}
function addNewWords(message){

    var newChatElement = document.createElement('div');
    newChatElement.classList.add('gameplay-word', 'd-inline-block', 'me-2', 'my-2');

    newChatElement.innerHTML = createWordsElm( message.message, '#'+message.fromWho.color)
    gameplayContentElm.appendChild(newChatElement);

    gameplayInputElm.value = '';
    gameplayContentElm.scrollTop = gameplayContentElm.scrollHeight

    changeTurn(message);

}

function createWordsElm(message, color) {
    var newChatElm =
        `
           <span style="color: ${color}">${message}</span>
           <img src="/img/arrow.svg" alt="" style="width: 26px;">
    `;
    return newChatElm;
}


function initWordsInputAction() {
    gameplayInputElm.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            let message = gameplayInputElm.value;

            sendWordsToServer(message);

            gameplayInputElm.value = '';
        }
    });
}
function checkValidWords(value) {
    if (typeof value !== 'string') {
        return false;
    }

    if (value == '' || value == undefined)
        return false;

    return /^[a-zA-Z]+$/.test(value);

}
function checkIfValueExists(value) {
    // Get all elements with the class "gameplay-word"
    const gameplayWords = document.getElementsByClassName("gameplay-word");

    // Convert HTMLCollection to an array for easier iteration
    const gameplayWordsArray = Array.from(gameplayWords);

    // Iterate through each element and check if the value matches
    for (const element of gameplayWordsArray) {
        const span = element.querySelector("span");
        if (span.innerText.trim() === value) {
            return true;
        }
    }
    return false;
}

function changeTurn(message){
    currPlayerName = message.toWhom.name;
    currPlayerId = message.toWhom.id;
    let directionElement = $('#direction')
    directionElement.text(`${currPlayerName}'s turn!`);
    disableGameplay();
    enableGameplay();
}
// ---------- end words ------------------


function handleJoinGame(message){
    updateAllPlayerInfor(message)
    bingbongSound.play();
}
function startGame(){
    let url = PATH_API + `/gameplay/${roomId}/start`;
    $.get(url, function (response){

    })
}
function handleStartGame(message){
    console.log("---START GAME---")
    currPlayerName = message.fromWho.name;
    currPlayerId = message.fromWho.id;
    countdown();
}
function countdown() {
    let count = 5;
    let directionElement = $('#direction')
    const interval = setInterval(() => {
        directionElement.text(count);
        count--;
        if (count === 0) {
            clearInterval(interval);
            directionElement.text(`${currPlayerName}'s turn!`);
            enableGameplay();
        }
    }, 1000);
}
function disableGameplay(){
    gameplayInputElm.disabled = true;
    document.getElementById("gameplay-btn").disabled = true;
}
function enableGameplay(){
    console.log("your id ", youselfId)
    console.log("curr palyer id ", currPlayerId)
    if (youselfId == currPlayerId){
        gameplayInputElm.disabled = false;
        document.getElementById("gameplay-btn").disabled = false;
        turnSound.play();
    }
}