// Substitua em: src/main/resources/static/js/registrar.js
document.addEventListener('DOMContentLoaded', () => {
    const registroForm = document.getElementById('registro-form');
    if (!registroForm) return;

    registroForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const nome = document.getElementById('nome').value;
        const email = document.getElementById('email').value;
        const senha = document.getElementById('senha').value;
        const grupoId = document.getElementById('grupoId').value; // <-- CAMPO NOVO
        const messageDiv = document.getElementById('form-message');

        messageDiv.textContent = 'Registrando...';
        messageDiv.className = 'form-message';

        // Validação simples do frontend
        if (!grupoId) {
            messageDiv.className = 'form-message error';
            messageDiv.textContent = 'Por favor, selecione um tipo de conta.';
            return;
        }

        try {
            const response = await fetch('/registrar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                // --- CORREÇÃO AQUI: Envia o grupoId ---
                body: JSON.stringify({
                    nome: nome,
                    email: email,
                    senha: senha,
                    grupoId: parseInt(grupoId) // <-- CAMPO NOVO
                })
            });

            const responseText = await response.text();

            if (!response.ok) {
                try {
                    const errorData = JSON.parse(responseText);
                    throw new Error(errorData.message || 'Erro ao registrar.');
                } catch(e) {
                    throw new Error(responseText);
                }
            }

            messageDiv.className = 'form-message success';
            messageDiv.textContent = 'Registro efetuado com sucesso! Redirecionando para o login...';

            setTimeout(() => {
                window.location.href = '/';
            }, 3000);

        } catch (error) {
            messageDiv.className = 'form-message error';
            messageDiv.textContent = error.message;
        }
    });
});