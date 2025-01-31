window.onload = () => {
    fetch('/api/posts/generateXml', { method: 'GET' })
        .then(response => response.text())
        .then(data => console.log(data))
        .catch(error => console.error('Errore nella generazione del file XML:', error));
};

// Funzione per aprire la modale del nuovo post
function openNewPostModal() {
    const modal = document.getElementById('newPostModal');
    if (modal) {
        modal.style.display = 'block';
    }
}

// Funzione per chiudere la modale del nuovo post
function closeNewPostModal() {
    const modal = document.getElementById('newPostModal');
    const newPostButton = document.querySelector('.button');
    if (modal) {
        modal.style.display = 'none';
        newPostButton.disabled = false;
    } else {
        console.error('Modale del nuovo post non trovata!');
    }
}

// Carica i primi 3 post
document.addEventListener('DOMContentLoaded', () => {
    loadPosts('../xml/generatedPosts.xml', true);
});

// Seleziona il form e il messaggio
const postForm = document.getElementById('postForm');
const message = document.getElementById('message');

postForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    // Disattivazione del pulsante per evitare invii multipli
    const newPostButton = document.querySelector('.button');
    newPostButton.disabled = true;

    const postContent = document.getElementById('post-content').value;

    let userId = 1; // Default se il recupero fallisce

    try {
        // Recupera l'ID utente loggato
        const userResponse = await fetch('/api/user/me');
        if (userResponse.ok) {
            userId = await userResponse.json(); // Assumiamo che ritorni un numero
            console.log('ID utente recuperato:', userId);
        } else {
            console.warn('Impossibile recuperare l\'ID utente. Utilizzo ID predefinito (1).');
        }
    } catch (error) {
        console.error('Errore durante il recupero dell\'ID utente:', error);
    }

    // Verifica l'ID prima di inviare la richiesta
    console.log('Invio richiesta con userId:', userId);

    try {
        // Invia il post al server usando Fetch API
        const response = await fetch(`/api/posts?userId=${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ content: postContent }),
        });
        document.getElementById('post-content').value = '';
        if (response.ok) {
            // Mostra messaggio di successo e reindirizza
            message.textContent = 'Post pubblicato con successo!';
            message.className = 'success';

            setTimeout(() => {
                window.location.href = 'home.html';
            }, 2000);
        } else {
            const errorText = await response.text();
            throw new Error(errorText || 'Errore durante la pubblicazione del post.');
        }
    } catch (error) {
        message.textContent = error.message || 'Si è verificato un errore sconosciuto.';
        message.className = 'error';
        // Svuota la textarea anche in caso di errore
        document.getElementById('post-content').value = '';
    } finally {
        newPostButton.disabled = false;
    }
});