const API = '';
let pokemonInput = '';
        
        // Suche Pokemon nach Name oder ID
        document.getElementById('search-btn').addEventListener('click', () => {
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
        });

        // Ins Team hinzufügen
        document.getElementById('add-btn').addEventListener('click', () => {
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
                    loadTeamList();
                    document.getElementById('search-input').value = '';
                })
                .catch(err => {
                    resultDiv.textContent = 'Fehler: ' + err.message;
                });
            pokemonInput = '';    
        });

        // Suche nach Typ
        document.getElementById('type-btn').addEventListener('click', () => {
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
        });
        

        // Team anzeigen
        document.getElementById('team-btn').addEventListener('click', () => {
            loadTeamList();

        });

        function loadTeamList() {
            const list = document.getElementById('team-list');
            list.innerHTML = '<li>Loading...';

            fetch(`${API}/api/team`)
                .then(res => {
                    if(!res.ok) throw new Error('Fehler beim Team Anzeigen');
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


        // Player Löschen
        document.getElementById('delete-btn').addEventListener('click', () => {
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
                console.log(p);
                resultSpan.textContent = p instanceof Error 
                        ? "Pokemon nicht gefunden" : capitalize(p.result);
                document.getElementById('delete-name').value = '';
                loadTeamList();
            })
            .catch(err => {
                resultSpan.textContent = 'Fehler: ' + err;
            });
        });


        // Team speichern
        document.getElementById('save-btn').addEventListener('click', () => {
            const filename = document.getElementById('save-filename').value.trim();
            const resultDiv = document.getElementById('save-result');

            if (!filename) {
                resultDiv.textContent = 'Gebe Dateiname ein';
                return;
            }

            fetch(`${API}/api/team/save`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ filename })
            })
                .then(res => {
                    if (!res.ok) return res.json().then(
                        e => Promise.reject(e.error || 'Fehler')
                    );
                })
                .then(data => {
                    document.getElementById('save-filename').value = '';
                    resultDiv.textContent = `Als "${filename}.json" gespeichert`;
                })
                .catch(err => {
                    resultDiv.textContent = `Fehler: ` + err;
                })
        });

        document.getElementById('load-btn').addEventListener('click', () => {
            const filename = document.getElementById('load-filename').value.trim().toLowerCase();
            const resultDiv = document.getElementById('load-result');

            if (!filename) {
                resultDiv.textContent = 'Gebe Dateiname ein';
                return;
            }

            fetch(`${API}/api/team/load`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ filename })
            })
                .then(res => {
                    if (!res.ok) return res.json().then(
                                        e => Promise.reject(e.error || 'Fehler'));
                    return res.json();
                })
                .then(data => {
                    resultDiv.textContent = `Team wurde aus ${filename} geladen`;
                    loadTeamList();
                }) 
                .catch (err => {
                    resultDiv.textContent = 'Fehler: ' + err;
                });
        });
        

        // schreibt das Wort mit einem Großbuchstaben
        function capitalize(s) {
            if (!s) return s;
            return s.charAt(0).toUpperCase() + s.slice(1);
        }