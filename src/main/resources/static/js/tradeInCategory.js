const cardContainer = document.querySelector(".cardContainer");

const cards = cardContainer.querySelectorAll(".card");

cards.forEach(card =>{
    card.onclick = () =>{
        card.querySelector("form").submit()
    }
})