package com.sys.gerenciador.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseComIdInput extends ExpenseInput {
    @NotNull
    private Long id;
}
