export class FormValidator {
  constructor(form) {
    this.form = form;
    this.fields = Array.from(this.form.querySelectorAll("[data-validate]"));
    this.errors = {};
    this.init();
  }

  init() {
    this.form.addEventListener("submit", (event) => this.handleSubmit(event));
    this.fields.forEach((field) =>
      field.addEventListener("input", () => this.validateField(field))
    );
  }

  handleSubmit(event) {
    this.errors = {};
    this.fields.forEach((field) => this.validateField(field));

    if (Object.values(this.errors).some((msg) => msg !== "")) {
      event.preventDefault();
    }
  }

  validateField(field) {
    const rules = field.dataset.validate.split("|");
    const value = field.value.trim();
    let errorMessage = "";

    for (let rule of rules) {
      const [ruleName, param] = rule.split(":");
      if (!this[ruleName]) continue;

      const isValid = this[ruleName](value, param);
      if (!isValid) {
        errorMessage = this.getErrorMessage(ruleName, param);
        break;
      }
    }

    this.errors[field.name] = errorMessage;
    this.showError(field, errorMessage);
    
    // Atualiza o estado visual do campo
    this.updateFieldState(field, errorMessage === "");
    
    return errorMessage === ""; // Retorna true se o campo for válido
  }

  // Função para verificar se um campo específico é válido
  isFieldValid(fieldName) {
    const field = this.fields.find(f => f.name === fieldName);
    if (!field) return true; // Se o campo não existe, consideramos válido
    
    return this.errors[fieldName] === "" || this.errors[fieldName] === undefined;
  }
  
  // Função para verificar se todo o formulário é válido
  isFormValid() {
    return !Object.values(this.errors).some(msg => msg !== "");
  }

  // Atualiza o estado visual do campo (válido/inválido)
  updateFieldState(field, isValid) {
    if (isValid) {
      field.classList.remove("invalid");
      field.classList.add("valid");
    } else {
      field.classList.remove("valid");
      field.classList.add("invalid");
    }
  }

  showError(field, message) {
    let errorSpan = field.parentElement.querySelector(".error");
    if (!errorSpan) {
      errorSpan = document.createElement("span");
      errorSpan.classList.add("error");
      // Corrigido: Adicionando o span após o campo
      field.parentElement.appendChild(errorSpan);
    }
    errorSpan.textContent = message;
    errorSpan.style.display = message ? "block" : "none";
  }

  required(value) {
    return value.length > 0;
  }

  min(value, minLength) {
    return value.length >= minLength;
  }

  max(value, maxLength) {
    return value.length <= maxLength;
  }

  email(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
  }

  money(value) {
    return /^(\d{1,3}(\.\d{3})*|\d+)([.,]\d{2})?$/.test(value);
  }

  quantity(value) {
    return /^\d+$/.test(value) && parseInt(value, 10) > 0;
  }

  getErrorMessage(rule, param) {
    const messages = {
      required: "Este campo é obrigatório.",
      min: `O campo deve ter pelo menos ${param} caracteres.`,
      max: `O campo deve ter no máximo ${param} caracteres.`,
      email: "Digite um e-mail válido.",
      money: "Digite um valor monetário válido (ex: 99,99 ou 1.000,00).",
      quantity: "Digite um número inteiro positivo.",
    };
    return messages[rule] || "Campo inválido.";
  }
}