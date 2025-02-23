const add = document.querySelector("#add");
const edit = document.querySelectorAll(".edit");
const addAmount = document.querySelector("#addAmount");

const modalAdd = document.getElementById("addModal");
const modalEdit = document.getElementById("editModal");
const modalAddAmountModal = document.getElementById("addAmountModal");

const closeModalBtn = document.querySelectorAll(".close");

// Abre modal de adição de valor
if (addAmount) {
    addAmount.addEventListener("click", () => {
        modalAddAmountModal.style.display = "flex";
    });
}

// Abre modal de adição
if (add) {
    add.addEventListener("click", () => {
        modalAdd.style.display = "flex";
    });
}

// Abre modal de edição
if (edit) {
    edit.forEach(edit => {
        edit.addEventListener("click", function () {
            modalEdit.style.display = "flex";
        });
    });
}

// Fecha modal aberto
closeModalBtn.forEach(btn => {
    btn.addEventListener("click", () => {
        modalAdd.style.display = "none";
        modalAddAmountModal.style.display = "none";
        modalEdit.style.display = "none";
    });
});

window.addEventListener("click", (event) => {
    if (event.target === modalAdd || event.target === modalAddAmountModal) {
        modalAdd.style.display = "none";
        modalAddAmountModal.style.display = "none";
    }
});

function hideMessage(selector, delay) {
    setTimeout(function () {
        var messageElement = document.querySelector(selector);
        if (messageElement) {
            messageElement.style.display = 'none';
        }
    }, delay);
}

document.addEventListener('DOMContentLoaded', function () {
    var successMessage = document.querySelector('.success');
    var errorMessage = document.querySelector('.erro');

    if (successMessage) {
        hideMessage('.success', 3000);
    }

    if (errorMessage) {
        hideMessage('.erro', 3000);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const menus = document.querySelectorAll(".navigation-item");

    menus.forEach(category => {
        category.addEventListener("click", function () {
            menus.forEach(menu => menu.classList.remove("active"));
            this.classList.add("active");
        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const editButtons = document.querySelectorAll(".edit");

    editButtons.forEach(button => {
        button.addEventListener("click", function () {
            const id = this.getAttribute("data-id");
            const nome = this.getAttribute("data-name");
            const valor = this.getAttribute("data-amount");
            const date = this.getAttribute("data-date");
            const situacao = this.getAttribute("data-situacao");

            document.getElementById("idUpdate").value = id;
            document.getElementById("nomeUpdate").value = nome;
            document.getElementById("valorUpdate").value = valor;
            document.getElementById("dateUpdate").value = date;
            document.getElementById("situacaoUpdate").value = situacao === "PAGO" ? "Pago" : "A Pagar";
        });
    });
});
