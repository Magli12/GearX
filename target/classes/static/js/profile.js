document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/user/profile')
        .then(response => {
            if (!response.ok) throw new Error("Errore nel recupero dei dati utente");
            return response.json();
        })
        .then(user => {
            document.getElementById('name').textContent = user.nome || 'N/A';
            document.getElementById('cognome').textContent = user.cognome || 'N/A';
            document.getElementById('birthdate').textContent = formatDate(user.dataDiNascita) || 'N/A';
            document.getElementById('username').textContent = user.username || 'N/A';
            document.getElementById('email').textContent = user.email || 'N/A';
        })
        .catch(error => {
            console.error("Errore nel caricamento dei dati utente:", error);
            alert("Impossibile caricare i dati del profilo.");
        });

    // Funzione per formattare la data nello stile "13 Gennaio 1945"
    function formatDate(dateString) {
        if (!dateString) return null;

        const mesi = [
            "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        ];
        const date = new Date(dateString);
        const giorno = date.getDate();
        const mese = mesi[date.getMonth()];
        const anno = date.getFullYear();

        return `${giorno} ${mese} ${anno}`;
    }
});