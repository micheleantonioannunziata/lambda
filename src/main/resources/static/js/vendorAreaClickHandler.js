// mapping button area
const areas = [
    { button: "insertionAreaBtn", area: "insertionArea" },
    { button: "dataAreaBtn", area: "dataArea" },
    { button: "statsAreaBtn", area: "statsArea" },
];

// mostra un'area e nasconde le altre
function showArea (activeBtn) {
    areas.forEach((item) => {
        const button = document.getElementById(item.button);
        const area = document.getElementById(item.area);

        if (button === activeBtn) {
            button.classList.add("violet");
            area.classList.remove("hidden");
        } else {
            button.classList.remove("violet");
            area.classList.add("hidden");
        }
    });
}

// click handler per ogni button
areas.forEach((item) => {
    const button = document.getElementById(item.button);
    button.onclick = () => showArea(button);
});