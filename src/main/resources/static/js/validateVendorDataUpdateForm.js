function validateVendorDataForm(){
    const ragioneSociale = document.getElementById("ragioneSociale").value
    const indirizzo = document.getElementById("indirizzo").value
    const passwordAttuale = document.getElementById("passwordAttuale").value
    const nuovaPassword = document.getElementById("nuovaPassword").value
    const confermaPassword = document.getElementById("confermaNuovaPassword").value

    const ragioneSocialeRegex = /^[A-Za-z0-9ÀÈÉÌÒÓÙàèéìòóùç&',.\s-]{2,100}$/
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,50}$/

    const indirizzoRegex = /^(?=.{15,255}$)[A-Za-z' -]+ \d+, \d{5} [A-Za-z' -]{3,}$/


    let error = ""

    if (passwordAttuale === "")
        error = "Inserire la password attuale per modificare i dati!"
    else if (!passwordRegex.test(passwordAttuale))
        error = "La password attuale inserita non rispetta il formato richiesto!"
    else if(!ragioneSocialeRegex.test(ragioneSociale))
        error = "La ragione sociale inserita non rispetta il formato richiesto!"
    else if (indirizzo !== "" && !indirizzoRegex.test(indirizzo))
        error = "Indirizzo inserito non rispetta il formato richiesto!"
    else if ((nuovaPassword === "" && confermaPassword !== "") || (nuovaPassword !== "" && confermaPassword === ""))
        error = "I campi relativi alle password non sono stati compilati nella loro interezza!"
    else if (nuovaPassword !== "" && !passwordRegex.test(nuovaPassword))
        error = "La nuova password inserita non rispetta il formato richiesto!"
    else if (confermaPassword !== "" && nuovaPassword !== confermaPassword)
        error = "Le password inserite non coincidono!"


    if (error !== "") {
        if (document.getElementById("error") == null) {
            let errorDiv = document.createElement("div")
            errorDiv.id = "error"
            errorDiv.classnome = "smallText textCenter"
            errorDiv.innerText = error


            const submitButton = document.querySelector("form button[type=submit]");
            const form = submitButton.parentNode;

            form.insertBefore(errorDiv, submitButton)
            return false
        } else {
            document.getElementById("error").innerText = error

            return false
        }
    }

    return true
}