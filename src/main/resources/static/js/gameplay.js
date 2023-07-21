const chatContentElm = document.getElementById('chat-content')
const chatInputElm = document.getElementById('chat-input');
const PATH_API = "/api/v1"
initChatInputAction();
function addNewMessage() {
    let message = chatInputElm.value;

    if (checkValidMessage(message) == true) {
        var newChatElement = document.createElement('div');
        newChatElement.classList.add('mb-2', 'chat-element');
        newChatElement.style.fontSize = '14px'
        newChatElement.innerHTML = createChatElm(getCurrentTime(), "XYZ", message)
        chatContentElm.appendChild(newChatElement);

        chatInputElm.value = '';
        chatContentElm.scrollTop = chatContentElm.scrollHeight
    }

}

function createChatElm(time, author, message) {
    var newChatElm =
        `
        <span class="fw-bold">${time}</span>
        <span class="fw-bold me-2 text-danger">${author}: </span>
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
            var message = chatInputElm.value;

            if (checkValidMessage(message) == true) {
                addNewMessage(message);
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
// --------------------

window.addEventListener('beforeunload', function (e) {
    let confirmationMessage = "Custom Changes you made may not be saved. Are you sure you want to leave this page?";
    e.returnValue = confirmationMessage;

    let idGame = window.location.href.match(/\/gameplay\/(\d+)/);
    let url = PATH_API + `/quit-game/${idGame[1]}`
    $.get(url, function (response){
        console.log(response);
    })

    return confirmationMessage;
});
