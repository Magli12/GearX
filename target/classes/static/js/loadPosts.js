let lastId = 0; // ID dell'ultimo post caricato
let isLoading = false;
let lastCommentId = 10000;

document.addEventListener('DOMContentLoaded', () => {
    loadPosts('/api/posts/generateXml', true);
});

function loadPosts(endpoint, showLoadMoreButton = false) {
    if (isLoading) return;

    isLoading = true;
    console.log("Caricamento post...  last id:", lastId);
    fetch(`/api/posts/generateXml?lastId=${lastId}&size=3`)
        .then(response => {
            console.log("Risposta dal server:", response);
            if (!response.ok) {
                throw new Error("Errore nella risposta del server.");
            }
            return response.text(); // Poiché il controller restituisce un XML
        })
        .then(data => {
            console.log("XML ricevuto:", data);
            const posts = parseXML(data); // Parsing dell'XML
            console.log("Numero di post parsati:", posts.length);
            const mainContainer = document.querySelector(".main-container");

            posts.forEach(post => {
                const postId = post.querySelector("idPost").textContent;
                console.log("Aggiungendo post ID:", postId);
                const postElement = createPostElement(post);
                mainContainer.appendChild(postElement);
            });

            if (posts.length > 0) {
                lastId = posts[posts.length - 1].querySelector("idPost").textContent; // Ultimo ID, quello più vecchio
                console.log("Ultimo ID aggiornato a:", lastId);
            }

            if (showLoadMoreButton && posts.length === 3) {
                const loadMoreButton = createLoadMoreButton(mainContainer);
                mainContainer.appendChild(loadMoreButton);
            }

            isLoading = false;
        })
        .catch(error => {
            console.error("Errore nel caricamento dei post:", error);
            isLoading = false;
        });
}

function parseXML(data) {
    console.log("Parsing XML...");
    const xmlDoc = new DOMParser().parseFromString(data, "application/xml");
    const posts = Array.from(xmlDoc.getElementsByTagName("post"));
    console.log("Post parsati:", posts);
    return posts;
}

function createPostElement(post) {
    const postId = post.querySelector("idPost").textContent;
    const author = post.querySelector("author").textContent;
    const date = post.querySelector("data").textContent;
    const time = post.querySelector("ora").textContent;
    const content = post.querySelector("testo").textContent;

    console.log(`Creazione del post - ID: ${postId}, Autore: ${author}, Data: ${date}, Ora: ${time}`);

    const postElement = document.createElement("div");
    postElement.classList.add("post");
    postElement.setAttribute("data-post-id", postId);
    postElement.innerHTML = `
        <div class="post-header">
            <span class="post-author">@${author}</span>
            <span class="post-date">${formatDateTime(date, time)}</span>
        </div>
        <p class="post-content">${content}</p>
        <div class="post-footer"></div>
    `;
    postElement.addEventListener('click', function() {
        console.log(`Cliccato sul post - ID: ${postId}`);
        openModal(post);
    });

    return postElement;
}

function createLoadMoreButton(container) {
    const button = document.createElement("button");
    button.id = "load-more";
    button.classList.add("button");
    button.textContent = "Carica altro..";
    button.onclick = () => {
        button.remove(); // Rimuovi il pulsante mentre carica
        loadPosts('/api/posts/generateXml', true);  // Carica altri post
    };
    return button;
}

function formatDateTime(dateString, timeString) {
    const combinedDateTime = new Date(`${dateString}T${timeString}`);
    const formattedDate = new Intl.DateTimeFormat("it-IT", {
        day: "numeric", month: "long", year: "numeric"
    }).format(combinedDateTime);
    const formattedTime = new Intl.DateTimeFormat("it-IT", {
        hour: "2-digit", minute: "2-digit"
    }).format(combinedDateTime);
    return `Pubblicato il ${formattedDate} alle ${formattedTime}`;
}

function openModal(post) {
    console.log("Apertura modale per il post:", post);

    const modal = document.getElementById("postModal");
    const overlay = document.querySelector(".modal-overlay");

    const postId = post.querySelector("idPost").textContent;
    const author = post.querySelector("author").textContent;
    const date = post.querySelector("data").textContent;
    const time = post.querySelector("ora").textContent;
    const content = post.querySelector("testo").textContent;

    document.getElementById("modalPostHeader").innerHTML = `
        <div class="post-header">
            <h1 class="post-title">Post di ${author}</h1>
            <span class="post-meta">${formatDateTime(date, time)}</span>
        </div>
    `;
    document.getElementById("modalPostBody").innerHTML = `
        <div class="post">
            <p>${content}</p>
        </div>
    `;
    document.getElementById("modalNewComment").innerHTML = `
        <div class="new-comment" data-post-id="${postId}">
            <form>
                <textarea id="newCommentText-${postId}" placeholder="Scrivi il tuo commento qui..." required></textarea>
                <button type="button" class="submit-comment" data-post-id="${postId}">Pubblica commento</button>
            </form>
        </div>`;

    const submitButton = document.querySelector(`.submit-comment[data-post-id="${postId}"]`);
    if (submitButton) {
        submitButton.addEventListener("click", handleNewComment);
    } else {
        console.error("Pulsante Submit non trovato.");
    }

    // Caricamento dei commenti
    loadComments(postId, true);

    modal.style.display = "block";
    overlay.style.display = "block";
    document.body.classList.add("no-scroll");
    overlay.addEventListener('click', closeModal);
}

function loadComments(postId, showLoadMoreButton = false) {
    if (isLoading) return;

    isLoading = true;
    console.log("Caricamento commenti per il post ID:", postId);
    console.log(`Url: /api/commenti?postId=${postId}&lastId=${lastCommentId}&size=3`);

    fetch(`/api/commenti?postId=${postId}&lastId=${lastCommentId}&size=3`)
        .then(response => {
            console.log("Risposta dal server:", response);
            if (!response.ok) {
                throw new Error("Errore nella risposta del server.");
            }
            return response.text(); // Poiché il controller restituisce un XML
        })
        .then(data => {
            console.log("XML ricevuto per i commenti:", data);
            const comments = parseXMLComments(data); // Parsing dell'XML dei commenti
            console.log("Numero di commenti parsati:", comments.length);

            const modalCommentSection = document.getElementById("modalCommentSection");
            comments.forEach(comment => {
                const commentElement = createCommentElement(comment);
                modalCommentSection.appendChild(commentElement);
            });

            if (comments.length > 0) {
                lastCommentId = comments[comments.length - 1].querySelector("idCommento").textContent; // Aggiorna l'ID dell'ultimo commento
                console.log("Ultimo ID commento aggiornato a:", lastCommentId);
            }

            if (showLoadMoreButton) {

                console.log("Verifica ulteriori commenti con lastCommentId:", lastCommentId);
                hasMoreComments(lastCommentId, postId).then(hasMore => {
                    if (hasMore) {
                        const loadMoreButton = createCommentsLoadMoreButton(modalCommentSection, postId);
                        modalCommentSection.appendChild(loadMoreButton);
                    }
                });
            }

            isLoading = false;
        })
        .catch(error => {
            console.error("Errore nel caricamento dei commenti:", error);
            isLoading = false;
        });
}

function parseXMLComments(data) {
    console.log("Parsing XML dei commenti...");
    const xmlDoc = new DOMParser().parseFromString(data, "application/xml");
    const comments = Array.from(xmlDoc.getElementsByTagName("comment"));
    console.log("Commenti parsati:", comments);

    const modalCommentSection = document.getElementById("modalCommentSection");

    // Rimuove eventuali messaggi precedenti
    const noCommentsMessage = modalCommentSection.querySelector(".no-comments");
    if (noCommentsMessage) {
        noCommentsMessage.remove();
    }

    if (comments.length === 0) {
        const message = document.createElement("p");
        message.textContent = "Nessun commento disponibile per questo post.";
        message.classList.add("no-comments");
        modalCommentSection.appendChild(message);
    }

    return comments;
}

function createCommentElement(comment) {
    const commentId = comment.querySelector("idCommento").textContent;
    const author = comment.querySelector("autore").textContent;
    const date = comment.querySelector("data").textContent;
    const time = comment.querySelector("ora").textContent;
    const content = comment.querySelector("contenuto").textContent;

    const commentElement = document.createElement("div");
    commentElement.classList.add("comment");
    commentElement.innerHTML = `
        <div class="comment-header">
            <span class="comment-author">@${author}</span>
            <span>${formatDateTime(date, time)}</span>
        </div>
        <div class="comment-body">${content}</div>
    `;
    return commentElement;
}

function createCommentsLoadMoreButton(container, postId) {
    const button = document.createElement("button");
    button.id = "load-more-comments";
    button.classList.add("button");
    button.textContent = "Carica altri commenti...";
    button.onclick = () => {
        button.remove(); // Rimuovi il pulsante mentre carica
        loadComments(postId, true);  // Carica altri commenti
    };
    return button;
}

function hasMoreComments(lastId, postId) {
    return fetch(`/api/commenti/hasMore?postId=${postId}&lastId=${lastId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Errore nella verifica di ulteriori commenti.");
            }
            return response.json();
        })
        .then(data => data) // Restituisce true o false
        .catch(error => {
            console.error("Errore nel controllo di ulteriori commenti:", error);
            return false; // Default in caso di errore
        });
}

function closeModal() {
    document.getElementById("postModal").style.display = "none";
    document.querySelector(".modal-overlay").style.display = "none";
    document.body.classList.remove("no-scroll");
    // Resetta il contenuto della modale
    document.getElementById("modalPostHeader").innerHTML = "";
    document.getElementById("modalPostBody").innerHTML = "";
    document.getElementById("modalNewComment").innerHTML = "";
    document.getElementById("modalCommentSection").innerHTML = "";

    lastCommentId = 10000; // Resetta l'ID dell'ultimo commento
}

function handleNewComment(event) {
    const button = event.target;
    const postId = button.getAttribute("data-post-id");
    const commentText = document.getElementById(`newCommentText-${postId}`).value;

    if (!commentText.trim()) {
        alert("Il commento non può essere vuoto.");
        return;
    }

    // Recupera dinamicamente l'ID utente
    let userId = 1; // Default
    fetch('/api/user/me')
        .then(response => {
            if (response.ok) {
                return response.json(); // Assumiamo che ritorni un numero
            } else {
                console.warn('Impossibile recuperare l\'ID utente. Utilizzo ID predefinito (1).');
                return Promise.resolve(userId);
            }
        })
        .then(fetchedUserId => {
            userId = fetchedUserId;

            // Crea i dati del commento
            const commentData = {
                contenuto: commentText,
                post: { idPost: parseInt(postId, 10) }
            };

            // Invio del commento
            return fetch(`/api/commenti?userId=${userId}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(commentData)
            });
        })
        .then(response => {
            if (!response.ok) throw new Error("Errore nell'aggiunta del commento.");
            return response.json();
        })
        .then(comment => {
            console.log("Commento aggiunto:", comment);

            // messaggio di successo
            const message = document.getElementById('message-comment');
            message.textContent = 'Commento pubblicato con successo!';
            message.className = 'success';

            // Svuota il campo di testo del commento
            document.getElementById(`newCommentText-${postId}`).value = "";

            // Aggiorna la sezione dei commenti
            lastCommentId = 10000; // Resetta l'ID dell'ultimo commento
            document.getElementById("modalCommentSection").innerHTML = "";
            loadComments(postId, true); // Ricarica i commenti

            // Chiusura del messaggio dopo un certo periodo
            setTimeout(() => {
                message.textContent = '';
                message.className = 'hidden';
            }, 2000);
        })
        .catch(error => {
            console.error("Errore nell'aggiunta del commento:", error);
            const message = document.getElementById('message-comment');
            message.textContent = 'Si è verificato un errore durante l\'aggiunta del commento.';
            message.className = 'error';
            setTimeout(() => {
                message.textContent = '';
                message.className = 'hidden';
            }, 2000);
        });
}







