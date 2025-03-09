import { FormValidator } from "./validator.js";

document.addEventListener("DOMContentLoaded", function () {
    // Elementos da página
    const formAddAmount = document.getElementById("formAddAmount");
    const formAddExpense = document.getElementById("despesaForm");
    const formEditExpense = document.getElementById("editExpense");

    const inputAmount = document.getElementById("valorAmount");
    const salaryAmount = document.getElementById("salaryAmount");
    const debtsAmount = document.getElementById("addDebts");
    const leftoversAmount = document.getElementById("addLeftovers");

    const skeleton = document.querySelectorAll(".skeleton-loader");
    const errorMessage = document.querySelector(".error");


    const modalAdd = document.getElementById("addModal");
    const modalEdit = document.querySelector("#edit");
    const modalAddAmount = document.getElementById("addAmountModal");

    const btnAdd = document.querySelector("#add");
    const btnAddAmount = document.querySelector("#addAmount");
    const btnCloseModals = document.querySelectorAll(".close");

    if (salaryAmount) {
        // Inicializa o salário
        fetchSalary();
        // Inicializa o débitos
        fetchDebts();
        // Inicializa o sobras
        fetchLeftTovers();
    }

    fetchTotalShopping();

    if (salaryAmount) {
        // Evento para submissão do formulário
        const validator = new FormValidator(formAddAmount);
        formAddAmount.addEventListener("submit", function (event) {
            if (validator.isFormValid()) {
                handleAddAmount();
            }
            event.preventDefault();
        });

        formAddAmount.addEventListener("input", function (event) {
            if (event.target.dataset.validate) {
                validator.validateField(event.target);
            }
        });

        // Evento para submissão do formulário
        formAddExpense.addEventListener("submit", (event) => {
            event.preventDefault();
            handleAddExpense();
        });

        formEditExpense.addEventListener("submit", (event) => {
            event.preventDefault();
            handleUpdateExpense();
        })
    }

    // Eventos para abrir e fechar modais
    setupModalEvents();

    // Eventos para edição
    setupEditEvents();

    // Função para exibir ou ocultar o loader do salário
    function toggleSkeletonLoader(isLoading) {
        const elementsToHide = [salaryAmount, debtsAmount, leftoversAmount];
        const displayState = isLoading ? "none" : "block";

        elementsToHide.forEach(el => {
            if (el) el.style.display = displayState;
        });

        skeleton.forEach(sk => {
            sk.style.display = isLoading ? "block" : "none";
        });
    }


    // Função para buscar o salário no servidor
    function fetchSalary() {
        fetchData("/getSalary")
            .then(data => {
                const dados = data.salary || 0.00;
                setAmountDisplay(dados, salaryAmount);
            })
            .catch(error => {
                console.log("Erro ao buscar salary", error);
            })
    }

    function fetchTotalShopping() {
        const totalShoppingAmount = document.getElementById("amountShop");
        fetchData("/getTotalShopping")
            .then(data => {
                const dados = data.totalShopping || 0.00;
                setAmountDisplay(dados, totalShoppingAmount);
            })
            .catch(error => {
                console.log("Erro ao buscar totalShopping", error);
            })
    }

    // Função para buscar as dividas no servidor
    function fetchDebts() {
        fetchData("/getDebts")
            .then(data => {
                const dados = data.debts || 0.00;
                setAmountDisplay(dados, debtsAmount);
            })
            .catch(error => {
                console.log("Erro ao buscar debitos", error);
            })
    }

    // Função para buscar as sobras no servidor
    function fetchLeftTovers() {
        fetchData("/getleftovers")
            .then(data => {
                const dados = data.leftovers || 0.00;
                setAmountDisplay(dados, leftoversAmount);
            })
            .catch(error => {
                console.log("Erro ao buscar sobras", error);
            })
    }


    function fetchData(path) {
        toggleSkeletonLoader(true);
        return fetch(path)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro na requisição: ${response.statusText}`);
                }
                return response.json();
            })
            .catch(error => {
                console.log("Erro ao obter dados:", error);
                return null;
            })
            .finally(() => toggleSkeletonLoader(false));
    }


    // Função para formatar e exibir valores monetários
    function setAmountDisplay(value, element) {
        element.textContent = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    }


    // Função para processar o envio do formulário de adição de amount
    function handleAddAmount() {
        const amount = inputAmount.value.trim();

        fetch("/addAmount", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ amount })
        })
            .then(response => {
                return response.text().then(text => text ? JSON.parse(text) : {});
            })
            .then(result => {
                if (result.success) {
                    showMessage(result);
                    fetchSalary();
                    fetchDebts();
                    fetchLeftTovers();
                    closeModal();
                } else {
                    showMessage(result);
                }
            })
            .catch(error => {
                console.error("Erro ao enviar:", error);
            });
    }


    // Função para processar o envio do formulário de adição de despesa
    function handleAddExpense() {
        const nome = document.getElementById("nome").value.trim();
        const valor = document.getElementById("valor").value.trim();
        const date = document.getElementById("date").value.trim();
        const situacao = document.getElementById("situacao").value;

        console.log("PASSOU AQUI handleAddExpense");

        fetch("/addExpense", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, valor, date, situacao })
        })
            .then(response => {
                return response.text().then(text => text ? JSON.parse(text) : {});
            })
            .then(result => {
                if (result.success) {
                    showMessage(result);
                    fetchSalary();
                    fetchDebts();
                    fetchLeftTovers();
                    closeModal();
                    location.reload();
                } else {
                    showMessage(result);
                }
            })
            .catch(error => {
                console.error("Erro ao enviar:", error);
            });
    }


    // Função para processar o envio do formulário de adição de despesa
    function handleUpdateExpense() {
        const id = document.getElementById("idUpdate").value;
        const nome = document.getElementById("nomeUpdate").value.trim();
        const valor = document.getElementById("valorUpdate").value.trim();
        const date = document.getElementById("dateUpdate").value.trim();
        const situacao = document.getElementById("situacaoUpdate").value;

        fetch("/updateExpense", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, nome, valor, date, situacao })
        })
            .then(response => {
                if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
                return response.text().then(text => text ? JSON.parse(text) : {});
            })
            .then(result => {
                if (result.success) {
                    closeModal();
                    showMessage(result);
                    fetchSalary();
                    fetchDebts();
                    fetchLeftTovers();
                } else {
                    showMessage(result);
                }
            })
            .catch(error => {
                console.error("Erro ao enviar:", error);
            });
    }

    function showMessage(result){
        if(result.success){
            successMessage(result.message);
        }else{
            errorMessages(result.message);
        }
    }

    function errorMessages(message){
        const error = document.querySelector(".erro");
        error.style.display = "flex";
        error.textContent = message;
    }

    function successMessage(message){
        const success = document.querySelector(".success");
        error.style.display = "flex";
        success.textContent = message;
    }

    // Função para fechar modais
    function closeModal() {
        modalAddAmount.style.display = "none";
        modalAdd.style.display = "none";
        formAddAmount.reset();
        formAddExpense.reset();
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
                errorMessages.style.display = "none";
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
            edit.addEventListener("click", function (event) {
                const expenseId = event.target.closest(".edit").dataset.id;
                fetchData(`/expense/${expenseId}`)
                    .then(data => populateEditModal(data))
                modalEdit.style.display = "flex";
            });
        });
    }

    // Preenche o modal de edição com os dados selecionados
    function populateEditModal(data) {
        document.getElementById("idUpdate").value = data.id;
        document.getElementById("nomeUpdate").value = data.nome;
        document.getElementById("valorUpdate").value = data.valor;
        document.getElementById("dateUpdate").value = data.date;
        document.getElementById("situacaoUpdate").value = data.situacao;
    }
});
