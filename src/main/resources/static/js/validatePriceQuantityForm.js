function validatePriceQuantityForm(){
    const quantita = document.getElementById("quantita").value
    const prezzo = document.getElementById("prezzo").value
    const scontoBase = document.getElementById("scontoBase").value
    const scontoPremium = document.getElementById("scontoPremium").value

    const quantityRegex = /[1-9][0-9]{0,2}/
    const prezzoRegex = /^[1-9]\d{0,3}(\.\d{1,2})?$/
    const scontoBaseRegex = /([1-9]?)\d/
    const scontoPremiumRegex = /([1-9]?)\d/

    let error = "";

    if (!quantityRegex.test(quantita)) error = "Il valore della quantità inserito non è ammissibile!";
    if (!prezzoRegex.test(prezzo) || parseFloat(prezzo) <= 0) error = "Il valore del prezzo base inserito non è ammissibile!"
    if (!scontoBaseRegex.test(scontoBase) || (parseInt(scontoBase) < 0 || parseInt(scontoBase) > 80)) error = "Il valore dello sconto base inserito non è ammissibile!"
    if (!scontoPremiumRegex.test(scontoPremium) || (parseInt(scontoPremium) < 0 || parseInt(scontoPremium) > 80) || (parseInt(scontoPremium) <= parseInt(scontoBase)))
        error = "Il valore dello sconto premium inserito non è ammissibile!"

    if (error !== ""){
        if (document.getElementById("error") == null) {
            let errorDiv = document.createElement("div")
            errorDiv.id = "error"
            errorDiv.className = "smallText textCenter"
            errorDiv.innerText = error
            const submitButton = document.querySelector("form button[type=submit]");
            const form = submitButton.parentNode;

            form.insertBefore(errorDiv, submitButton)
        } else document.getElementById("error").innerText = error

        return false
    }

    return true
}