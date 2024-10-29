// joueur_script.js

/**
 * Shows or hides the loading indicator.
 * @param {boolean} isLoading - If true, show the loading indicator; otherwise, hide it.
 */
function showLoading(isLoading) {
    const loadingIndicator = document.getElementById('loadingIndicator');
    if (loadingIndicator) {
        loadingIndicator.style.display = isLoading ? 'block' : 'none';
    }
}

/**
 * Displays an error message in the players table.
 * @param {string} message - The error message to display.
 */
function displayError(message) {
    const tableBody = document.getElementById('joueursTableBody');
    tableBody.innerHTML = '';
    const row = document.createElement('tr');
    const cell = document.createElement('td');
    cell.colSpan = 8; // Updated colspan to 8 to include Actions column
    cell.textContent = message;
    cell.style.textAlign = 'center';
    cell.style.color = 'red';
    row.appendChild(cell);
    tableBody.appendChild(row);
}

/**
 * Formats a date string into DD/MM/YY format.
 * @param {string} dateString - The date string to format.
 * @returns {string} - The formatted date string.
 */
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    if (isNaN(date)) return 'N/A'; // Handle invalid dates
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
    const year = date.getFullYear().toString().slice(-2);
    return `${day}/${month}/${year}`;
}

/**
 * Displays a list of players in the HTML table.
 * @param {Array<Object>} joueurs - The list of players to display.
 */
function displayJoueurs(joueurs) {
    const tableBody = document.getElementById('joueursTableBody');
    tableBody.innerHTML = '';

    if (!joueurs || joueurs.length === 0) {
        const row = document.createElement('tr');
        const cell = document.createElement('td');
        cell.colSpan = 8; // Updated colspan to 8 to include Actions column
        cell.textContent = 'Aucun joueur à afficher.';
        cell.style.textAlign = 'center';
        row.appendChild(cell);
        tableBody.appendChild(row);
        return;
    }

    joueurs.forEach(joueur => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${joueur.JONUMERO || 'N/A'}</td>
            <td>${joueur.JONOM || 'N/A'}</td>
            <td>${joueur.JOPRENOM || 'N/A'}</td>
            <td>${formatDate(joueur.JODATE_NAI)}</td>
            <td>${joueur.JOEMAIL || 'N/A'}</td>
            <td>${joueur.JOPOSTE || 'N/A'}</td>
            <td>${joueur.JOTELEPHONE || 'N/A'}</td>
            <td>
                <button class="edit-btn" onclick="openEditModal(${joueur.JONUMERO}, '${joueur.JONOM}', '${joueur.JOPRENOM}', '${joueur.JODATE_NAI}', '${joueur.JOEMAIL}', '${joueur.JOPOSTE}', '${joueur.JOTELEPHONE}')">Modifier</button>
                <button class="delete-btn" onclick="deleteJoueur(${joueur.JONUMERO})">Supprimer</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

/**
 * Fetches and displays all players.
 */
async function loadJoueurs() {
    try {
        showLoading(true);
        const response = await fetch('/api/joueurs');
        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const joueurs = await response.json();
        displayJoueurs(joueurs);
    } catch (error) {
        displayError("Échec du chargement des joueurs.");
        console.error("Failed to load all joueurs:", error);
    } finally {
        showLoading(false);
    }
}

/**
 * Prompts the user for a position and fetches players matching that position.
 */
async function loadJoueursParPoste() {
    const poste = prompt("Entrez le poste des joueurs à afficher (ex: ATTAQUANT)");
    if (!poste) return; // Cancel if no input

    // Optional: Validate the input to prevent SQL injection or invalid queries
    const validPostes = ['ATTAQUANT', 'DEFENSEUR', 'MILIEU', 'GARDIEN']; // Example positions
    if (!validPostes.includes(poste.toUpperCase())) {
        alert("Poste invalide. Veuillez entrer un poste valide (ex: ATTAQUANT).");
        return;
    }

    try {
        showLoading(true);
        const response = await fetch(`/api/joueurs/poste?poste=${encodeURIComponent(poste)}`);
        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const joueurs = await response.json();
        displayJoueurs(joueurs);
    } catch (error) {
        displayError(`Échec du chargement des joueurs avec le poste ${poste}.`);
        console.error(`Failed to load joueurs with poste ${poste}:`, error);
    } finally {
        showLoading(false);
    }
}

/**
 * Fetches and displays players older than 20 years.
 */
async function loadJoueursPlusDe20Ans() {
    try {
        showLoading(true);
        const response = await fetch('/api/joueurs/plus-de-20-ans');
        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const joueurs = await response.json();
        displayJoueurs(joueurs);
    } catch (error) {
        displayError("Échec du chargement des joueurs de plus de 20 ans.");
        console.error("Failed to load joueurs over 20:", error);
    } finally {
        showLoading(false);
    }
}

/**
 * Opens the Add Player modal.
 */
function openAddModal() {
    const addModal = document.getElementById('addModal');
    addModal.style.display = 'block';
}

/**
 * Closes the Add Player modal.
 */
function closeAddModal() {
    const addModal = document.getElementById('addModal');
    addModal.style.display = 'none';
    document.getElementById('addJoueurForm').reset();
}

/**
 * Opens the Edit Player modal with pre-filled data.
 * @param {number} numero - Player number.
 * @param {string} nom - Player's last name.
 * @param {string} prenom - Player's first name.
 * @param {string} dateNaissance - Player's date of birth.
 * @param {string} email - Player's email.
 * @param {string} poste - Player's position.
 * @param {string} telephone - Player's phone number.
 */
function openEditModal(numero, nom, prenom, dateNaissance, email, poste, telephone) {
    const editModal = document.getElementById('editModal');
    editModal.style.display = 'block';

    // Populate form fields with existing data
    document.getElementById('editNumero').value = numero;
    document.getElementById('editNom').value = nom;
    document.getElementById('editPrenom').value = prenom;
    document.getElementById('editDateNaissance').value = formatForInput(dateNaissance);
    document.getElementById('editEmail').value = email;
    document.getElementById('editPoste').value = poste;
    document.getElementById('editTelephone').value = telephone;
}

/**
 * Closes the Edit Player modal.
 */
function closeEditModal() {
    const editModal = document.getElementById('editModal');
    editModal.style.display = 'none';
    document.getElementById('editJoueurForm').reset();
}

/**
 * Formats a date string from the database to YYYY-MM-DD for input fields.
 * @param {string} dateString - The date string from the database.
 * @returns {string} - Formatted date string.
 */
function formatForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (isNaN(date)) return '';
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

/**
 * Handles the submission of the Add Player form.
 */
document.getElementById('addJoueurForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const nom = document.getElementById('addNom').value.trim();
    const prenom = document.getElementById('addPrenom').value.trim();
    const dateNaissance = document.getElementById('addDateNaissance').value;
    const email = document.getElementById('addEmail').value.trim();
    const poste = document.getElementById('addPoste').value.trim();
    const telephone = document.getElementById('addTelephone').value.trim();

    // Basic validation
    if (!nom || !prenom || !dateNaissance || !email || !poste || !telephone) {
        alert("Veuillez remplir tous les champs.");
        return;
    }

    try {
        showLoading(true);
        const response = await fetch('/api/joueurs', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nom,
                prenom,
                dateNaissance,
                email,
                poste,
                telephone
            })
        });

        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const newJoueur = await response.json();

        // Optionally, refresh the table or add the new player to the table
        loadJoueurs();

        closeAddModal();
    } catch (error) {
        displayError("Échec de l'ajout du joueur.");
        console.error("Failed to add joueur:", error);
    } finally {
        showLoading(false);
    }
});

/**
 * Handles the submission of the Edit Player form.
 */
document.getElementById('editJoueurForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const numero = parseInt(document.getElementById('editNumero').value);
    const nom = document.getElementById('editNom').value.trim();
    const prenom = document.getElementById('editPrenom').value.trim();
    const dateNaissance = document.getElementById('editDateNaissance').value;
    const email = document.getElementById('editEmail').value.trim();
    const poste = document.getElementById('editPoste').value.trim();
    const telephone = document.getElementById('editTelephone').value.trim();

    // Basic validation
    if (!nom || !prenom || !dateNaissance || !email || !poste || !telephone) {
        alert("Veuillez remplir tous les champs.");
        return;
    }

    try {
        showLoading(true);
        const response = await fetch(`/api/joueurs/${numero}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nom,
                prenom,
                dateNaissance,
                email,
                poste,
                telephone
            })
        });

        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const updatedJoueur = await response.json();

        // Optionally, refresh the table or update the specific row
        loadJoueurs();

        closeEditModal();
    } catch (error) {
        displayError("Échec de la modification du joueur.");
        console.error("Failed to update joueur:", error);
    } finally {
        showLoading(false);
    }
});

/**
 * Deletes a player after confirmation.
 * @param {number} numero - Player number to delete.
 */
async function deleteJoueur(numero) {
    const confirmation = confirm("Êtes-vous sûr de vouloir supprimer ce joueur ?");
    if (!confirmation) return;

    try {
        showLoading(true);
        const response = await fetch(`/api/joueurs/${numero}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error(`Erreur: ${response.statusText}`);
        const remainingJoueurs = await response.json();

        // Optionally, refresh the table
        loadJoueurs();
    } catch (error) {
        displayError("Échec de la suppression du joueur.");
        console.error("Failed to delete joueur:", error);
    } finally {
        showLoading(false);
    }
}

// Optional: Automatically load all players when the page loads
document.addEventListener('DOMContentLoaded', () => {
    loadJoueurs();
});
