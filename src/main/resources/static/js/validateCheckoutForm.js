function validateCheckoutForm() {
    const destinatario = document.getElementById("destinatario").value;
    const indirizzo = document.getElementById("indirizzo").value;
    const lambdaPayment = document.getElementById("lambdaFlag");

    const intestatarioCarta = document.getElementById("intestatarioCarta").value;
    const numeroCarta = document.getElementById("numeroCarta").value;
    const cvv = document.getElementById("CVV").value;
    const dataScadenza = document.getElementById("scadenza").value;


    const destinatarioRegex = /^(?=.{5,50}$)([A-Za-z ]{2,} [A-Za-z]{2,})$/;
    const indirizzoRegex = /^(?=.{15,255}$)[A-Za-z' -]+ \d+, \d{5} [A-Za-z' -]{3,}$/;
    const intestatarioRegex = /(?=.{5,50}$)([A-Za-z ]{2,} [A-Za-z]{2,})$/;
    const numeroCartaRegex = /^\d{16}$/;
    const cvvRegex = /^\d{3}$/;
    const scadenzaRegex = /^(0[1-9]|1[0-2])\/(20[2-3][0-9])$/;

    let error = "";

    // Controlla sempre destinatario e indirizzo
    if (!destinatarioRegex.test(destinatario)) error = "Destinatario inserito non rispetta il formato richiesto!";
    if (!indirizzoRegex.test(indirizzo)) error = "Indirizzo inserito non rispetta il formato richiesto!";

    // Se lambdaPayment non ha la classe "underline", esci dai controlli relativi alla carta
    if (!lambdaPayment.classList.contains("underline")) {
        // Esegui controlli sulla carta
        if (!intestatarioRegex.test(intestatarioCarta)) error = "Intestatario carta inserito non rispetta il formato richiesto!";
        if (!numeroCartaRegex.test(numeroCarta)) error = "Numero carta inserito non rispetta il formato richiesto!";
        if (!cvvRegex.test(cvv)) error = "CVV inserito non rispetta il formato richiesto!";
        if (!scadenzaRegex.test(dataScadenza)) error = "Data di scadenza inserita non rispetta il formato richiesto!";
    }

    // Gestione degli errori
    if (error !== "") {
        if (document.getElementById("error") == null) {
            let errorDiv = document.createElement("div");
            errorDiv.id = "error";
            errorDiv.className = "smallText textCenter";
            errorDiv.innerText = error;
            const submitButton = document.querySelector("form button[type=submit]");
            const form = submitButton.parentNode;

            form.insertBefore(errorDiv, submitButton)
        } else {
            document.getElementById("error").innerText = error;
        }

        return false;
    }

    return true;
}
