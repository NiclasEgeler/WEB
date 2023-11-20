console.log("Minesweeper");
document.querySelectorAll(".cell").forEach((element) => {
    element.addEventListener("mousedown", (event)=>  {
        event.preventDefault();
        var x = element.getAttribute("data-x");
        var y = element.getAttribute("data-y");

        if(event.button == 0) {
            console.log("left click")
            window.location.href = `/place/${x}/${y}`;
        }
        // fetch if cell is mine when api is available. If yes show alert box with game over and play again popup
        //$('#gameOverModal').modal('show');
        if(event.button == 2)
        window.location.href = `/flag/${x}/${y}`;
    });
    element.addEventListener('contextmenu', (event) => {
        event.preventDefault();
    });
});

document.querySelectorAll(".mine").forEach((element) => {
    element.addEventListener("click", (event)=>  {
        event.preventDefault();
        $('#gameOverModal').modal('show');
    });
});

document.getElementById("playAgainButton").addEventListener("click", function() {
    window.location.href = '/undo';
});