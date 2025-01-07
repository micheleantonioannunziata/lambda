const cards = document.querySelectorAll(".cardContainer > .card");

cards.forEach(card =>{
    card.onclick = function (){
        let form = card.querySelector("form");
        form.submit();
    }
})