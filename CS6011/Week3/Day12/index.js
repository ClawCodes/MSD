let body = document.getElementById("b1");

let header = document.createElement("h1");
header.textContent = "Multiplication Table";
header.style.textAlign = "center";
body.appendChild(header);

let highlightedCell = null;

class Table {
    constructor() {
        this.table = this.generateTable();
        // this.highlightedCell = null;
    }

    hoverHighlight(event) {
        if (highlightedCell !== event.target) {
            event.target.style.backgroundColor = "#eade91";
        }
    }

    resetBackground(event) {
        if (highlightedCell !== event.target) {
            event.target.style.backgroundColor = "white";
        }
    }

    createCell(value, cellType) {
        let cell = document.createElement(cellType);
        cell.innerHTML = value;
        cell.style.width = "50px";
        cell.style.height = "50px";
        cell.style.borderStyle = "solid";

        cell.addEventListener("mouseover", this.hoverHighlight);
        cell.addEventListener("mouseout", this.resetBackground);

        return cell;
    }

    highlightClick(event) {
        const clickedCell = event.target;
        if (clickedCell.tagName === "TD" || clickedCell.tagName === "TH") {
            if (highlightedCell) {
                highlightedCell.style.backgroundColor = "white";
            }

            clickedCell.style.backgroundColor = "green";
            highlightedCell = clickedCell;
        }
    }

    generateTable() {
        let table = document.createElement("table");
        for (let i = 1; i < 11; i++) {
            let row = document.createElement("tr");
            for (let j = 1; j < 11; j++) {
                let cellType = "td";
                if (i === 1 || j === 1) {
                    cellType = "th";
                }
                row.appendChild(this.createCell(i * j, cellType));
            }
            table.appendChild(row);
        }
        table.style.textAlign = "center";
        // Center the table on the web page
        table.style.marginLeft = "auto";
        table.style.marginRight = "auto";

        table.addEventListener("click", this.highlightClick);

        return table;
    }
}

let table = new Table();
body.appendChild(table.table);

let colors = Array("blue", "purple", "red");
let intervalCount = 0;
function changeBackground() {
    if (intervalCount > 2) {
        intervalCount = 0;
    }
    body.style.backgroundColor = colors[intervalCount];
    intervalCount++;
}

let backgroundButton = document.createElement("button");
backgroundButton.textContent = "Go Crazy!";
backgroundButton.style.transform = "translateX(500%)"

backgroundButton.addEventListener("click", function () {
        window.setInterval(changeBackground, 100)
        crazyModeActive = true;
})

body.appendChild(backgroundButton);

