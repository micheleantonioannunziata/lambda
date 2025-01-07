function lambdaMethod(spanLambda) {
    spanLambda.classList.toggle("underline");
    const isHighlighted = spanLambda.classList.contains("underline");

    const cardPayment = [
        document.getElementById("intestatarioCarta"),
        document.getElementById("numeroCarta"),
        document.getElementById("CVV"),
        document.getElementById("scadenza")
    ];

    const lambdaPayment = document.getElementById("lambdaPayment"); // Identifica il container o l'elemento per il metodo lambda.

    if (isHighlighted) {
        // Disabilita i campi di pagamento con carta.
        cardPayment.forEach(cardInput => {
            cardInput.disabled = true;
            cardInput.value = "";
            cardInput.style.cursor = "not-allowed";
            cardInput.style.opacity = '0.5';
            spanLambda.querySelector("input").value = "true";
        });

        // Abilita il metodo lambda.
        lambdaPayment.disabled = false;
        lambdaPayment.style.cursor = "default";
    } else {
        // Abilita i campi di pagamento con carta.
        cardPayment.forEach(cardInput => {
            cardInput.disabled = false;
            cardInput.style.cursor = "default";
            cardInput.style.opacity = '1';
            spanLambda.querySelector("input").value = "false";
        });

        // Disabilita il metodo lambda.
        lambdaPayment.disabled = true;
        lambdaPayment.value = ""; // Azzera il valore se necessario.
        lambdaPayment.style.cursor = "not-allowed";
    }
}
