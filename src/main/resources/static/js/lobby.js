const PATH_API = '/api/v1'
$(document).ready(function (){
    let url = PATH_API + '/player'
    $.get(url, function (response){
        setPlayerColor(response.color);
    })
})

function setPlayerColor(color){
    $('#player-name').css('color', "#" + color);
}

function createRoom(){
    let url = PATH_API + '/newgame'
    $.get(url, function (response){
        window.location.href = `/gameplay/${response}`
    })
}
function joinGame(idGame){
    let url = PATH_API + `/join-game/${idGame}`
    $.get(url, function (response){
        if (response == true){
            window.location.href = `/gameplay/${idGame}`
        }
        else{
            alert("This room is full or not exit!")
            window.location.reload();
        }
    })
}