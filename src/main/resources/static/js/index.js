const PATH_API = '/api/v1'
var playerNameElm = document.getElementById('player-name')
var warningElm = document.getElementById('warning-inp')
var colorValue = document.getElementById('color-inp')



$(document).ready(function (){
    $('#name-inp').keypress(function (event){
        if (event.which === 13){
            submitName();
        }
    })

    $(window).bind("pageshow", function(event) {
        if (event.originalEvent.persisted) {
            window.location.reload();
        }
    });
})
function submitName(){
    let value = document.getElementById('name-inp').value;
    if (value.trim() == '' || value == undefined){
        document.getElementById('name-inp').value = '';
        warningElm.style.display = 'block'
    }
    else{
        sendToServer (value);
    }
}

function sendToServer (nameVal){
    let color = document.getElementById('color-inp').value.replace("#", "");
    let url = PATH_API + `/new-player/${nameVal}/${color}`
    $.get(url, function (response){
        window.location.href = "/lobby";
    })
}

function inputingName(value){
    warningElm.style.display = 'none'
    playerNameElm.textContent = value;
    setPlayerColorName();
}

function changeColor(value){
    colorValue = value;
    setPlayerColorName();
}

function setPlayerColorName(){
    playerNameElm.style.color = colorValue;
}