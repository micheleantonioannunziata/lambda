document.getElementById("searchProductByCategory").onkeydown = (event) =>{
    if (event.key === "Enter") document.getElementById("searchProductByCategory").submit();
}


const cards = document.querySelectorAll(".cardContainer > .card")

cards.forEach(card =>{
    card.onclick = () =>{
        card.querySelector("form").submit();
    }
})