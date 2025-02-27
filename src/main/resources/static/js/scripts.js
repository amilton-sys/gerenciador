document.addEventListener("DOMContentLoaded", function () {
    // Elementos da página
    const formAddAmount = document.getElementById("formAddAmount");
    const formAddExoense = document.getElementById("despesaForm");
    const inputAmount = document.getElementById("valorAmount");
    const errorMessage = document.getElementById("error-message");
    const salaryAmount = document.getElementById("salaryAmount");
    const debtsAmount = document.getElementById("addDebts");
    const leftoversAmount = document.getElementById("addLeftovers");
    const skeleton = document.querySelectorAll("#skeleton");

    const modalAdd = document.getElementById("addModal");
    const modalEdit = document.getElementById("edit");
    const modalAddAmount = document.getElementById("addAmountModal");

    const btnAdd = document.querySelector("#add");
    const btnAddAmount = document.querySelector("#addAmount");
    const btnCloseModals = document.querySelectorAll(".close");

    // Inicializa o salário
    fetchSalary();
    // Inicializa o débitos
    fetchDebts();
    // Inicializa o sobras
    fetchLeftTovers();

    // Evento para submissão do formulário
    formAddAmount.addEventListener("submit", function (event) {
        event.preventDefault();
        handleAddAmount();
    });

    // Evento para submissão do formulário
    formAddExoense.addEventListener("submit", function (event) {
        event.preventDefault();
        handleAddAmount();
    });

    // Eventos para abrir e fechar modais
    setupModalEvents();

    // Eventos para edição
    setupEditEvents();

    // Função para exibir ou ocultar o loader do salário
    function toggleSkeletonLoader(isLoading) {
        salaryAmount.style.display = isLoading ? "none" : "block";
        debtsAmount.style.display = isLoading ? "none" : "block";
        leftoversAmount.style.display = isLoading ? "none" : "block";
        skeleton.forEach(sk => {
            sk.style.display = isLoading ? "block" : "none";
        })
    }

    // Função para buscar o salário no servidor
    function fetchSalary() {
        toggleSkeletonLoader(true);
        fetch("/getSalary")
            .then(response => response.json())
            .then(data => {
                const salary = data.salary || 0.00;
                setSalaryDisplay(salary);
            })
            .catch(error => {
                console.error("Erro ao obter salário:", error);
                salaryAmount.textContent = "Erro ao carregar";
            })
            .finally(() => toggleSkeletonLoader(false));
    }

    // Função para buscar as dividas no servidor
    function fetchDebts() {
        toggleSkeletonLoader(true);
        fetch("/getDebts")
            .then(response => response.json())
            .then(data => {
                const debt = data.debts || 0.00;
                setDebtDisplay(debt);
            })
            .catch(error => {
                console.error("Erro ao obter débitos:", error);
            })
            .finally(() => toggleSkeletonLoader(false));
    }

    // Função para buscar as sobras no servidor
    function fetchLeftTovers() {
        toggleSkeletonLoader(true);
        fetch("/getleftovers")
            .then(response => response.json())
            .then(data => {
                const leftovers = data.leftovers || 0.00;
                setLeftOversDisplay(leftovers);
            })
            .catch(error => {
                console.error("Erro ao obter sobras:", error);
            })
            .finally(() => toggleSkeletonLoader(false));
    }


    // Função para formatar e exibir o salário
    function setSalaryDisplay(value) {
        salaryAmount.textContent = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    }

    // Função para formatar e exibir o salário
    function setDebtDisplay(value) {
        debtsAmount.textContent = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    }

     // Função para formatar e exibir o salário
     function setLeftOversDisplay(value) {
        leftoversAmount.textContent = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    }

    // Função para processar o envio do formulário de adição de amount
    function handleAddAmount() {
        const amountStr = inputAmount.value.trim();

        if (!isValidAmount(amountStr)) {
            showError("Por favor, insira um valor numérico válido.");
            return;
        }

        fetch("/addAmount", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ amountStr })
        })
            .then(response => {
                if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
                return response.text().then(text => text ? JSON.parse(text) : {});
            })
            .then(result => {
                if (result.success) {
                    fetchSalary();
                    fetchDebts();
                    fetchLeftTovers();
                    closeModal();
                } else {
                    showError(result.error || "Erro ao adicionar o salário.");
                }
            })
            .catch(error => {
                console.error("Erro ao enviar:", error);
                showError("Erro inesperado ao tentar adicionar o salário.");
            });
    }


    // Função para processar o envio do formulário de adição de despesa
    function handleAddAmount() {
        const amountStr = inputAmount.value.trim();

        if (!isValidAmount(amountStr)) {
            showError("Por favor, insira um valor numérico válido.");
            return;
        }

        fetch("/addAmount", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ amountStr })
        })
            .then(response => {
                if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
                return response.text().then(text => text ? JSON.parse(text) : {});
            })
            .then(result => {
                if (result.success) {
                    fetchSalary();
                    fetchDebts();
                    fetchLeftTovers();
                    closeModal();
                } else {
                    showError(result.error || "Erro ao adicionar o salário.");
                }
            })
            .catch(error => {
                console.error("Erro ao enviar:", error);
                showError("Erro inesperado ao tentar adicionar o salário.");
            });
    }

    // Função para validar entrada numérica
    function isValidAmount(value) {
        return /^[0-9]+(\.[0-9]{1,2})?$/.test(value);
    }

    // Função para exibir erro
    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = "flex";
    }

    // Função para fechar modais
    function closeModal() {
        modalAddAmount.style.display = "none";
        formAddAmount.reset();
        errorMessage.style.display = "none";
    }

    // Configura eventos dos modais
    function setupModalEvents() {
        if (btnAddAmount) {
            btnAddAmount.addEventListener("click", () => {
                modalAddAmount.style.display = "flex";
            });
        }

        if (btnAdd) {
            btnAdd.addEventListener("click", () => {
                modalAdd.style.display = "flex";
            });
        }

        btnCloseModals.forEach(btn => {
            btn.addEventListener("click", closeAllModals);
        });

        window.addEventListener("click", event => {
            if (event.target === modalAdd || event.target === modalAddAmount) {
                closeAllModals();
            }
        });
    }

    // Fecha todos os modais
    function closeAllModals() {
        modalAdd.style.display = "none";
        modalAddAmount.style.display = "none";
        modalEdit.style.display = "none";
    }

    // Configura eventos de edição
    function setupEditEvents() {
        const editButtons = document.querySelectorAll(".edit");

        editButtons.forEach(edit => {
            edit.addEventListener("click", function () {
                populateEditModal(edit);
                modalEdit.style.display = "flex";
            });
        });
    }

    // Preenche o modal de edição com os dados selecionados
    function populateEditModal(edit) {
        document.getElementById("idUpdate").value = edit.getAttribute("data-id");
        document.getElementById("nomeUpdate").value = edit.getAttribute("data-name");
        document.getElementById("valorUpdate").value = edit.getAttribute("data-amount");
        document.getElementById("dateUpdate").value = edit.getAttribute("data-date");
        document.getElementById("situacaoUpdate").value =
            edit.getAttribute("data-situacao") === "PAGO" ? "Pago" : "A Pagar";
    }
});
