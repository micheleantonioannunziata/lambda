function validateTechnicalRequirementsForm(){
    let ram = document.getElementById("ram").value;
    let storage = document.getElementById("storage").value;
    let color = document.getElementById("color").value;

    let error = ""

    const ramValues = new Set([1, 2, 4, 6, 8, 12, 16, 24, 32, 64, 128]);
    const storageValues = new Set([4, 6, 8, 12, 16, 24, 32, 64, 128, 256, 512, 1024, 2048]);
    const coloreRegex = /^[A-Za-z ]{3,50}$/


    if (!ramValues.has(parseInt(ram))) error = "Il valore della ram inserito non è ammissibile!"
    if (!storageValues.has(parseInt(storage))) error = "Il valore dello spazio archiviazione inserito non è ammissibile!"
    if (!coloreRegex.test(color)) error = "Il colore inserito non rispetta il formato richiesto!"

    if (error !== ""){
        if (document.getElementById("error") == null) {
            let errorDiv = document.createElement("div")
            errorDiv.id = "error"
            errorDiv.className = "smallText textCenter"
            errorDiv.innerText = error
            const submitButton = document.querySelector("#techRequirements button[type=submit]");
            const form = submitButton.parentNode;
            form.insertBefore(errorDiv, submitButton)
        } else document.getElementById("error").innerText = error

        return false
    }

    return true

}