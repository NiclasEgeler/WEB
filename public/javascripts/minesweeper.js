// document.querySelectorAll(".cell").forEach((element) => {
//   element.addEventListener("mousedown", (event) => {
//     event.preventDefault();
//     var x = element.getAttribute("data-x");
//     var y = element.getAttribute("data-y");

//     if (event.button == 0) {
//       console.log("left click");
//       window.location.href = `/place/${x}/${y}`;
//     }
//     // fetch if cell is mine when api is available. If yes show alert box with game over and play again popup
//     //$('#gameOverModal').modal('show');
//     if (event.button == 2) window.location.href = `/flag/${x}/${y}`;
//   });
//   element.addEventListener("contextmenu", (event) => {
//     event.preventDefault();
//   });
// });

// document.querySelectorAll(".mine").forEach((element) => {
//   element.addEventListener("click", (event) => {
//     event.preventDefault();
//     $("#gameOverModal").modal("show");
//   });
// });

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
  $.get(`/api/flag/${x}/${y}`, function (data) {
    loadGrid(data);
  });
}

function openCell(x, y) {
  $.get(`/api/place/${x}/${y}`, function (data) {
    if (data.cells[x][y].isMine) $("#gameOverModal").modal("show");
    loadGrid(data);
  });
}
function loadGrid(gridData) {
  let gameContainer = $("#game-board");
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

  $(".cell").each(function () {
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

$(document).ready(function () {
  $.ajax({
    url: "/api/grid",
    type: "GET",
    success: (data) => {
      console.log(data);
      loadGrid(data);
    },
    error: (error) => {
      console.log(error);
    },
  });

  $("#undoButton").click(function () {
    $.get("/api/undo", function (data) {
      loadGrid(data);
    });
  });

  $("#playAgainButton").click(function () {
    $.get("/api/undo", function (data) {
        $("#gameOverModal").modal("hide");
      loadGrid(data);
    });
  });

  $("#redoButton").click(function () {
    $.get("/api/redo", function (data) {
      loadGrid(data);
    });
  });
});
