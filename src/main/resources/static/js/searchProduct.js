document.getElementById("searchProduct").addEventListener("keydown", function(event){
    if(event.key === "Enter"){
        document.getElementById("searchForm").submit();
    }
})

function submitForm(div){
    div.querySelector("form").submit();
}