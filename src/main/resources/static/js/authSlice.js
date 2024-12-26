const authContainer = document.querySelector('.authContainer')
const activeButton = document.getElementById('activeButton')
const removeButton = document.getElementById('removeButton')

activeButton.onclick = () => authContainer.classList.add('active')
removeButton.onclick = () => authContainer.classList.remove('active')