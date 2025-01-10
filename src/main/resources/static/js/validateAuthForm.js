function validateLoginForm(){
    const email = document.getElementById("emailLogin").value
    const password = document.getElementById("passwordLogin").value

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,50}$/
    const emailRegex = /^[\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,8}$/

    let message = ""

    if (!emailRegex.test(email))
        message = "La mail inserita non rispetta il formato richiesto!"
    else if (!passwordRegex.test(password))
        message = "La password inserita non rispetta il formato richiesto!"

    if (message !== "") {
        if (document.getElementById("messageDiv") == null) {
            let messageDiv = document.createElement("div")
            messageDiv.id = "messageDiv"
            messageDiv.className = "smallText textCenter"
            messageDiv.innerText = message
            document.querySelector("#logInForm > form").appendChild(messageDiv)

            return false
        } else {
            document.getElementById("messageDiv").innerText = message

            return false
        }
    }

    return true
}

function validateUserSignUpForm() {
    const nome = document.getElementById("userName").value
    const cognome = document.getElementById("userSurname").value
    const email = document.getElementById("userEmail").value
    const password = document.getElementById("userPassword").value
    const confirmPassword = document.getElementById("userConfirmPassword").value

    let message = ""

    const nomeRegex = /^[A-Za-z]{2,}[A-Za-z ]{0,48}$/
    const cognomeRegex = /^[A-Za-z]{2,}([A-Za-z' ]{0,48}[A-Za-z])?$/
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,50}$/
    const emailRegex = /^[\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,8}$/

    if (!nomeRegex.test(nome)) message = "Il nome inserito non rispetta il formato richiesto!"
    else if (!cognomeRegex.test(cognome)) message = "Il cognome inserito non rispetta il formato richiesto!"
    else if (!emailRegex.test(email)) message = "La mail inserita non rispetta il formato richiesto!"
    else if (!passwordRegex.test(password)) message = "La password inserita non rispetta il formato richiesto!"
    else if (confirmPassword !== password) message = "Conferma password e password non coincidono!"


    if (message !== "") {
        if (document.getElementById("messageDiv") == null) {
            let messageDiv = document.createElement("div")
            messageDiv.id = "messageDiv"
            messageDiv.className = "smallText textCenter"
            messageDiv.innerText = message
            document.querySelector("#signUpForm > form").appendChild(messageDiv)
        } else
            document.getElementById("messageDiv").innerText = message

        return false
    }

    return true
}

function validateVendorSignUp() {
    const ragioneSociale = document.getElementById("ragioneSocialeSignUp").value
    const partitaIva = document.getElementById("partitaIvaSignUp").value
    const email = document.getElementById("emailSignUp").value
    const password = document.getElementById("passwordSignUp").value
    const confirmPassword = document.getElementById("confirmPasswordSignUp").value

    let message = ""

    const ragioneSocialeRegex = /^[A-Za-z0-9ÀÈÉÌÒÓÙàèéìòóùç&',.\s-]{2,100}$/
    const partitaIvaRegex = /^\d{11}$/
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,50}$/
    const emailRegex = /^[\w.%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,8}$/

    if (!ragioneSocialeRegex.test(ragioneSociale)) message = "La ragione sociale inserita non rispetta il formato richiesto!"
    else if (!partitaIvaRegex.test(partitaIva)) message = "La partita iva inserita non rispetta il formato richiesto!"
    else if (!emailRegex.test(email)) message = "La mail inserita non rispetta il formato richiesto!"
    else if (!passwordRegex.test(password)) message = "La password inserita non rispetta il formato richiesto!"
    else if (confirmPassword !== password) message = "Conferma password e password non coincidono!"


    if (message !== "") {
        if (document.getElementById("messageDiv") == null) {
            let messageDiv = document.createElement("div")
            messageDiv.id = "messageDiv"
            messageDiv.className = "smallText textCenter"
            messageDiv.innerText = message
            document.querySelector("#signUpForm > form").appendChild(messageDiv)
        } else
            document.getElementById("messageDiv").innerText = message

        return false
    }

    return true
}