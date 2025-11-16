// Crie em: src/main/resources/static/js/login.js
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    if (!loginForm) return;

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const senha = document.getElementById('senha').value;
        const errorMessage = document.getElementById('error-message');
        errorMessage.textContent = ''; // Limpa erros antigos

        try {
            const response = await fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: email, senha: senha })
            });

            if (!response.ok) {
                throw new Error('Email ou senha inválidos.');
            }

            const data = await response.json();
            localStorage.setItem('jwt_token', data.token);
            window.location.href = '/home'; // Redireciona

        } catch (error) {
            errorMessage.textContent = error.message;
        }
    });
});