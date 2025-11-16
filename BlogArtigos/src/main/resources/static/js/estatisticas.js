// Crie em: src/main/resources/static/js/estatisticas.js
document.addEventListener('DOMContentLoaded', () => {

    async function carregarEstatisticas() {
        const tbody = document.getElementById('stats-body');
        tbody.innerHTML = '<tr><td colspan="5">Carregando...</td></tr>';

        try {
            const response = await fetch('/estatisticas', { method: 'GET' });
            if (!response.ok) {
                throw new Error('Falha ao buscar estatísticas.');
            }

            const dados = await response.json();
            tbody.innerHTML = ''; // Limpa o "Carregando..."

            if (dados.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5">Nenhum autor encontrado.</td></tr>';
                return;
            }

            dados.forEach(autor => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                        <td>${autor.usuarioId}</td>
                        <td>${autor.autorNome}</td>
                        <td>${autor.email}</td>
                        <td>${autor.nomeGrupo}</td>
                        <td>${autor.totalArtigos}</td>
                    `;
                tbody.appendChild(tr);
            });

        } catch (error) {
            console.error(error.message);
            tbody.innerHTML = `<tr><td colspan="5" style="color: red;">${error.message}</td></tr>`;
        }
    }

    carregarEstatisticas();
});