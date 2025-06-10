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


        // Team speichern
        document.getElementById('save-btn').addEventListener('click', () => {
            saveTeam();
        });


        // Team laden
        document.getElementById('load-btn').addEventListener('click', () => {
            loadTeam();
            document.getElementById('teams-list').innerHTML = '';
        });

        // Team analysieren
        document.getElementById('weakness-btn').addEventListener('click', () => {
            analyzeTeamWeakness();
        });
        document.getElementById('strength-btn').addEventListener('click', () => {
            analyzeTeamStrength();
        });