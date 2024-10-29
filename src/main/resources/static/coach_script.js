document.addEventListener('DOMContentLoaded', () => {
    fetchAllEntraineurs();
    fetchAllEquipes();
    setupEventListeners();
});

/**
 * Récupère et affiche tous les entraîneurs dans le tableau.
 */
async function fetchAllEntraineurs() {
    try {
        const response = await fetch('/api/entraineurs');
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des entraîneurs');
        }
        const entraineurs = await response.json();
        populateEntraineursTable(entraineurs);
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Affiche les entraîneurs dans le tableau HTML.
 * @param {Array} entraineurs - Liste des entraîneurs.
 */
function populateEntraineursTable(entraineurs) {
    const tbody = document.querySelector('#entraineursTable tbody');
    tbody.innerHTML = ''; // Réinitialiser le contenu

    entraineurs.forEach(entraineur => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${entraineur.enNumero}</td>
            <td>${entraineur.enNom}</td>
            <td>${entraineur.enPrenom}</td>
            <td>${formatDate(entraineur.enDateNai)}</td>
            <td>${entraineur.enEmail}</td>
            <td>${entraineur.enTelephone || '-'}</td>
            <td>${entraineur.eqNumero || '-'}</td>
            <td class="actions">
                <button class="edit-button" onclick="openEditModal(${entraineur.enNumero})">Modifier</button>
                <button class="delete-button" onclick="deleteEntraineur(${entraineur.enNumero})">Supprimer</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

/**
 * Formate une date au format YYYY-MM-DD.
 * @param {string} dateStr - Chaîne de date.
 * @returns {string} - Date formatée.
 */
function formatDate(dateStr) {
    const date = new Date(dateStr);
    if (isNaN(date)) return '-';
    const year = date.getFullYear();
    const month = (`0${(date.getMonth() + 1)}`).slice(-2);
    const day = (`0${date.getDate()}`).slice(-2);
    return `${year}-${month}-${day}`;
}

/**
 * Récupère et remplit les listes déroulantes des équipes.
 */
async function fetchAllEquipes() {
    try {
        const response = await fetch('/api/equipes');
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des équipes');
        }
        const equipes = await response.json();
        populateEquipeSelect('addEnEquipe', equipes);
        populateEquipeSelect('editEnEquipe', equipes);
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Remplit une liste déroulante des équipes.
 * @param {string} selectId - ID de la liste déroulante.
 * @param {Array} equipes - Liste des équipes.
 */
function populateEquipeSelect(selectId, equipes) {
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="">-- Sélectionnez une équipe --</option>'; // Réinitialiser les options

    equipes.forEach(equipe => {
        const option = document.createElement('option');
        option.value = equipe.eqNumero;
        option.textContent = equipe.eqNom;
        select.appendChild(option);
    });
}

/**
 * Configure les écouteurs d'événements pour les boutons et formulaires.
 */
function setupEventListeners() {
    // Boutons de fermeture des modaux
    document.getElementById('closeAddModal').addEventListener('click', closeAddModal);
    document.getElementById('closeEditModal').addEventListener('click', closeEditModal);

    // Boutons d'annulation dans les modaux
    document.getElementById('cancelAdd').addEventListener('click', closeAddModal);
    document.getElementById('cancelEdit').addEventListener('click', closeEditModal);

    // Bouton pour ouvrir le modal d'ajout
    document.getElementById('openAddModalButton').addEventListener('click', openAddModal);

    // Formulaires de modaux
    document.getElementById('addEntraineurForm').addEventListener('submit', addEntraineur);
    document.getElementById('editEntraineurForm').addEventListener('submit', updateEntraineur);

    // Boutons de recherche et réinitialisation
    document.getElementById('searchButton').addEventListener('click', searchEntraineurs);
    document.getElementById('resetButton').addEventListener('click', resetSearch);

    // Fermeture des modaux en cliquant en dehors du contenu
    window.addEventListener('click', (event) => {
        const addModal = document.getElementById('addModal');
        const editModal = document.getElementById('editModal');

        if (event.target === addModal) {
            closeAddModal();
        }

        if (event.target === editModal) {
            closeEditModal();
        }
    });
}

/**
 * Recherche des entraîneurs par nom.
 */
async function searchEntraineurs() {
    const nom = document.getElementById('searchNom').value.trim();

    if (nom === '') {
        Swal.fire('Attention', 'Veuillez entrer un nom pour la recherche.', 'warning');
        return;
    }

    try {
        const response = await fetch(`/api/entraineurs/search?nom=${encodeURIComponent(nom)}`);
        if (!response.ok) {
            throw new Error('Erreur lors de la recherche des entraîneurs');
        }
        const entraineurs = await response.json();
        populateEntraineursTable(entraineurs);
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Réinitialise la recherche et affiche tous les entraîneurs.
 */
function resetSearch() {
    document.getElementById('searchNom').value = '';
    fetchAllEntraineurs();
}

/**
 * Ouvre le modal d'ajout d'un entraîneur.
 */
function openAddModal() {
    document.getElementById('addEntraineurForm').reset();
    document.getElementById('addModal').style.display = 'block';
}

/**
 * Ferme le modal d'ajout d'un entraîneur.
 */
function closeAddModal() {
    document.getElementById('addModal').style.display = 'none';
}

/**
 * Ouvre le modal de modification d'un entraîneur avec les données pré-remplies.
 * @param {number} enNumero - Numéro de l'entraîneur à modifier.
 */
async function openEditModal(enNumero) {
    try {
        const response = await fetch(`/api/entraineurs/${enNumero}`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Entraîneur non trouvé.');
            } else {
                throw new Error('Erreur lors de la récupération de l\'entraîneur.');
            }
        }
        const entraineur = await response.json();
        populateEditForm(entraineur);
        document.getElementById('editModal').style.display = 'block';
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Remplit le formulaire de modification avec les données de l'entraîneur.
 * @param {Object} entraineur - Objet entraîneur.
 */
function populateEditForm(entraineur) {
    document.getElementById('editEnNumero').value = entraineur.enNumero;
    document.getElementById('editEnNom').value = entraineur.enNom;
    document.getElementById('editEnPrenom').value = entraineur.enPrenom;
    document.getElementById('editEnDateNai').value = formatDate(entraineur.enDateNai);
    document.getElementById('editEnEmail').value = entraineur.enEmail;
    document.getElementById('editEnTelephone').value = entraineur.enTelephone || '';
    document.getElementById('editEnEquipe').value = entraineur.eqNumero || '';
}

/**
 * Ferme le modal de modification d'un entraîneur.
 */
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

/**
 * Ajoute un nouvel entraîneur en soumettant le formulaire d'ajout.
 * @param {Event} event - Événement de soumission du formulaire.
 */
async function addEntraineur(event) {
    event.preventDefault();

    const enNom = document.getElementById('addEnNom').value.trim();
    const enPrenom = document.getElementById('addEnPrenom').value.trim();
    const enDateNai = document.getElementById('addEnDateNai').value;
    const enEmail = document.getElementById('addEnEmail').value.trim();
    const enTelephone = document.getElementById('addEnTelephone').value.trim();
    const eqNumero = document.getElementById('addEnEquipe').value;

    if (eqNumero === '') {
        Swal.fire('Attention', 'Veuillez sélectionner une équipe.', 'warning');
        return;
    }

    const data = {
        enNom,
        enPrenom,
        enDateNai,
        enEmail,
        enTelephone,
        eqNumero: parseInt(eqNumero, 10)
    };

    try {
        const response = await fetch('/api/entraineurs', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error('Erreur lors de l\'ajout de l\'entraîneur');
        }

        const newEntraineur = await response.json();
        Swal.fire('Succès', 'Entraîneur ajouté avec succès.', 'success');
        closeAddModal();
        fetchAllEntraineurs();
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Met à jour un entraîneur en soumettant le formulaire de modification.
 * @param {Event} event - Événement de soumission du formulaire.
 */
async function updateEntraineur(event) {
    event.preventDefault();

    const enNumero = document.getElementById('editEnNumero').value.trim();
    const enNom = document.getElementById('editEnNom').value.trim();
    const enPrenom = document.getElementById('editEnPrenom').value.trim();
    const enDateNai = document.getElementById('editEnDateNai').value;
    const enEmail = document.getElementById('editEnEmail').value.trim();
    const enTelephone = document.getElementById('editEnTelephone').value.trim();
    const eqNumero = document.getElementById('editEnEquipe').value;

    if (enNumero === '' || isNaN(enNumero)) {
        Swal.fire('Erreur', 'Numéro d\'entraîneur invalide.', 'error');
        return;
    }

    if (eqNumero === '') {
        Swal.fire('Attention', 'Veuillez sélectionner une équipe.', 'warning');
        return;
    }

    const data = {
        enNom,
        enPrenom,
        enDateNai,
        enEmail,
        enTelephone,
        eqNumero: parseInt(eqNumero, 10)
    };

    try {
        const response = await fetch(`/api/entraineurs/${enNumero}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Entraîneur non trouvé.');
            } else {
                throw new Error('Erreur lors de la mise à jour de l\'entraîneur.');
            }
        }

        const updatedEntraineur = await response.json();
        Swal.fire('Succès', 'Entraîneur mis à jour avec succès.', 'success');
        closeEditModal();
        fetchAllEntraineurs();
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Supprime un entraîneur après confirmation.
 * @param {number} enNumero - Numéro de l'entraîneur à supprimer.
 */
async function deleteEntraineur(enNumero) {
    const confirmation = await Swal.fire({
        title: 'Êtes-vous sûr ?',
        text: 'Voulez-vous vraiment supprimer cet entraîneîneur ?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler'
    });

    if (confirmation.isConfirmed) {
        try {
            const response = await fetch(`/api/entraineurs/${enNumero}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Entraîneur non trouvé.');
                } else {
                    throw new Error('Erreur lors de la suppression de l\'entraîneur.');
                }
            }

            Swal.fire('Supprimé!', 'Entraîneur supprimé avec succès.', 'success');
            fetchAllEntraineurs();
        } catch (error) {
            console.error(error);
            Swal.fire('Erreur', error.message, 'error');
        }
    }
}
