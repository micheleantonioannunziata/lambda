const upLoadImageBtn = document.getElementById('upLoadImageBtn');
const hiddenImageInput = document.getElementById('hiddenImageInput');
const imageNameDisplay = document.getElementById('imageNameDisplay');
const resetImageBtn = document.getElementById('resetImageBtn')

const defaultMessage = 'Nessun file selezionato'


upLoadImageBtn.onclick = () => hiddenImageInput.click();

hiddenImageInput.onchange = () => {
    imageNameDisplay.textContent = hiddenImageInput.files[0]?.name || defaultMessage
    resetImageBtn.classList.remove('hidden')
}

resetImageBtn.onclick = () => {
    resetImageBtn.classList.add('hidden')
    hiddenImageInput.value = ""
    imageNameDisplay.textContent = defaultMessage
}



function validateTradeInForm(){
    const ram = parseInt(document.getElementById("ram").value)
    const spazioArchiviazione = parseInt(document.getElementById("spazioArchiviazione").value)
    const colore = document.getElementById("colore").value
    const condizioneGenerale = document.getElementById("condizioneGenerale").value
    const batteria = document.getElementById("batteria").value

    let error = ""

    const ramValues = new Set([1, 2, 4, 6, 8, 12, 16, 24, 32, 64, 128]);
    const storageValues = new Set([4, 6, 8, 12, 16, 24, 32, 64, 128, 256, 512, 1024, 2048]);
    const coloreRegex = /^[A-Za-z ]{3,50}$/;
    const condizioneGeneraleValues = new Set(["Discreta", "Buona", "Ottima", "Eccellente"]);
    const batteriaRegex = /([1-9]?\d|100)/;

    if (hiddenImageInput.files.length === 0) error = "Il file dell'immagine non è stato caricato correttamente"
    if (!ramValues.has(ram)) error = "Il valore della ram inserito non è ammissibile!"
    if (!storageValues.has(spazioArchiviazione)) error = "Il valore dello spazio archiviazione inserito non è ammissibile!"
    if (!coloreRegex.test(colore)) error = "Il colore inserito non rispetta il formato richiesto!"
    if (!condizioneGeneraleValues.has(condizioneGenerale)) error = "Il valore della condizione generale inserito non è ammissibile!"
    if (!batteriaRegex.test(batteria)) error = "Il valore della batteria inserito non è ammissibile"



    if (error !== ""){
        if (document.getElementById("error") == null) {
            let errorDiv = document.createElement("div")
            errorDiv.id = "error"
            errorDiv.className = "smallText textCenter"
            errorDiv.innerText = error
            document.querySelector("form").appendChild(errorDiv)
        } else document.getElementById("error").innerText = error

        return false
    }

    return true
}