/**
 * Classe FormValidator
 * Implementa um sistema de validação de formulários em JavaScript
 * Permite validar campos com múltiplas regras definidas via atributo data-validate
 */
export class FormValidator {
  /**
   * Construtor da classe
   * @param {HTMLFormElement} form - O elemento de formulário HTML a ser validado
   */
  constructor(form) {
    this.form = form; // Armazena a referência ao formulário
    this.fields = Array.from(this.form.querySelectorAll("[data-validate]")); // Seleciona todos os campos que precisam de validação
    this.errors = {}; // Objeto para armazenar as mensagens de erro de cada campo
    this.passwordCriteria = {
      hasLength: false,
      hasLetter: false,
      hasNumber: false,
      hasSpecial: false
    };
    this.init(); // Inicializa o validador
  }

  /**
   * Inicializa os eventos do validador
   * Configura os listeners para eventos de submit e input
   */
  init() {
    // Adiciona listener para o evento de envio do formulário
    this.form.addEventListener("submit", (event) => this.handleSubmit(event));

    // Adiciona listener para o evento de input em cada campo
    this.fields.forEach((field) => {
      field.addEventListener("input", () => this.validateField(field));
      if (field.name === "password") {
        this.setupPasswordValidation(field);
      }

    });
  }

  /**
   * Configura a validação especial para o campo de senha
   */
  setupPasswordValidation(passwordField) {
    // Cria o contêiner de critérios se ainda não existir
    const parentElement = passwordField.parentElement;
    let criteriaContainer = parentElement.querySelector(".password-criteria");
    
    if (!criteriaContainer) {
      criteriaContainer = document.createElement("div");
      criteriaContainer.className = "password-criteria";
      
      // Adiciona os critérios com ícones
      criteriaContainer.innerHTML = `
        <p class="criteria-item" data-criteria="hasLength"><span class="icon">❌</span> A senha deve ter pelo menos 8 caracteres</p>
        <p class="criteria-item" data-criteria="hasLetter"><span class="icon">❌</span> Deve ter letras</p>
        <p class="criteria-item" data-criteria="hasNumber"><span class="icon">❌</span> Deve ter números</p>
        <p class="criteria-item" data-criteria="hasSpecial"><span class="icon">❌</span> Deve ter caracteres especiais</p>
      `;
      
      parentElement.insertBefore(criteriaContainer, passwordField.nextSibling);
    }
    
    // Adiciona evento de input para atualizar os critérios em tempo real
    passwordField.addEventListener("input", () => {
      this.checkPasswordCriteria(passwordField.value);
      this.updatePasswordCriteriaDisplay();
    });
  }

  /**
   * Verifica os critérios de senha e atualiza o objeto de estado
   */
  checkPasswordCriteria(value) {
    this.passwordCriteria = {
      hasLength: value.length >= 8,
      hasLetter: /[A-Za-z]/.test(value),
      hasNumber: /\d/.test(value),
      hasSpecial: /[@$!%*?&]/.test(value)
    };
    
    return Object.values(this.passwordCriteria).every(criteria => criteria);
  }

  /**
   * Atualiza a exibição visual dos critérios de senha
   */
  updatePasswordCriteriaDisplay() {
    const items = document.querySelectorAll(".criteria-item");
    
    items.forEach(item => {
      const criteriaName = item.dataset.criteria;
      const icon = item.querySelector(".icon");
      
      if (this.passwordCriteria[criteriaName]) {
        item.classList.add("valid");
        item.classList.remove("invalid");
        icon.textContent = "✅";
      } else {
        item.classList.add("invalid");
        item.classList.remove("valid");
        icon.textContent = "❌";
      }
    });
  }

  /**
   * Manipulador do evento de envio do formulário
   * @param {Event} event - O evento de submit
   */
  handleSubmit(event) {
    this.errors = {}; // Limpa o objeto de erros

    // Valida todos os campos do formulário
    this.fields.forEach((field) => this.validateField(field));

    // Se existir algum erro, impede o envio do formulário
    if (Object.values(this.errors).some((msg) => msg !== "")) {
      event.preventDefault();
    }
  }

  /**
   * Valida um campo específico com base nas regras definidas
   * @param {HTMLElement} field - O campo a ser validado
   * @returns {boolean} - Retorna true se o campo for válido
   */
  validateField(field) {
    // Obtém as regras de validação separadas por |
    const rules = field.dataset.validate.split("|");
    const value = field.value.trim(); // Remove espaços em branco
    let errorMessage = "";

    // Itera sobre cada regra de validação
    for (let rule of rules) {
      const [ruleName, param] = rule.split(":"); // Separa o nome da regra e seu parâmetro, se houver
      if (!this[ruleName]) continue; // Pula se a regra não estiver implementada

      // Executa a função de validação correspondente à regra
      const isValid = this[ruleName](value, param);
      if (!isValid) {
        errorMessage = this.getErrorMessage(ruleName, param);
        break; // Interrompe ao encontrar o primeiro erro
      }
    }

    // Armazena a mensagem de erro para este campo
    this.errors[field.name] = errorMessage;

    // Exibe a mensagem de erro no DOM
    this.showError(field, errorMessage);

    // Atualiza o estado visual do campo
    this.updateFieldState(field, errorMessage === "");

    return errorMessage === ""; // Retorna true se o campo for válido
  }

  /**
   * Verifica se um campo específico é válido
   * @param {string} fieldName - O nome do campo a verificar
   * @returns {boolean} - Retorna true se o campo for válido
   */
  isFieldValid(fieldName) {
    const field = this.fields.find(f => f.name === fieldName);
    if (!field) return true; // Se o campo não existe, consideramos válido

    return this.errors[fieldName] === "" || this.errors[fieldName] === undefined;
  }

  /**
   * Verifica se todo o formulário é válido
   * @returns {boolean} - Retorna true se todos os campos forem válidos
   */
  isFormValid() {
    return !Object.values(this.errors).some(msg => msg !== "");
  }

  /**
   * Atualiza o estado visual do campo (válido/inválido)
   * @param {HTMLElement} field - O campo a ser atualizado
   * @param {boolean} isValid - Indica se o campo é válido
   */
  updateFieldState(field, isValid) {
    if (isValid) {
      field.classList.remove("invalid");
      field.classList.add("valid");
    } else {
      field.classList.remove("valid");
      field.classList.add("invalid");
    }
  }

  /**
   * Exibe a mensagem de erro para um campo
   * @param {HTMLElement} field - O campo relacionado ao erro
   * @param {string} message - A mensagem de erro a ser exibida
   */
  showError(field, message) {
    // Busca um span de erro existente ou cria um novo
    let errorSpan = field.parentElement.querySelector(".error");
    if (!errorSpan) {
      errorSpan = document.createElement("span");
      errorSpan.classList.add("error");
      // Adiciona o span após o campo
      field.parentElement.appendChild(errorSpan);
    }

    // Define o texto e visibilidade do span de erro
    errorSpan.textContent = message;
    errorSpan.style.display = message ? "block" : "none";
  }

  /**
   * Regra: Verifica se o campo foi preenchido
   * @param {string} value - Valor do campo
   * @returns {boolean} - Retorna true se o campo não estiver vazio
   */
  required(value) {
    return value.length > 0;
  }

  /**
   * Regra: Verifica o comprimento mínimo do valor
   * @param {string} value - Valor do campo
   * @param {number} minLength - Comprimento mínimo exigido
   * @returns {boolean} - Retorna true se o comprimento for adequado
   */
  min(value, minLength) {
    return value.length >= minLength;
  }

  /**
   * Regra: Verifica o comprimento máximo do valor
   * @param {string} value - Valor do campo
   * @param {number} maxLength - Comprimento máximo permitido
   * @returns {boolean} - Retorna true se o comprimento for adequado
   */
  max(value, maxLength) {
    return value.length <= maxLength;
  }

  /**
   * Regra: Verifica se o valor é um email válido
   * @param {string} value - Valor do campo
   * @returns {boolean} - Retorna true se for um email válido
   */
  email(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
  }

  /**
   * Regra: Verifica se o valor está em formato monetário válido
   * @param {string} value - Valor do campo
   * @returns {boolean} - Retorna true se for um valor monetário válido
   */
  money(value) {
    return /^(\d{1,3}(\.\d{3})*|\d+)([.,]\d{2})?$/.test(value);
  }

  /**
   * Regra: Verifica se o valor é uma quantidade (número inteiro positivo)
   * @param {string} value - Valor do campo
   * @returns {boolean} - Retorna true se for uma quantidade válida
   */
  quantity(value) {
    return /^\d+$/.test(value) && parseInt(value, 10) > 0;
  }

  // /**
  //  * Regra: Verifica se o valor atende aos requisitos de uma senha segura
  //  * @param {string} value - Valor do campo
  //  * @returns {boolean} - Retorna true se a senha for válida
  //  */
  // password(value) {
  //   // Senha deve ter pelo menos 8 caracteres, uma letra, um número e um caractere especial
  //   return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(value);
  // }

  /**
   * Regra: Verifica se o valor corresponde ao valor de outro campo
   * Usado principalmente para confirmação de senha
   * @param {string} value - Valor do campo atual
   * @param {string} targetFieldName - Nome do campo a ser comparado
   * @returns {boolean} - Retorna true se os valores forem iguais
   */
  matches(value, targetFieldName) {
    const targetField = this.form.querySelector(`[name="${targetFieldName}"]`);
    if (!targetField) return false;
    return value === targetField.value;
  }

  /**
   * Obtém a mensagem de erro para uma regra específica
   * @param {string} rule - Nome da regra de validação
   * @param {string} param - Parâmetro da regra
   * @returns {string} - Mensagem de erro correspondente
   */
  getErrorMessage(rule, param) {
    const messages = {
      required: "Este campo é obrigatório.",
      min: `O campo deve ter pelo menos ${param} caracteres.`,
      max: `O campo deve ter no máximo ${param} caracteres.`,
      email: "Digite um e-mail válido.",
      money: "Digite um valor monetário válido (ex: 99,99 ou 1.000,00).",
      quantity: "Digite um número inteiro positivo.",
      matches: `Este campo deve corresponder ao campo ${param}.`
    };
    return messages[rule] || "Campo inválido.";
  }
}