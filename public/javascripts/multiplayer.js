function getCellClass(cell) {
    if (cell.isFlagged) {
        return "cell-flagged";
    }
    if (cell.isMine && !cell.isHidden) {
        return "cell-opened cell-mine";
    }
    if (cell.isHidden) {
        return "";
    }

    return "cell-opened " + (cell.value > 0 ? "cell-value-" + cell.value : " ");
}

function getCellContent(cell) {
    if (cell.isFlagged) {
        return "🏴";
    }
    if (cell.isHidden) {
        return "?";
    }
    if (cell.isMine) {
        return "💣";
    }
    return cell.value > 0 ? cell.value.toString() : " ";
}

function flagCell(x, y) {
    $.get(`/api/multiplayer/flag/${userId}/${x}/${y}`, function (data) {
        loadGrid("game-board", data);
    });
}

function openCell(x, y) {
    $.get(`/api/multiplayer/place/${userId}/${x}/${y}`, function (data) {
        if (data.cells[x][y].isMine) $("#gameOverModal").modal("show");
        loadGrid("game-board", data);
    });
}
function loadGrid(id, gridData, addClick = true) {
    let gameContainer = $(`#${id}`);
    gameContainer.empty();

    rowCount = 0;
    for (let row of gridData.cells) {
        cellCount = 0;
        let rowDiv = $('<div class="game-row"></div>');
        gameContainer.append(rowDiv);
        for (let cell of row) {
            let cellClass = getCellClass(cell);
            let cellDiv = $(
                `<div data-x="${rowCount}" data-y="${cellCount}" class="cell ${cellClass}"><span>${getCellContent(
                    cell
                )}</span></div>`
            );
            cellCount++;
            rowDiv.append(cellDiv);
        }
        rowCount++;
    }
    if (!addClick) return;
    $(`#${id} .cell`).each(function () {
        $(this).on("mousedown", function (event) {
            event.preventDefault();
            var x = $(this).data("x");
            var y = $(this).data("y");

            if (event.button == 0) {
                openCell(x, y);
            }
            if (event.button == 2) {
                flagCell(x, y);
            }
        });

        $(this).on("contextmenu", function (event) {
            event.preventDefault();
        });
    });
}

function ping() {
    socket.send('ping');
    tm = setTimeout(function () {
        console.log('ping timeout');
        socket.close();
    }, 5000);
}

function pong() {
    clearTimeout(tm);
}


//  generate random user id 
const userId = Math.floor(Math.random() * 10000) + 1;
console.log(userId);

//  connect to ws
const socket = new WebSocket(`ws://localhost:9000/websocket/${userId}`);


socket.onopen = function () {
    setInterval(ping, 30000);
}

//  register user id on server
socket.addEventListener('open', function (event) {
    socket.send(userId);
});

//  receive message from server
socket.addEventListener('message', function (event) {
    console.log('Message from server ', event.data);
    if (event.data === 'start') {
        //  generate board
        $.ajax({
            url: `/api/multiplayer/grid/${userId}`,
            type: "GET",
            success: (data) => {
                loadGrid("game-board", data);
            },
            error: (error) => {
                console.log(error);
            },
        });
        $.ajax({
            url: `/api/multiplayer/opponent/${userId}`,
            type: "GET",
            success: (data) => {
                loadGrid("game-board-opponent", data, false);
            },
            error: (error) => {
                console.log(error);
            },
        });
    }

    if (event.data === 'update') {
        $.ajax({
            url: `/api/multiplayer/opponent/${userId}`,
            type: "GET",
            success: (data) => {
                loadGrid("game-board-opponent", data, false);
            },
            error: (error) => {
                console.log(error);
            },
        });
    }

    if (event.data === 'wait') {
        console.log('waiting');
    }
    if (event.data === 'pong') {
        pong();
    }
});

