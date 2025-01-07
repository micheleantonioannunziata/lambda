const upLoadImageBtn = document.getElementById('upLoadImageBtn');
const hiddenImageInput = document.getElementById('hiddenImageInput');
const imageNameDisplay = document.getElementById('imageNameDisplay');
const resetImageBtn = document.getElementById('resetImageBtn')

const defaultMessage = 'Nessun file selezionato'


upLoadImageBtn.onclick = () => hiddenImageInput.click();

hiddenImageInput.onchange = () => {
    imageNameDisplay.textContent = hiddenImageInput.files[0]?.name || "Nessun file sel"
    resetImageBtn.classList.remove('hidden')
}

resetImageBtn.onclick = () => {
    resetImageBtn.classList.add('hidden')
    hiddenImageInput.value = ""
    imageNameDisplay.textContent = defaultMessage
}