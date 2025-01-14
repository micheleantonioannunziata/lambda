window.onload = function (){
    const errorContainer = document.getElementById("errorContainer");

    if (!errorContainer) return;

    const errorType = errorContainer.getAttribute("data-error-type");
    const errorMsg = errorContainer.getAttribute("data-error-msg");

    switch (errorType) {
        case 'insufficientPoints':
            document.getElementById('btnCancelOrder').style.display = 'inline-block';
            document.getElementById('btnModifyPayment').style.display = 'inline-block';
            break;
        default:
            document.getElementById('btnGoBack').style.display = 'inline-block';
    }
};