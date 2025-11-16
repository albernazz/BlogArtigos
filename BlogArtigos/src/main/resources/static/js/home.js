document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt_token');

    // 1. Verifica se o utilizador está logado
    if (!token) {
        window.location.href = '/';
        return;
    }

    // --- Elementos do DOM ---
    const logoutButton = document.getElementById('logout-button');
    const novoArtigoFormContainer = document.getElementById('form-novo-artigo'); // Pega o container
    const novoArtigoForm = document.getElementById('novo-artigo-form');
    const searchBar = document.getElementById('search-bar');
    const artigosLista = document.getElementById('artigos-lista');
    const statsLink = document.getElementById('stats-link');
    const toggleFormBtn = document.getElementById('toggle-form-btn'); // Botão de toggle

    let debounceTimer;

    // --- Decodificador de Token ---
    function getUsuarioInfoFromToken(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return {
                id: payload.id,
                roles: payload.scope || []
            };
        } catch (e) {
            console.error('Token inválido:', e);
            localStorage.removeItem('jwt_token');
            window.location.href = '/';
            return null;
        }
    }

    const usuarioLogado = getUsuarioInfoFromToken(token);
    if (!usuarioLogado) return;

    // --- Lógica de UI (Visibilidade) ---
    function setupUIBasedOnRoles() {
        // Mostra/esconde link de Stats
        if (usuarioLogado.roles.includes('ROLE_ADMIN')) {
            statsLink.style.display = 'inline-block';
        } else {
            statsLink.style.display = 'none';
        }

        // Mostra/esconde botão de publicar
        if (usuarioLogado.roles.includes('ROLE_AUTOR') || usuarioLogado.roles.includes('ROLE_ADMIN')) {
            toggleFormBtn.style.display = 'block';
        } else {
            toggleFormBtn.style.display = 'none';
        }

        // Listener para o botão de toggle do formulário
        toggleFormBtn.addEventListener('click', () => {
            const isHidden = novoArtigoFormContainer.style.display === 'none';
            novoArtigoFormContainer.style.display = isHidden ? 'block' : 'none';
            toggleFormBtn.textContent = isHidden ? 'Esconder Formulário -' : 'Publicar Novo Artigo +';
        });
    }

    // --- Logout (Restaurado) ---
    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('jwt_token');
        window.location.href = '/';
    });

    // --- Lógica de Busca (Restaurada) ---
    searchBar.addEventListener('input', (event) => {
        clearTimeout(debounceTimer);
        const termo = event.target.value;
        debounceTimer = setTimeout(() => {
            carregarArtigos(termo);
        }, 500);
    });

    // --- Lógica de Novo Artigo (Restaurada) ---
    novoArtigoForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const titulo = document.getElementById('titulo').value;
        const conteudo = document.getElementById('conteudo').value;
        const categoriaId = document.getElementById('categoriaId').value;
        const formError = document.getElementById('form-error');
        formError.textContent = '';
        if (!categoriaId) {
            formError.textContent = "Por favor, selecione uma categoria.";
            return;
        }
        const body = {
            titulo: titulo,
            conteudo: conteudo,
            usuarioId: usuarioLogado.id,
            categoriaId: parseInt(categoriaId)
        };
        try {
            const response = await fetch('/artigos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });
            if (response.status === 403) { throw new Error('Acesso negado. Você não tem permissão de AUTOR.'); }
            if (!response.ok) { throw new Error('Erro ao publicar o artigo.'); }

            novoArtigoForm.reset();
            document.getElementById('categoriaId').selectedIndex = 0;
            // Esconde o formulário e recarrega
            novoArtigoFormContainer.style.display = 'none';
            toggleFormBtn.textContent = 'Publicar Novo Artigo +';
            await carregarArtigos();
        } catch (error) {
            formError.textContent = error.message;
        }
    });

    // --- Carregador de Categorias (Restaurado) ---
    async function carregarCategorias() {
        const selectCategoria = document.getElementById('categoriaId');
        try {
            const response = await fetch('/categorias', { method: 'GET' });
            if (!response.ok) { throw new Error('Falha ao carregar categorias.'); }
            const categorias = await response.json();
            selectCategoria.innerHTML = '';
            const defaultOption = document.createElement('option');
            defaultOption.value = "";
            defaultOption.textContent = "Selecione uma categoria";
            defaultOption.disabled = true;
            defaultOption.selected = true;
            selectCategoria.appendChild(defaultOption);
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;
                option.textContent = categoria.nome;
                selectCategoria.appendChild(option);
            });
        } catch (error) {
            console.error(error.message);
            selectCategoria.innerHTML = '<option value="">Erro ao carregar</option>';
        }
    }

    // --- Carregador Principal de Artigos (COM LÓGICA DE DELETE) ---
    async function carregarArtigos(termoDeBusca = "") {
        artigosLista.innerHTML = '<p>Carregando...</p>';
        let url = '/artigos';
        if (termoDeBusca) {
            url = `/artigos?busca=${encodeURIComponent(termoDeBusca)}`;
        }

        try {
            const response = await fetch(url, { method: 'GET' });
            const artigos = await response.json();
            artigosLista.innerHTML = '';
            if (artigos.length === 0) {
                artigosLista.innerHTML = '<p>Nenhum artigo encontrado.</p>';
                return;
            }

            artigos.forEach(artigo => {
                const div = document.createElement('div');
                div.className = 'artigo';

                // Lógica de botões (Editar e Apagar)
                let botoesHtml = '';
                const isDono = usuarioLogado.id === artigo.usuarioId;
                const isAdmin = usuarioLogado.roles.includes('ROLE_ADMIN');

                // Só o dono pode editar
                if (isDono) {
                    botoesHtml += `<a href="/editar/${artigo.artigoId}" class="edit-button">Editar</a>`;
                }
                // O Dono OU o Admin podem apagar
                if (isDono || isAdmin) {
                    botoesHtml += `<button class="delete-button" data-artigo-id="${artigo.artigoId}">Apagar</button>`;
                }

                div.innerHTML = `
                    ${botoesHtml} <span class="artigo-categoria">${artigo.categoriaNome}</span> 
                    <h3>${artigo.titulo}</h3>
                    <p>${artigo.conteudo.substring(0, 150)}...</p>
                    <p class="artigo-meta">Por: <strong>${artigo.autorNome}</strong></p>
                    
                    <div class="comentarios-secao">
                        <div class="comentarios-lista" id="comentarios-${artigo.artigoId}">
                            <p>Carregando comentários...</p>
                        </div>
                        <form class="comentario-form" data-artigo-id="${artigo.artigoId}">
                            <textarea class="comentario-texto" rows="3" placeholder="Escreva um comentário..." required></textarea>
                            <button type="submit">Comentar</button>
                        </form>
                    </div>
                `;

                artigosLista.appendChild(div);

                // Carrega comentários
                const containerComentarios = div.querySelector(`#comentarios-${artigo.artigoId}`);
                carregarComentarios(artigo.artigoId, containerComentarios);

                // Liga o listener do form de comentário
                div.querySelector('.comentario-form').addEventListener('submit', submeterComentario);

                // --- LISTENER NOVO (Delete) ---
                const deleteBtn = div.querySelector('.delete-button');
                if (deleteBtn) {
                    deleteBtn.addEventListener('click', deletarArtigo);
                }
            });
        } catch (error) {
            artigosLista.innerHTML = '<p style="color: red;">Erro ao carregar artigos.</p>';
        }
    }

    // --- FUNÇÃO NOVA (Delete) ---
    async function deletarArtigo(event) {
        const artigoId = event.target.dataset.artigoId;

        if (!window.confirm("Tem certeza que deseja apagar este artigo?\nEsta ação não pode ser desfeita e apagará todos os comentários.")) {
            return;
        }

        try {
            const response = await fetch(`/artigos/${artigoId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 403) { throw new Error('Acesso negado. Você não tem permissão para apagar este artigo.'); }
            if (response.status === 404) { throw new Error('Artigo não encontrado.'); }
            if (!response.ok) { throw new Error('Erro ao apagar o artigo.'); }

            await carregarArtigos(searchBar.value); // Recarrega a lista (mantendo a busca atual)

        } catch (error) {
            alert(error.message);
        }
    }

    // --- Funções de Comentários (Restauradas) ---
    async function carregarComentarios(artigoId, container) {
        try {
            const response = await fetch(`/comentarios/${artigoId}`, { method: 'GET' });
            if (!response.ok) return;
            const comentarios = await response.json();
            container.innerHTML = '<h4>Comentários:</h4>';
            if (comentarios.length === 0) {
                container.innerHTML += '<p>Seja o primeiro a comentar!</p>';
            }
            comentarios.forEach(comentario => {
                const div = document.createElement('div');
                div.className = 'comentario';
                div.innerHTML = `<p><strong>${comentario.autor}:</strong> ${comentario.texto}</p>`;
                container.appendChild(div);
            });
        } catch (error) {
            container.innerHTML = '<p style="color: red;">Erro ao carregar comentários.</p>';
        }
    }

    async function submeterComentario(event) {
        event.preventDefault();
        const form = event.target;
        const artigoId = form.dataset.artigoId;
        const textoInput = form.querySelector('.comentario-texto');
        const body = {
            artigoId: artigoId,
            texto: textoInput.value
        };
        try {
            const response = await fetch('/comentarios', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });
            if (response.status === 403) { throw new Error('Acesso negado. Faça login para comentar.'); }
            if (!response.ok) { throw new Error('Erro ao enviar comentário.'); }
            form.reset();
            const container = form.closest('.comentarios-secao').querySelector('.comentarios-lista');
            await carregarComentarios(artigoId, container);
        } catch (error) {
            alert(error.message);
        }
    }

    // --- Inicialização da Página ---
    setupUIBasedOnRoles();
    carregarArtigos();
    carregarCategorias();
});