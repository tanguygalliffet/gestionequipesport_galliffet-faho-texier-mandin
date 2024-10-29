// training_script.js

document.addEventListener('DOMContentLoaded', () => {
    fetchAllTrainings();
    setupEventListeners();
});

/**
 * Récupère et affiche tous les entraînements dans le tableau.
 */
async function fetchAllTrainings() {
    try {
        const response = await fetch('/api/trainings');
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des entraînements');
        }
        const trainings = await response.json();
        populateTrainingsTable(trainings);
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Affiche les entraînements dans le tableau HTML.
 * @param {Array} trainings - Liste des entraînements.
 */
function populateTrainingsTable(trainings) {
    const tbody = document.querySelector('#trainingsTable tbody');
    tbody.innerHTML = ''; // Réinitialiser le contenu

    trainings.forEach(training => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${training.trNumero}</td>
            <td>${training.eqNumero || '-'}</td>
            <td>${formatDateTime(training.trDateDeb)}</td>
            <td>${formatDateTime(training.trDateFin)}</td>
            <td class="actions">
                <button class="toggle-materials-button" data-trnumero="${training.trNumero}">Voir Matériels</button>
                <button class="edit-button" onclick="openEditModal(${training.trNumero})">Modifier</button>
                <button class="delete-button" onclick="deleteTraining(${training.trNumero})">Supprimer</button>
            </td>
        `;

        // Ligne pour les matériels utilisés (cachée par défaut)
        const materialsRow = document.createElement('tr');
        materialsRow.classList.add('materials-row');
        materialsRow.dataset.trnumero = training.trNumero;
        materialsRow.style.display = 'none';

        let materialsHtml = '';
        if (training.materielsUtilises && training.materielsUtilises.length > 0) {
            materialsHtml = '<ul>';
            training.materielsUtilises.forEach(material => {
                materialsHtml += `<li>${material.materielNom}: ${material.nombreUtilise}</li>`;
            });
            materialsHtml += '</ul>';
        } else {
            materialsHtml = '<p>Aucun matériel utilisé.</p>';
        }

        materialsRow.innerHTML = `
            <td colspan="5">
                <div class="materials-container">
                    ${materialsHtml}
                </div>
            </td>
        `;

        tbody.appendChild(tr);
        tbody.appendChild(materialsRow);
    });

    // Ajouter les écouteurs pour les boutons "Voir Matériels"
    document.querySelectorAll('.toggle-materials-button').forEach(button => {
        button.addEventListener('click', toggleMaterialsRow);
    });
}

/**
 * Formate une date et heure au format YYYY-MM-DD HH:MM.
 * @param {string} dateStr - Chaîne de date.
 * @returns {string} - Date et heure formatées.
 */
function formatDateTime(dateStr) {
    const date = new Date(dateStr);
    if (isNaN(date)) return '-';
    const year = date.getFullYear();
    const month = (`0${(date.getMonth() + 1)}`).slice(-2);
    const day = (`0${date.getDate()}`).slice(-2);
    const hours = (`0${date.getHours()}`).slice(-2);
    const minutes = (`0${date.getMinutes()}`).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

/**
 * Configure les écouteurs d'événements pour les boutons et formulaires.
 */
function setupEventListeners() {
    // Bouton pour ouvrir le modal d'ajout
    document.getElementById('openAddModalButton').addEventListener('click', openAddModal);

    // Boutons de fermeture des modales
    document.getElementById('closeAddModal').addEventListener('click', closeAddModal);
    document.getElementById('closeEditModal').addEventListener('click', closeEditModal);

    // Boutons d'annulation dans les modales
    document.getElementById('cancelAdd').addEventListener('click', closeAddModal);
    document.getElementById('cancelEdit').addEventListener('click', closeEditModal);

    // Formulaires de modales
    document.getElementById('addTrainingForm').addEventListener('submit', addTraining);
    document.getElementById('editTrainingForm').addEventListener('submit', updateTraining);

    // Récupérer les équipes pour les listes déroulantes
    fetchAllEquipes();

    // Récupérer les matériels pour les listes
    fetchAllMateriels();
}

/**
 * Ouvre le modal d'ajout d'un entraînement.
 */
function openAddModal() {
    document.getElementById('addTrainingForm').reset();
    document.getElementById('addModal').style.display = 'block';
}

/**
 * Ferme le modal d'ajout d'un entraînement.
 */
function closeAddModal() {
    document.getElementById('addModal').style.display = 'none';
}

/**
 * Ouvre le modal de modification d'un entraînement avec les données pré-remplies.
 * @param {number} trNumero - Numéro de l'entraînement à modifier.
 */
async function openEditModal(trNumero) {
    try {
        const response = await fetch(`/api/trainings/${trNumero}`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Entraînement non trouvé.');
            } else {
                throw new Error('Erreur lors de la récupération de l\'entraînement.');
            }
        }
        const training = await response.json();
        populateEditForm(training);
        document.getElementById('editModal').style.display = 'block';
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Remplit le formulaire de modification avec les données de l'entraînement.
 * @param {Object} training - Objet entraînement.
 */
function populateEditForm(training) {
    document.getElementById('editTrNumero').value = training.trNumero;
    document.getElementById('editEqNumero').value = training.eqNumero;
    document.getElementById('editTrDateDeb').value = formatDateTimeLocal(training.trDateDeb);
    document.getElementById('editTrDateFin').value = formatDateTimeLocal(training.trDateFin);
    fetchAllMateriels().then(materiels => {
        populateMaterielsCheckboxes('editMaterielsContainer', materiels, training.materielsUtilises);
    });
}


/**
 * Ferme le modal de modification d'un entraînement.
 */
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

/**
 * Ajoute un nouvel entraînement en soumettant le formulaire d'ajout.
 * @param {Event} event - Événement de soumission du formulaire.
 */
async function addTraining(event) {
    event.preventDefault();

    const eqNumero = document.getElementById('addEqNumero').value;
    const trDateDeb = document.getElementById('addTrDateDeb').value;
    const trDateFin = document.getElementById('addTrDateFin').value;
    const materiels = getSelectedMateriels('addMaterielsContainer');

    const data = {
        eqNumero: parseInt(eqNumero, 10),
        trDateDeb: trDateDeb,
        trDateFin: trDateFin,
        materielsUtilises: materiels
    };

    try {
        const response = await fetch('/api/trainings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erreur lors de l\'ajout de l\'entraînement');
        }

        const newTraining = await response.json();
        Swal.fire('Succès', 'Entraînement ajouté avec succès.', 'success');
        closeAddModal();
        fetchAllTrainings();
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Met à jour un entraînement en soumettant le formulaire de modification.
 * @param {Event} event - Événement de soumission du formulaire.
 */
async function updateTraining(event) {
    event.preventDefault();

    const trNumero = document.getElementById('editTrNumero').value;
    const eqNumero = document.getElementById('editEqNumero').value;
    const trDateDeb = document.getElementById('editTrDateDeb').value;
    const trDateFin = document.getElementById('editTrDateFin').value;
    const materiels = getSelectedMateriels('editMaterielsContainer');

    const data = {
        trNumero: parseInt(trNumero, 10),
        eqNumero: parseInt(eqNumero, 10),
        trDateDeb: trDateDeb,
        trDateFin: trDateFin,
        materielsUtilises: materiels
    };

    try {
        const response = await fetch(`/api/trainings/${trNumero}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            if (response.status === 404) {
                throw new Error('Entraînement non trouvé.');
            } else {
                throw new Error(errorText || 'Erreur lors de la mise à jour de l\'entraînement.');
            }
        }

        const updatedTraining = await response.json();
        Swal.fire('Succès', 'Entraînement mis à jour avec succès.', 'success');
        closeEditModal();
        fetchAllTrainings();
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
    }
}

/**
 * Supprime un entraînement après confirmation.
 * @param {number} trNumero - Numéro de l'entraînement à supprimer.
 */
async function deleteTraining(trNumero) {
    const confirmation = await Swal.fire({
        title: 'Êtes-vous sûr ?',
        text: 'Voulez-vous vraiment supprimer cet entraînement ?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler'
    });

    if (confirmation.isConfirmed) {
        try {
            const response = await fetch(`/api/trainings/${trNumero}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorText = await response.text();
                if (response.status === 404) {
                    throw new Error('Entraînement non trouvé.');
                } else {
                    throw new Error(errorText || 'Erreur lors de la suppression de l\'entraînement.');
                }
            }

            Swal.fire('Supprimé!', 'Entraînement supprimé avec succès.', 'success');
            fetchAllTrainings();
        } catch (error) {
            console.error(error);
            Swal.fire('Erreur', error.message, 'error');
        }
    }
}

/**
 * Affiche ou masque les matériels utilisés pour un entraînement.
 * @param {Event} event - Événement du bouton.
 */
function toggleMaterialsRow(event) {
    const trNumero = event.target.dataset.trnumero;
    const materialsRow = document.querySelector(`.materials-row[data-trnumero="${trNumero}"]`);
    if (materialsRow.style.display === 'none') {
        materialsRow.style.display = '';
        event.target.textContent = 'Cacher Matériels';
    } else {
        materialsRow.style.display = 'none';
        event.target.textContent = 'Voir Matériels';
    }
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
        populateEquipeSelect('addEqNumero', equipes);
        populateEquipeSelect('editEqNumero', equipes);
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
 * Récupère et remplit les champs pour les matériels.
 */
async function fetchAllMateriels() {
    try {
        const response = await fetch('/api/materiels');
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des matériels');
        }
        const materiels = await response.json();
        populateMaterielsCheckboxes('addMaterielsContainer', materiels);
        return materiels; // Retourne les matériels pour un usage ultérieur
    } catch (error) {
        console.error(error);
        Swal.fire('Erreur', error.message, 'error');
        return [];
    }
}

/**
 * Remplit les champs pour les matériels avec quantités.
 * @param {string} containerId - ID du conteneur pour les matériels.
 * @param {Array} materiels - Liste des matériels.
 * @param {Array} selectedMateriels - Liste des matériels sélectionnés (pour le formulaire de modification).
 */
function populateMaterielsCheckboxes(containerId, materiels, selectedMateriels = []) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';

    materiels.forEach(materiel => {
        const div = document.createElement('div');
        div.classList.add('materiel-item');

        const label = document.createElement('label');
        label.textContent = materiel.materielNom;

        const input = document.createElement('input');
        input.type = 'number';
        input.min = 0;
        input.placeholder = 'Quantité';
        input.name = 'materiels[' + materiel.materielNumero + ']';

        // Pré-remplir les quantités pour les matériels sélectionnés
        const selectedMateriel = selectedMateriels.find(m => m.materielNumero === materiel.materielNumero);
        if (selectedMateriel) {
            input.value = selectedMateriel.nombreUtilise;
        } else {
            input.value = 0; // Ou laissez vide selon votre logique
        }

        div.appendChild(label);
        div.appendChild(input);

        container.appendChild(div);
    });
}

/**
 * Récupère les matériels sélectionnés et leurs quantités.
 * @param {string} containerId - ID du conteneur des matériels.
 * @returns {Array} - Liste des matériels sélectionnés avec leurs quantités.
 */
function getSelectedMateriels(containerId) {
    const container = document.getElementById(containerId);
    const inputs = container.querySelectorAll('input[type="number"]');
    const materiels = [];

    inputs.forEach(input => {
        const nombreUtilise = parseInt(input.value, 10);
        if (!isNaN(nombreUtilise) && nombreUtilise > 0) {
            const materielNumero = parseInt(input.dataset.materielNumero, 10);
            materiels.push({
                materielNumero: materielNumero,
                nombreUtilise: nombreUtilise
            });
        }
    });

    return materiels;
}

/**
 * Formate une date pour l'utiliser dans un input de type datetime-local.
 * @param {string} dateStr - Chaîne de date.
 * @returns {string} - Date formatée pour datetime-local.
 */
function formatDateTimeLocal(dateStr) {
    const date = new Date(dateStr);
    if (isNaN(date)) return '';
    const year = date.getFullYear();
    const month = (`0${(date.getMonth() + 1)}`).slice(-2);
    const day = (`0${date.getDate()}`).slice(-2);
    const hours = (`0${date.getHours()}`).slice(-2);
    const minutes = (`0${date.getMinutes()}`).slice(-2);
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}
