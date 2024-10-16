let body = document.getElementById("b1");

// Create first header
let header1 = document.createElement("h1")
header1.textContent="Section 1";
header1.style.color="#9f82a1";
body.appendChild(header1)

// Create first paragraph
let paragraph1 = document.createElement("p")
paragraph1.textContent="This is the first paragraph of the page";
body.appendChild(paragraph1)

// Bold
let boldText = document.createElement("b")
boldText.textContent="This text should be bold!";
body.appendChild(boldText)

// Line break
let lineBreak = document.createElement("br")
body.appendChild(lineBreak)

// Quotation
let quote = document.createElement("q")
quote.textContent="This is a quotation";
body.appendChild(quote)

// Break
body.appendChild(lineBreak)

// Strong text
let strong = document.createElement("strong")
strong.textContent="STRONG TEXT";
body.appendChild(strong)

// Break
body.appendChild(lineBreak)

// Dropdown
let dropDown = document.createElement("select");
dropDown.name = "Drop down list";
let option1 = document.createElement("option");
option1.value = "Hi :)";
option1.text = "Hi";
dropDown.appendChild(option1)
let option2 = document.createElement("option");
option2.value = "Bye :(";
option2.text = "Bye";
dropDown.appendChild(option2)

body.appendChild(dropDown)

// Break
body.appendChild(lineBreak)

// Image
let image = document.createElement("img");
image.src="cat.jpeg";
body.appendChild(image)
