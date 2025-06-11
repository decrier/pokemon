// Suche Pokemon nach Name oder ID
function findPokemonByNameOrId() {
    pokemonInput = document.getElementById('search-input').value.trim().toLowerCase();
    const resultDiv = document.getElementById('search-result');
    resultDiv.textContent = 'Loading...';

    if (!pokemonInput) {
        resultDiv.textContent = 'Gebe den Name oder ID ein.';
        return;
    }

    fetch(`${API}/api/pokemon/${pokemonInput}`)
        .then(res => {
            if (!res.ok) throw new Error('Pokemon nicht gefunden');
                return res.json();
            })
        .then(p => {
            let txt = `Name: ${capitalize(p.name)}` + '\n';
            const types = p.types.map(t => capitalize(t.type.name)).join(', ');
            txt += `Types: ${types}\n`;
            p.stats.forEach(s => {
                const statName = s.stat.name;
                const value = s.base_stat;
                if (['hp', 'attack', 'defense'].includes(statName)) {
                    txt += `${capitalize(statName)}: ${value}\n`;
                }
            });
            resultDiv.innerText = txt;                                       
        })
        .catch(err => {
            resultDiv.textContent = 'Fehler: ' + err.message;
    });
}


// Ins Team hinzufügen
function addInTeam() {
    const resultDiv = document.getElementById('search-result');

    if(!pokemonInput) {
        resultDiv.textContent = 'Gebe den Name oder ID ein.';
        return;
    }

    fetch(`${API}/api/team`, {
        method: 'POST',
        headers: { 'Content-Type': 'application-json'},
        body: JSON.stringify({ nameOrId: pokemonInput })
    })
        .then(res => {
            if (!res.ok) {
                return res.json()
                        .then(e => Promise.reject(e.error || "Fehler beim Adding"));
            }
            return res.json();
        })
        .then(p => {
            resultDiv.textContent = `Pokemon ${capitalize(p.name)} hinzugefügt.`;
            loadPlayersList();
            document.getElementById('search-input').value = '';
        })
        .catch(err => {
            resultDiv.textContent = 'Fehler: ' + err.message;
        });
    pokemonInput = '';
}

// Suche nach Typ
function findByType() {
    const type = document.getElementById('type-input').value.trim().toLowerCase();
    const list = document.getElementById('type-list');
    list.innerHTML = '<li>Loading...';

    if(!type) {
        list.innerHTML = '<li>Gebe den Pokemon-Typ ein.';
        return;    
    }

    fetch(`${API}/api/type/${type}`)
        .then(res => {
            if(!res.ok) throw new Error('Typ nicht gefunden.');
                return res.json();
            })
        .then(arr => {
            list.innerHTML = '';
            if (arr.length === 0) {
                list.innerHTML = '<li>Keine Treffer gefunden';
                return;
            }
            arr.forEach(name => {
                const li = document.createElement('li');
                li.textContent = capitalize(name);
                list.append(li);
            });
            document.getElementById('type-input').value = '';
        })
        .catch(err => {
            list.innerHTML = `<li>Fehler + ${err.message}`;
    });
}

function findWeaks() {
    const strongWeakType = document.getElementById('strongs-weaks-input').value.trim().toLowerCase();
    const strongWeakList = document.getElementById('s-list');
    const strongWeakResult = document.getElementById('s-result');
    strongWeakList.innerHTML = "<li>Loading...";

    if (!strongWeakType) {
        strongWeakList.innerHTML = '<li>Gebe den Pokemon-Typ ein.';
        return; 
    }

    fetch(`${API}/api/typeinfo/weaks/${strongWeakType}`)
        .then(res => {
            if (!res.ok) throw new Error('Typ nicht gefunden.');
                return res.json();
            })
        .then(arr => {
            if (!arr || arr.length === 0) {
                strongWeakList.innerHTML = '<li>Keine Treffer gefunden';
                return;
            }
            strongWeakResult.textContent = `Typ "${strongWeakType}" richtet doppelten Schaden an:`;
            strongWeakList.innerHTML = '';
            arr.forEach(t => {
                const li = document.createElement('li');
                li.textContent = capitalize(t);
                strongWeakList.append(li);
            })
        })
        .catch(err => {
            strongWeakList.innerHTML = `<li>Fehler: ${err.message}`;
        });
}

function findStrongs() {
    const strongWeakType = document.getElementById('strongs-weaks-input').value.trim().toLowerCase();
    const strongWeakList = document.getElementById('s-list');
    const strongWeakResult = document.getElementById('s-result');
    strongWeakList.innerHTML = "<li>Loading...";

    if (!strongWeakType) {
        strongWeakList.innerHTML = '<li>Gebe den Pokemon-Typ ein.';
        return; 
    }

    fetch(`${API}/api/typeinfo/strongs/${strongWeakType}`)
        .then(res => {
            if (!res.ok) throw new Error('Typ nicht gefunden.');
                return res.json();
        })
        .then(arr => {
            if (!arr || arr.length === 0) {
                strongWeakList.innerHTML = '<li>Keine Treffer gefunden';
                return;
            }
            strongWeakResult.textContent = `Typ "${strongWeakType}" erleidet doppelten Schaden durch:`;
            strongWeakList.innerHTML = '';
            arr.forEach(t => {
                const li = document.createElement('li');
                li.textContent = capitalize(t);
                strongWeakList.append(li);
            })
        })
        .catch(err => {
            strongWeakList.innerHTML = `<li>Fehler: ${err.message}`;
        });
}

// Players anzeigen
function loadPlayersList() {
    const list = document.getElementById('players-list');
    list.innerHTML = '<li>Loading...';

    fetch(`${API}/api/team`)
        .then(res => {
            console.log(res);
            if(!res.ok) throw new Error('Fehler beim Players-Liste Anzeigen');
                return res.json();
            })
        .then(arr => {
            list.innerHTML = '';
            let textHtml = '';
            if (!arr || arr.length === 0) {
                list.innerHTML = '<li>Dein Team ist leer.';
                return;
            }
            arr.forEach(p => {
                textHtml += `<li>${capitalize(p.name)}</li>`;
            });
            list.innerHTML = textHtml;
        })
        .catch(err => {
            list.innerHTML = `<li>Fehler + ${err.message}`;
        });
}

// Teamliste anzeigen
function loadTeamsList() {
    const teamList = document.getElementById('teams-list');
    teamList.innerHTML = '<li>Loading...';

    fetch(`${API}/api/team/l`)
        .then(res => {
            console.log(res);
            if (!res.ok) throw new Error('Fehler beim Teamsliste Anzeigen');
            return res.json();
        })
        .then(arr =>{
            teamList.innerHTML = '';
            console.log(arr);
                    
            if (!arr || arr.length === 0) {
                teamList.innerHTML = '<li>Du hast keine Teams</li>';
                return;
            }
            arr.forEach(t => {
                const li = document.createElement('li');
                li.textContent = t;
                teamList.append(li);
            })
        })
        .catch(err =>{
            teamList.innerHTML = `<li>Fehler + ${err.message}`;
        });
}

// Player Löschen
function deletePlayer() {
    const resultSpan = document.getElementById('delete-result');
    const inputDelete = document.getElementById('delete-name').value.trim().toLowerCase();

    if(!inputDelete) {
        resultSpan.textContent = 'Gebe den Name oder ID ein.';
        return;
    }

    fetch(`${API}/api/pokemon/delete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nameOrId: inputDelete })
    })
    .then(res => {
        if (!res.ok) return new Error("Fehler beim Löschen");
        return res.json();
    })
    .then(p => {

        resultSpan.textContent = p instanceof Error 
                ? "Pokemon nicht gefunden" : capitalize(p.result);
        document.getElementById('delete-name').value = '';
        loadPlayersList();
    })
    .catch(err => {
        resultSpan.textContent = 'Fehler: ' + err;
    });
}

// Team speichern
function saveTeam() {
    const name = document.getElementById('save-filename').value.trim().toLowerCase();
    const resultDiv = document.getElementById('save-result');

    if (!name) {
        resultDiv.textContent = 'Gebe Teamname ein';
        return;
    }

    fetch(`${API}/api/teams/${name}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    })
        .then(res => {
            if (!res.ok) return res.json().then(
                e => Promise.reject(e.error || 'Fehler')
            );
        })
        .then(data => {
            document.getElementById('save-filename').value = '';
            resultDiv.textContent = `Als "${name}" in DB(JS) gespeichert`;
        })
        .catch(err => {
            resultDiv.textContent = `Fehler: ` + err;
    })
}

// Team laden
function loadTeam() {
    const teamName = document.getElementById('team-name').value.trim().toLowerCase();
    const resultDiv = document.getElementById('load-result');

    if (!teamName) {
        resultDiv.textContent = 'Gebe Dateiname ein';
        return;
    }

    fetch(`${API}/api/teams/${teamName}`)
        .then(res => {
            if (!res.ok) throw new Error("Team nicht gefunden");
            return res.json();
        })
        .then(arr => {
            resultDiv.textContent = `Team wurde aus ${teamName} geladen`;
            loadPlayersList();
            document.getElementById('team-name').value = '';
        }) 
        .catch (err => {
            resultDiv.textContent = 'Fehler: ' + err;
    });
}

function deleteTeam() {
    const teamName = document.getElementById('team-name').value.trim().toLowerCase();
    const resultDiv = document.getElementById('load-result');

    if(!teamName) {
        resultDiv.textContent = 'Gebe den Name ein.';
        return;
    }

    fetch(`${API}/api/teams/${teamName}`, {
        method: 'DELETE',
        /*
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: teamName})*/
    })
        .then(res => {
            if (!res.ok) return res.json().then(
                                e => Promise.reject(e.error || 'Fehler'));
                return res.json();
        })
        .then(data => {
            resultDiv.textContent = `Team "${teamName}" wurde gelöscht`;
            loadTeamsList();
            document.getElementById('team-name').value = '';
        })

}

function analyzeTeamWeakness() {
    const resultDiv = document.getElementById('strength-result');
    const resList = document.getElementById('strength-list');

    resultDiv.textContent = 'Loading...';

    fetch(`${API}/api/team/weakness`)
        .then(res => {
            if (!res.ok) throw new Error("Kann keine Daten abrufen");
                return res.json();
        })
        .then(map => {
            const entries = Object.entries(map);
            if (entries.length === 0) {
                resultDiv.textContent = 'Team ist leer';
                return;
            }
            resultDiv.textContent = 'Dein Team ist schwach gegen Typen:';
            resList.innerHTML = '';
            
            entries.forEach(([type, count]) => {
                const li = document.createElement('li');
                li.textContent = `${capitalize(type)}: ${count}`;
                resList.append(li);
            });
        })
        .catch(err => {
            resultDiv.textContent = 'Fehler: '+ err.message;
        });
}

function analyzeTeamStrength() {
    const resultDiv = document.getElementById('strength-result');
    const resList = document.getElementById('strength-list');

    resultDiv.textContent = 'Loading...';

    fetch(`${API}/api/team/strength`)
        .then(res => {
            if (!res.ok) throw new Error("Kann keine Daten abrufen");
                return res.json();
        })
        .then(map => {
            const entries = Object.entries(map);
            if (entries.length === 0) {
                resultDiv.textContent = 'Team ist leer';
                return;
            }
            resultDiv.textContent = 'Dein Team ist stark gegen Typen:';
            resList.innerHTML = '';
            
            entries.forEach(([type, count]) => {
                const li = document.createElement('li');
                li.textContent = `${capitalize(type)}: ${count}`;
                resList.append(li);
            });
        })
        .catch(err => {
            resultDiv.textContent = 'Fehler: '+ err.message;
        });
}

// schreibt das Wort mit einem Großbuchstaben
function capitalize(s) {
    if (!s) return s;
    return s.charAt(0).toUpperCase() + s.slice(1);
}