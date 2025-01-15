# Lambda

Lambda è una piattaforma web progettata per rivoluzionare il commercio di dispositivi tecnologici. 

L'obiettivo principale è semplificare la navigazione e l'acquisto di prodotti elettronici agli acquirenti finali, 
offrendo al contempo ai rivenditori uno spazio unico e centralizzato per gestire le loro inserzioni.
La piattaforma mira a rendere l'esperienza di acquisto semplice, immediata e personalizzata.

## Autori
### Il Lambda Team

- Michele Antonio Annunziata - [micheleantonioannunziata](https://github.com/micheleantonioannunziata)
- Raffaele Coppola - [Raff2812](https://github.com/Raff2812)
- Gianfranco Vitiello - [gianf03](https://github.com/gianf03)
- Luigi Auriemma - [LuigiAuriemma](https://github.com/LuigiAuriemma)

## Documentazione

**Benvenuti nella sezione dedicata alla documentazione ufficiale di Lambda.** 

Link alla documentazione ufficiale e alla javadoc: [documentazione ufficiale e javadoc](https://micheleantonioannunziata.github.io/lambda/)

## Installazione

### Prerequisiti

Per eseguire il progetto, assicurarsi di avere installato i seguenti strumenti:
- **IntelliJ IDEA** 2024.3.1
- **MySQL Workbench** 8.0 CE

### Step by step

1. **Scaricare il progetto**:  
   Scaricare il file `.zip` del progetto dalla repository GitHub e aprirlo tramite IntelliJ IDEA.

2. **Configurazione del Database**:
    - Aprire **MySQL Workbench** e creare una nuova connessione al database.

   Esempio di configurazione della connessione:
    ```plaintext
    hostname = localhost
    port = 3306
    username = mioUsername
    password = miaPassword
    ```

   Per stabilire la connessione anche su **Intellij IDEA** è possibile seguire la serie di passi riportati alla seguente pagina: 
   [https://www.jetbrains.com/help/idea/connecting-to-a-database.html](https://www.jetbrains.com/help/idea/connecting-to-a-database.html).

3. **Configurazione `application.properties`**:  
   Configurare il file `application.properties` per stabilire la connessione al database.

    Esempio di configurazione:
    ```properties
    spring.datasource.url = jdbc:mysql://localhost:3306/lambda
    spring.datasource.username = mioUsername
    spring.datasource.password = miaPassword
    spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto = create
    spring.jpa.show-sql = true
    ```
   
   Per approfondire i passi della configurazione è possibile visitare la documentazione ufficiale 
   riportata al seguente link: [https://docs.spring.io/spring-boot/appendix/application-properties/index.html](https://docs.spring.io/spring-boot/appendix/application-properties/index.html) 
   
4. **Creazione dello schema**:
   Dopo aver configurato la connessione, creare un nuovo `schema` nel database.

    Esempio di schema:
   ```schema
   nome = lambda
    ```
    Per la creazione dello schema direttamente da **Intellij IDEA** è possibile seguire la serie di passi riportati alla seguente pagina: [https://www.jetbrains.com/help/idea/schemas.html](https://www.jetbrains.com/help/idea/schemas.html)    

5. **Avvio del progetto**:
   Tornare su IntelliJ IDEA, selezionare lo schema creato, e avviare il progetto.

## Utilizzo di Lambda

### Inizializzazione del catalogo

Lambda utilizza un meccanismo di inizializzazione del catalogo basato su un file JSON. 
Questo processo viene gestito automaticamente all'avvio dell'applicazione grazie alla classe `DataLoader`, 
annotata con `@PostConstruct`. 
La classe `DataLoader` legge il file `dataLoader.json` presente nella cartella `src/main/resources/json` e popola le tabelle del database con i dati forniti.

### Formato del File JSON

Il file JSON deve avere il seguente formato:

```json
{
  "insert": "y",
  "categories": [
    {
      "name": "nomeCategoria",
      "immagine": "immagineURLCategoria"
    }
  ],
  "superProducts": [
    {
      "marca": "marcaSuperProdotto",
      "modello": "modelloSuperProdotto",
      "immagine": "immagineURLSuperProdotto",
      "categoria": "nomeCategoria"
    }
  ],
  "products": [
    {
      "superProdottoModello": "modelloSuperProdotto",
      "ram": 8,
      "spazioArchiviazione": 128,
      "colore": "coloreProdotto"
    }
  ],
  "insertions": [
    {
      "partitaIva": "partitaIVArivenditore",
      "prodotto": {
        "superProdottoModello": "modelloSuperProdotto",
        "ram": 8,
        "spazioArchiviazione": 128,
        "colore": "coloreProdotto"
      },
      "prezzo": 1200,
      "scontoStandard": 0,
      "scontoPremium": 10,
      "quantità": 90
    }
  ],
  "vendors": [
    {
      "partitaIva": "partitaIVARivenditore",
      "ragioneSociale": "ragioneSocialeRivenditore",
      "mail": "mailRivenditore",
      "password": "passwordRivenditore"
    }
  ]
}
```

#### Dettagli dei campi

- **insert**: Campo **obbligatorio per attivare l'inserimento dei dati**. Se il valore è **"y"**, i dati verranno inseriti nel database. Qualsiasi altro valore impedirà l'inserimento.
- **categories**: Elenco delle categorie dei prodotti, con il nome e un'immagine associata. 
Per le immagini, è possibile posizionare un file nella cartella `categories` presente in `src/main/resources/static/images` e utilizzare l'URL `/images/categories/immagine.png`, oppure fornire direttamente un link URL esterno.
- **superProducts**: Descrizione dettagliata dei prodotti principali, inclusi marca, modello, immagine e categoria.
Per le immagini, è possibile posizionare un file nella cartella `products` presente in `src/main/resources/static/images` e utilizzare l'URL `/images/products/immagine.png`, oppure fornire direttamente un link URL esterno.
- **products**: Specifiche dettagliate dei prodotti, come modello, RAM, spazio di archiviazione e colore.
- **insertions**: Informazioni sulle inserzioni dei prodotti, incluse le caratteristiche del prodotto, prezzo, sconti e quantità disponibile.
- **vendors**: Informazioni sui rivenditori, come partita IVA, ragione sociale, email e password.

### Procedura di inizializzazione

All'avvio dell'applicazione, la classe `DataLoader` verifica il valore del campo insert nel file JSON.

Se il valore è "y", i dati forniti nelle sezioni *categories*, *superProducts*, *products*, *insertions* e *vendors* verranno inseriti nelle rispettive tabelle del database.
Se il valore è diverso da "y", non verrà eseguito alcun inserimento.

#### Nota Importante

Gli utenti possono modificare il file JSON per personalizzare il catalogo iniziale. 
Tuttavia, è fondamentale sapere che:

- Il file JSON deve essere correttamente formattato.
- Qualsiasi modifica al file JSON è a totale responsabilità dell'utente. (Bisogna fare attenzione a non violare i *vincoli di integrità referenziale*)



## Built With

- **Java** - Linguaggio di programmazione per lo sviluppo back-end.
- **Spring Framework** - Framework Java utilizzato per lo sviluppo del back-end (Spring MVC/Web).
- **Maven** - Strumento di gestione delle dipendenze.
- **HTML5** - Linguaggio di markup Utilizzato per lo sviluppo front-end.
- **CSS** - Fogli di stile per la progettazione del front-end.
- **JavaScript** - Linguaggio di scripting per la manipolazione del DOM.
- **Thymeleaf** - Template engine Java per la generazione di pagine dinamiche.
