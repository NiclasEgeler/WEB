console.log("Minesweeper");
document.querySelectorAll(".cell").forEach((element) => {
    element.addEventListener("mousedown", (event)=>  {
        event.preventDefault();
        var x = element.getAttribute("data-x");
        var y = element.getAttribute("data-y");

        if(event.button == 0)
        window.location.href = `/place/${x}/${y}`;
        if(event.button == 2)
        window.location.href = `/flag/${x}/${y}`;
    });
    element.addEventListener('contextmenu', (event) => {
        event.preventDefault();
    });
});