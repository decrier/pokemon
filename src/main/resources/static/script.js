const API = '';
let pokemonInput = '';
        
        // Suche Pokemon nach Name oder ID
        document.getElementById('search-btn').addEventListener('click', () => {
            findPokemonByNameOrId();
        });


        // Ins Team hinzufügen
        document.getElementById('add-btn').addEventListener('click', () => {
            addInTeam();       
        });


        // Suche nach Typ
        document.getElementById('type-btn').addEventListener('click', () => {
            findByType();
        });

        // Suche nach Schwäche oder Stärke 
        document.getElementById('weaks-btn').addEventListener('click', () => {
            findWeaks();
        });
        document.getElementById('strongs-btn').addEventListener('click', () => {
            findStrongs();
        });
        

        // Players anzeigen
        document.getElementById('players-btn').addEventListener('click', () => {
            loadPlayersList();
        });

        

        // Teamliste anzeigen
        document.getElementById('teams-btn').addEventListener('click', () => {
            loadTeamsList();
        });

        
        // Player Löschen
        document.getElementById('delete-btn').addEventListener('click', () => {
            deletePlayer();
        });

        // Alle Players Löschen
        document.getElementById('clear-btn').addEventListener('click', () => {
            clearList();
        });


        // Team speichern
        document.getElementById('save-btn').addEventListener('click', () => {
            saveTeam();
        });


        // Team laden
        document.getElementById('load-btn').addEventListener('click', () => {
            loadTeam();
            document.getElementById('teams-list').innerHTML = '';
        });

        // Team löschen
        document.getElementById('delete-team-btn').addEventListener('click', () => {
            deleteTeam();
        });

        // Team analysieren
        document.getElementById('weakness-btn').addEventListener('click', () => {
            analyzeTeamWeakness();
        });
        document.getElementById('strength-btn').addEventListener('click', () => {
            analyzeTeamStrength();
        });

        document.getElementById('type-list').addEventListener('click', function(event) {
            if (event.target && event.target.nodeName === 'LI') {
                document.getElementById('search-input').value = event.target.textContent;
            }
        });

        document.getElementById('players-list').addEventListener('click', function(event) {
            if (event.target && event.target.nodeName === 'LI') {
                document.getElementById('delete-name').value = event.target.textContent;
            }
        });

        document.getElementById('teams-list').addEventListener('click', function(event) {
            if (event.target && event.target.nodeName === 'LI') {
                document.getElementById('team-name').value = event.target.textContent;
            }
        });

        document.getElementById('s-list').addEventListener('click', function(event) {
            if (event.target && event.target.nodeName === 'LI') {
                document.getElementById('strongs-weaks-input').value = event.target.textContent;
            }
        });