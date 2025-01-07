function changeOptions(select, firstType){
    let firstOption = select.value;

    let urlSearchParam = new URLSearchParams();

    let idSuperProdotto = select.getAttribute("data-id");

    urlSearchParam.append("idSuperProdotto", idSuperProdotto);


    urlSearchParam.append("firstType", firstType);
    urlSearchParam.append("firstOption", firstOption);

    let secondSelect, thirdSelect;
    let secondType, thirdType;

    if(firstType === "ram"){
        secondSelect = document.getElementById("storage");
        thirdSelect = document.getElementById("color");
        secondType = "storage";
        thirdType = "color";
    }else if(firstType === "storage"){
        secondSelect = document.getElementById("ram");
        thirdSelect = document.getElementById("color");
        secondType = "ram";
        thirdType = "color";
    } else if(firstType === "color"){
        secondSelect = document.getElementById("ram");
        thirdSelect = document.getElementById("storage");
        secondType = "ram";
        thirdType = "storage";
    }

    let secondOption = secondSelect.value;
    let thirdOption = thirdSelect.value;

    console.log(firstOption + " " + secondOption + " " + thirdOption);


    urlSearchParam.append("secondType", secondType);
    urlSearchParam.append("secondOption", secondOption);

    urlSearchParam.append("thirdType", thirdType);
    urlSearchParam.append("thirdOption", thirdOption);


    fetch("/searchCombinations?" + urlSearchParam.toString())
        .then(response =>{
            if(!response.ok)
                throw new Error();

            return response.text();
        })
        .then(responseText =>{
            updateSelectView(responseText);
        })
}

function updateSelectView(response) {
    let products = JSON.parse(response);

    // Mantieni i valori selezionati
    let selectedRam = document.getElementById("ram").value;
    let selectedStorage = document.getElementById("storage").value;
    let selectedColor = document.getElementById("color").value;

    console.log(selectedRam, selectedStorage, selectedColor);

    // Aggiorna la select della RAM
    let ramSelect = document.getElementById("ram");
    ramSelect.innerHTML = "";
    let optionDefaultRam = document.createElement("option");
    optionDefaultRam.value = ""; // Opzione vuota per deselezionare
    optionDefaultRam.innerText = "RAM";
    ramSelect.append(optionDefaultRam);

    // Aggiorna la select dello Storage
    let storageSelect = document.getElementById("storage");
    storageSelect.innerHTML = "";
    let optionDefaultStorage = document.createElement("option");
    optionDefaultStorage.value = ""; // Opzione vuota per deselezionare
    optionDefaultStorage.innerText = "Spazio di archiviazione";
    storageSelect.append(optionDefaultStorage);

    // Aggiorna la select del Colore
    let colorSelect = document.getElementById("color");
    colorSelect.innerHTML = "";
    let optionDefaultColor = document.createElement("option");
    optionDefaultColor.value = ""; // Opzione vuota per deselezionare
    optionDefaultColor.innerText = "Colore";
    colorSelect.append(optionDefaultColor);

    let ramValues = new Set();
    let storageValues = new Set();
    let colorValues = new Set();

    let index = 0;
    products.forEach(product => {
        const ramKey = `ram${index}`;
        const storageKey = `storage${index}`;
        const colorKey = `color${index}`;

        const ram = product[ramKey];
        const storage = product[storageKey];
        const color = product[colorKey];

        // Aggiungi RAM
        if (!ramValues.has(ram)) {
            let ramOption = document.createElement("option");
            ramOption.innerText = `${ram}GB`;
            ramOption.value = `${ram}`;
            ramSelect.append(ramOption);
            ramValues.add(ram);
        }

        // Aggiungi Storage
        if (!storageValues.has(storage)) {
            let storageOption = document.createElement("option");
            storageOption.innerText = `${storage}GB`;
            storageOption.value = `${storage}`;
            storageSelect.append(storageOption);
            storageValues.add(storage);
        }

        // Aggiungi Colore
        if (!colorValues.has(color)) {
            let colorOption = document.createElement("option");
            colorOption.innerText = `${color}`;
            colorOption.value = `${color}`;
            colorSelect.append(colorOption);
            colorValues.add(color);
        }

        index++;
    });

    if(selectedRam !== "") {
        ramSelect.value = selectedRam;
    } else ramSelect.value = "";

    if(selectedStorage !== "") storageSelect.value = selectedStorage;
    else selectedStorage.value = "";

    if(selectedColor !== "") colorSelect.value = selectedColor;
    else selectedColor.value = "";
}