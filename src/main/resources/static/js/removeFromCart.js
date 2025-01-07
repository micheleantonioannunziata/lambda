function removeFromCart(button){
    let idInserzione = button.getAttribute("data-insertion");
    let form = document.createElement("form");
    form.action = "removeFromCart";
    form.method = "POST";

    let input = document.createElement("input");
    input.type = "hidden";
    input.value = idInserzione;
    input.name = "idInserzione";

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

/*function removeFromCart(button){
    let idInserzione = button.getAttribute("data-insertion")
    //console.log(idInserzione);
    let urlSearchParam = new URLSearchParams();
    urlSearchParam.append("idInserzione", idInserzione);
    fetch("removeFromCart?"+urlSearchParam.toString())
        .then(response=>{
            if (!response.ok)
                throw new Error("Risposta non conforme");
            return response.text();
        })
        .then(responseText=>{
            updateCart(responseText)
        })
}*/

//da rivedere
/*function updateCart(response) {
    if (response !== "[]") {
        let cardContainer = document.querySelector(".cardContainer");
        let cart = JSON.parse(response); // Converte la risposta JSON in un oggetto JavaScript
        console.log(cart);
        cardContainer.innerHTML = ""; // Cancella tutto il contenuto esistente

        cart.forEach((cartItem, index) => {
            // Crea il contenitore principale per ogni elemento del carrello
            let card = document.createElement("div");
            card.className = "card shadow";

            // Aggiungi l'immagine del prodotto
            let img = document.createElement("img");
            img.src = cartItem[`itemImage${index}`];
            img.alt = "Immagine prodotto";
            card.appendChild(img);

            // Contenitore infoBlock
            let infoBlock = document.createElement("div");
            infoBlock.className = "infoBlock";

            // Modello del prodotto
            let modelSpan = document.createElement("span");
            modelSpan.className = "normalText";
            modelSpan.textContent = cartItem[`itemModel${index}`];
            infoBlock.appendChild(modelSpan);

            // Rating (aggiungi stelle fisse come nell'HTML originale)
            let ratingDiv = document.createElement("div");
            ratingDiv.className = "rating mt10";
            for (let i = 0; i < 5; i++) {
                let starImg = document.createElement("img");
                starImg.src = "/images/fullStar.svg";
                starImg.alt = "Star";
                ratingDiv.appendChild(starImg);
            }
            infoBlock.appendChild(ratingDiv);

            // Informazioni del rivenditore
            let vendorDiv = document.createElement("div");
            vendorDiv.className = "italic smallText mt10";
            vendorDiv.textContent = `di ${cartItem[`itemVendor${index}`]}`;
            infoBlock.appendChild(vendorDiv);

            // Bottone di rimozione
            let removeButton = document.createElement("button");
            removeButton.className = "violetBtn topRight smallText";
            removeButton.setAttribute("data-insertion", cartItem[`itemInsertonId${index}`]);
            removeButton.textContent = "Rimuovi";
            removeButton.onclick = function () {
                removeFromCart(this);
            };
            infoBlock.appendChild(removeButton);

            // Prezzo
            let priceSpan = document.createElement("span");
            priceSpan.className = "midText bottomRight mt10";
            priceSpan.textContent = `€ ${cartItem[`itemStandardPrice${index}`]}`;
            infoBlock.appendChild(priceSpan);

            // Aggiungi infoBlock alla card
            card.appendChild(infoBlock);

            // Aggiungi la card al contenitore principale
            cardContainer.appendChild(card);
        })
    }
}*/
