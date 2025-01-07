const orderRows = document.querySelectorAll('.orderRow')

orderRows.forEach(orderRow => {
    orderRow.onclick = () => {
        const cardContainer = orderRow.querySelector('.cardContainer')
        const imgRow = orderRow.querySelector('img')


        if (cardContainer) {
            const isHidden = cardContainer.classList.toggle('hidden')
            orderRow.classList.toggle('shadow', isHidden)

            imgRow.src = isHidden ? "/images/down.svg" : "/images/up.svg"
        }
    }
})