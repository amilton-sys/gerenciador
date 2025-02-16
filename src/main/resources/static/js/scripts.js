const addExpense = document.querySelector("#addExpense");
const editExpense = document.querySelector(".editExpense");
const addAmount = document.querySelector("#addAmount");

const modalAddExpense = document.getElementById("addExpenseModal");
const modalEditExpense = document.getElementById("editExpenseModal");
const modalAddAmountModal = document.getElementById("addAmountModal");

const closeModalBtn = document.querySelectorAll(".close");



if (addAmount) {
    addAmount.addEventListener("click", () => {
        modalAddAmountModal.style.display = "flex";
    });
}

if (addExpense) {
    addExpense.addEventListener("click", () => {
        modalAddExpense.style.display = "flex";
    });
}

closeModalBtn.forEach(btn => {
    btn.addEventListener("click", () => {
        modalAddExpense.style.display = "none";
        modalAddAmountModal.style.display = "none";
        modalEditExpense.style.display = "none";
    });
});

window.addEventListener("click", (event) => {
    if (event.target === modalAddExpense || event.target === modalAddAmountModal) {
        modalAddExpense.style.display = "none";
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
    console.log(menus);

    menus.forEach(category => {
        category.addEventListener("click", function () {
            menus.forEach(menu => menu.classList.remove("active"));
            this.classList.add("active");
        });
    });
});

