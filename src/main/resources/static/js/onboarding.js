document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.feel-card');
    cards.forEach(card => {
        card.addEventListener('click', () => {
            cards.forEach(c => c.classList.remove('active'));
            card.classList.add('active');
            card.querySelector('input').checked = true;
        });
    });
});