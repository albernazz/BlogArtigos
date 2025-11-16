// Crie em: src/main/resources/static/js/editar.js
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = '/';
        return;
    }

    const artigoId = document.body.dataset.artigoId;
    const form = document.getElementById('editar-artigo-form');
    const tituloInput = document.getElementById('titulo');
    const conteudoInput = document.getElementById('conteudo');
    const messageDiv = document.getElementById('form-message');

    // 1. Busca os dados atuais do artigo
    async function carregarArtigo() {
        try {
            const response = await fetch(`/artigos/${artigoId}`, { method: 'GET' });
            if (!response.ok) { throw new Error('Artigo não encontrado.'); }

            const artigo = await response.json();

            tituloInput.value = artigo.titulo;
            conteudoInput.value = artigo.conteudo;
            messageDiv.textContent = ''; // Limpa "Carregando..."

        } catch (error) {
            messageDiv.className = 'form-message error';
            messageDiv.textContent = error.message;
        }
    }

    // 2. Envia as alterações (UPDATE)
    form.addEventListener('submit', async function(event) {
        event.preventDefault();
        messageDiv.textContent = 'Salvando...';
        messageDiv.className = 'form-message';

        const body = {
            titulo: tituloInput.value,
            conteudo: conteudoInput.value
        };

        try {
            const response = await fetch(`/artigos/${artigoId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });

            if (response.status === 403) { throw new Error('Acesso negado. Você não tem permissão de AUTOR.'); }
            if (!response.ok) { throw new Error('Erro ao salvar alterações.'); }

            // --- CORREÇÃO DA MENSAGEM ---
            // Removemos a gíria de dev "Triggers disparadas"
            messageDiv.className = 'form-message success';
            messageDiv.textContent = 'Artigo atualizado com sucesso!';
            // --- FIM DA CORREÇÃO ---

            setTimeout(() => { window.location.href = '/home'; }, 2000);

        } catch (error) {
            messageDiv.className = 'form-message error';
            messageDiv.textContent = error.message;
        }
    });

    carregarArtigo();
});