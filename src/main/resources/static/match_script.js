// script.js

document.addEventListener('DOMContentLoaded', () => {
    loadMatches();

    document.getElementById('addMatchButton').addEventListener('click', showAddMatchForm);
    document.getElementById('closeFormButton').addEventListener('click', closeMatchForm);
    document.getElementById('matchForm').addEventListener('submit', submitMatchForm);
});

function loadMatches() {
    fetch('/api/matches')
        .then(response => response.json())
        .then(matches => {
            const tbody = document.querySelector('#matchesTable tbody');
            tbody.innerHTML = '';
            matches.forEach(match => {
                const tr = document.createElement('tr');

                tr.innerHTML = `
                    <td>${match.matchNumero}</td>
                    <td>${match.eqNom}</td>
                    <td>${match.matchEqAdv}</td>
                    <td>${formatDateTime(match.matchDate)}</td>
                    <td>${match.matchLieu}</td>
                    <td>${match.matchExterieur ? 'Oui' : 'Non'}</td>
                    <td>${match.matchButEnc}</td>
                    <td>${match.matchButMis}</td>
                    <td>
                        <button onclick="showEditMatchForm(${match.matchNumero})">Modifier</button>
                        <button onclick="deleteMatch(${match.matchNumero})">Supprimer</button>
                    </td>
                `;

                tbody.appendChild(tr);
            });
        });
}

function showAddMatchForm() {
    document.getElementById('matchForm').reset();
    document.getElementById('matchNumero').value = '';
    document.getElementById('formTitle').textContent = 'Ajouter un Match';
    document.getElementById('submitButton').textContent = 'Enregistrer';

    loadEquipes();

    document.getElementById('matchFormContainer').style.display = 'block';
}

function showEditMatchForm(matchNumero) {
    fetch(`/api/matches/${matchNumero}`)
        .then(response => response.json())
        .then(match => {
            document.getElementById('matchNumero').value = match.matchNumero;
            document.getElementById('formTitle').textContent = 'Modifier le Match';
            document.getElementById('submitButton').textContent = 'Mettre à jour';

            loadEquipes(match.eqNumero);

            document.getElementById('matchEqAdv').value = match.matchEqAdv;
            document.getElementById('matchDate').value = formatDateTimeLocal(match.matchDate);
            document.getElementById('matchLieu').value = match.matchLieu;
            document.getElementById('matchExterieur').value = match.matchExterieur;
            document.getElementById('matchButEnc').value = match.matchButEnc;
            document.getElementById('matchButMis').value = match.matchButMis;

            document.getElementById('matchFormContainer').style.display = 'block';
        });
}

function closeMatchForm() {
    document.getElementById('matchFormContainer').style.display = 'none';
}

function submitMatchForm(event) {
    event.preventDefault();

    const matchNumero = document.getElementById('matchNumero').value;
    const eqNumero = document.getElementById('eqNumero').value;
    const matchEqAdv = document.getElementById('matchEqAdv').value;
    const matchDate = document.getElementById('matchDate').value;
    const matchLieu = document.getElementById('matchLieu').value;
    const matchExterieur = document.getElementById('matchExterieur').value === 'true';
    const matchButEnc = document.getElementById('matchButEnc').value;
    const matchButMis = document.getElementById('matchButMis').value;

    const match = {
        eqNumero: parseInt(eqNumero),
        matchEqAdv,
        matchDate,
        matchLieu,
        matchExterieur,
        matchButEnc: parseInt(matchButEnc),
        matchButMis: parseInt(matchButMis)
    };

    if (matchNumero) {
        // Mise à jour
        fetch(`/api/matches/${matchNumero}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(match)
        }).then(() => {
            closeMatchForm();
            loadMatches();
        });
    } else {
        // Ajout
        fetch('/api/matches', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(match)
        }).then(() => {
            closeMatchForm();
            loadMatches();
        });
    }
}

function deleteMatch(matchNumero) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce match ?')) {
        fetch(`/api/matches/${matchNumero}`, {
            method: 'DELETE'
        }).then(() => {
            loadMatches();
        });
    }
}

function loadEquipes(selectedEqNumero) {
    fetch('/api/equipes')
        .then(response => response.json())
        .then(equipes => {
            const eqNumeroSelect = document.getElementById('eqNumero');
            eqNumeroSelect.innerHTML = '';
            equipes.forEach(equipe => {
                const option = document.createElement('option');
                option.value = equipe.eqNumero;
                option.textContent = equipe.eqNom;
                if (selectedEqNumero && equipe.eqNumero === selectedEqNumero) {
                    option.selected = true;
                }
                eqNumeroSelect.appendChild(option);
            });
        });
}

function formatDateTime(dateTimeString) {
    const options = { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' };
    const date = new Date(dateTimeString);
    return date.toLocaleString('fr-FR', options);
}

function formatDateTimeLocal(dateTimeString) {
    const date = new Date(dateTimeString);
    const tzOffset = date.getTimezoneOffset() * 60000;
    const localISOTime = new Date(date - tzOffset).toISOString().slice(0, -1);
    return localISOTime;
}
