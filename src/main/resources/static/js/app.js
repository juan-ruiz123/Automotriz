document.addEventListener('DOMContentLoaded', () => {
    // 1. Persistencia de Modo Oscuro / Claro
    const htmlEl = document.documentElement;
    const savedTheme = localStorage.getItem('taller-theme') || 'light';
    htmlEl.setAttribute('data-theme', savedTheme);
    actualizarIconoTema(savedTheme);

    const btnTheme = document.getElementById('btnToggleTheme');
    if (btnTheme) {
        btnTheme.addEventListener('click', () => {
            const current = htmlEl.getAttribute('data-theme');
            const next = current === 'dark' ? 'light' : 'dark';
            htmlEl.setAttribute('data-theme', next);
            localStorage.setItem('taller-theme', next);
            actualizarIconoTema(next);
        });
    }

    function actualizarIconoTema(theme) {
        const icon = document.getElementById('themeIcon');
        if (!icon) return;
        icon.className = theme === 'dark' ? 'bi bi-sun-fill text-warning' : 'bi bi-moon-stars-fill';
    }

    // 2. Persistencia de Barra Lateral Plegable
    const sidebar = document.getElementById('sidebar');
    const btnSidebar = document.getElementById('btnToggleSidebar');
    if (sidebar && localStorage.getItem('taller-sidebar') === 'collapsed') {
        sidebar.classList.add('collapsed');
    }
    if (sidebar && btnSidebar) {
        btnSidebar.addEventListener('click', () => {
            sidebar.classList.toggle('collapsed');
            localStorage.setItem('taller-sidebar', sidebar.classList.contains('collapsed') ? 'collapsed' : 'expanded');
        });
    }

    // 3. Auto-cierre de notificaciones Toast
    document.querySelectorAll('.toast').forEach(toastEl => {
        const toast = new bootstrap.Toast(toastEl, { delay: 4500 });
        toast.show();
    });

    // 4. Modal de Confirmación Personalizado
    const modalEl = document.getElementById('confirmActionModal');
    if (modalEl) {
        const confirmModal = new bootstrap.Modal(modalEl);
        const modalMessage = document.getElementById('confirmModalMessage');
        const btnConfirm = document.getElementById('confirmModalBtn');

        document.querySelectorAll('[data-confirm-url]').forEach(trigger => {
            trigger.addEventListener('click', (e) => {
                e.preventDefault();
                modalMessage.textContent = trigger.getAttribute('data-confirm-text') || '¿Está seguro de realizar esta acción?';
                btnConfirm.setAttribute('href', trigger.getAttribute('data-confirm-url'));
                confirmModal.show();
            });
        });
    }

    // 5. Filtro dinámico instantáneo en tablas
    const searchInput = document.getElementById('tableSearchInput');
    const targetTable = document.getElementById('dataTable');
    if (searchInput && targetTable) {
        searchInput.addEventListener('keyup', () => {
            const term = searchInput.value.toLowerCase().trim();
            const rows = targetTable.querySelectorAll('tbody tr[data-searchable="true"]');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(term) ? '' : 'none';
            });
        });
    }
});