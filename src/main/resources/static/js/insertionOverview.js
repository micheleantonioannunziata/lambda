// Selettori principali
const colorChoose = document.querySelector('#colorChoose > .chooses');
const memoryChoose = document.querySelector('#ramChoose > .chooses');
const storageChoose = document.querySelector('#storageChoose > .chooses');
const colorChooseBtns = colorChoose.querySelectorAll('button');
const memoryChooseBtns = memoryChoose.querySelectorAll('button');
const storageChooseBtns = storageChoose.querySelectorAll('button');
const addToCartBtn = document.getElementById('addToCartBtn');
const price = document.getElementById('price');
const otherVendorBtn = document.getElementById('otherVendorBtn');
const otherVendorSection = document.getElementById('otherVendorSection');
const overlay = document.getElementById('overlay');
const otherVendorCards = otherVendorSection.querySelectorAll('.cardContainer > .card');
const addToCartForm = document.getElementById("addToCartForm");
const pIvaInput = addToCartForm.querySelector("#pIvaCart");
const idSuperInput = addToCartForm.querySelector("#idSuperCart");
const ramInput = addToCartForm.querySelector("#ramCart");
const storageInput = addToCartForm.querySelector("#storageCart");
const colorInput = addToCartForm.querySelector("#colorCart");



// Mostra il pulsante "Aggiungi al carrello"
function showAddToCartBtn() {
    addToCartBtn.classList.add('active', 'clickable');
    addToCartBtn.classList.remove('notClickable');
    addToCartBtn.disabled = false;
}

// Nasconde il pulsante "Aggiungi al carrello"
function hideAddToCartBtn() {
    addToCartBtn.classList.remove('active', 'clickable');
    addToCartBtn.classList.add('notClickable');
    addToCartBtn.disabled = true;
}

// Aggiorna lo stato attivo dei pulsanti
function updateActive(btns, btnToActivate) {
    if (btnToActivate.classList.contains('active')) {
        btnToActivate.classList.remove('active');
        hideAddToCartBtn();
        return;
    }

    btns.forEach(btn => btn.classList.remove('active'));
    btnToActivate.classList.add('active');
    checkAddToCartVisibility();
}

// Controlla se tutte le scelte sono attive e abilita/disabilita il pulsante "Aggiungi al carrello"
function checkAddToCartVisibility() {
    const isColorActive = Array.from(colorChooseBtns).some(btn => btn.classList.contains('active'));
    const isMemoryActive = Array.from(memoryChooseBtns).some(btn => btn.classList.contains('active'));
    const isStorageActive = Array.from(storageChooseBtns).some(btn => btn.classList.contains('active'));

    if (isColorActive && isMemoryActive && isStorageActive) {
        const activeColor = Array.from(colorChooseBtns).find(btn => btn.classList.contains('active')).querySelector('span').innerText;
        const activeMemory = Array.from(memoryChooseBtns).find(btn => btn.classList.contains('active')).querySelector('span').innerText;
        const activeStorage = Array.from(storageChooseBtns).find(btn => btn.classList.contains('active')).querySelector('span').innerText;

        verifyCombination(activeColor, activeMemory.replace('GB', ''), activeStorage.replace('GB', ''));
    } else {
        hideAddToCartBtn();
    }
}

// Verifica la combinazione selezionata tramite una chiamata fetch
function verifyCombination(color, memory, storage) {
    const urlSearchParam = new URLSearchParams();
    urlSearchParam.append('firstType', 'ram');
    urlSearchParam.append('firstOption', memory);
    urlSearchParam.append('secondType', 'storage');
    urlSearchParam.append('secondOption', storage);
    urlSearchParam.append('thirdType', 'color');
    urlSearchParam.append('thirdOption', color);

    const idFormHelper = document.getElementById('idHelper');
    urlSearchParam.append('idSuperProdotto', idFormHelper.querySelector('input[name="idSuperProdotto"]').value);
    urlSearchParam.append('partitaIvaRivenditore', idFormHelper.querySelector('input[name="partitaIvaRivenditore"]').value);

    fetch('searchCombinations?' + urlSearchParam.toString())
        .then(response => {
            if (!response.ok) throw new Error();
            return response.text();
        })
        .then(responseText => {
            const resp = JSON.parse(responseText);
            if (resp.length > 0) {
                price.innerText = '€ ' + resp[0].prezzo.toFixed(2);
                pIvaInput.value = idFormHelper.querySelector('input[name="partitaIvaRivenditore"]').value;
                idSuperInput.value = idFormHelper.querySelector('input[name="idSuperProdotto"]').value;
                ramInput.value = memory;
                storageInput.value = storage;
                colorInput.value = color;
                showAddToCartBtn();
            } else {
                price.innerText = '';
                addToCartForm.querySelectorAll("input").forEach(input => {
                    input.value = "";
                });
                hideAddToCartBtn();
            }
        })
        .catch(() => {
            price.innerText = '';
            hideAddToCartBtn();
        });
}

// Eventi per i pulsanti di selezione
colorChooseBtns.forEach(colorChooseBtn => {
    colorChooseBtn.onclick = () => updateActive(colorChooseBtns, colorChooseBtn);
});

memoryChooseBtns.forEach(memoryChooseBtn => {
    memoryChooseBtn.onclick = () => updateActive(memoryChooseBtns, memoryChooseBtn);
});

storageChooseBtns.forEach(storageChooseBtn => {
    storageChooseBtn.onclick = () => updateActive(storageChooseBtns, storageChooseBtn);
});

// Eventi per la sezione "Altri rivenditori"
otherVendorBtn.onclick = () => {
    otherVendorSection.classList.add('active');
    overlay.classList.add('active');
};

overlay.onclick = () => {
    overlay.classList.remove('active');
    otherVendorSection.classList.remove('active');
};

otherVendorCards.forEach(card => {
    card.onclick = () => card.querySelector('form').submit();
});

// Inizializza la pagina al caricamento
window.onload = checkAddToCartVisibility;
