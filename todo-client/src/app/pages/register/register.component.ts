import { Component, signal, computed, inject } from '@angular/core';
import {
  disabled,
  email,
  form,
  FormField,
  FormRoot,
  minLength,
  required,
  validate,
} from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { RegisterRequest } from '@/@types/register.types';
import { AuthService } from '@app/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';

@Component({
  selector: 'register-component',
  templateUrl: './register.component.html',
  imports: [MatIconModule, RouterLink, FormField, FormRoot],
})
export class RegisterComponent {
  private api = inject(AuthService);
  private snackBar = inject(CustomSnackBarService);
  private router = inject(Router);

  hasNameError = computed(() => {
    const name = this.registerForm.name();
    return name.invalid() && name.touched();
  });

  hasEmailError = computed(() => {
    const email = this.registerForm.email();
    return email.invalid() && email.touched();
  });

  hasPasswordError = computed(() => {
    const password = this.registerForm.password();
    return password.invalid() && password.dirty();
  });

  hasConfirmPasswordError = computed(() => {
    const confirmPassword = this.registerForm.confirmPassword();
    return confirmPassword.invalid() && confirmPassword.dirty();
  });

  registerModel = signal<RegisterRequest>({
    name: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  registerForm = form(
    this.registerModel,
    (schema) => {
      required(schema.name, { message: 'Digite o seu nome' });
      required(schema.email, { message: 'Digite o seu e-mail' });
      email(schema.email, { message: 'E-mail inválido' });
      required(schema.password, { message: 'Digite uma senha' });
      minLength(schema.password, 8, { message: 'Senha deve ter mais de 8 digitos' });
      disabled(schema.confirmPassword, {
        when: ({ valueOf, stateOf }) => {
          const passwordVal = valueOf(schema.password);
          const passwordState = stateOf(schema.password);

          const isEmpty = !passwordVal || passwordVal.trim() === '';
          const hasErrors = passwordState.invalid();

          return isEmpty || hasErrors;
        },
      });
      validate(schema.confirmPassword, (ctx) => {
        const password = ctx.valueOf(schema.password);
        const confirmPassword = ctx.valueOf(schema.confirmPassword);

        if (password !== confirmPassword) {
          return {
            kind: 'passwordMismatch',
            message: 'Senhas não correspondem',
          };
        }

        return undefined;
      });
    },
    {
      submission: {
        action: async (field) => {
          this.api.register(field().value()).subscribe({
            next: () => {
              this.snackBar.success('Usuário criado com sucesso! faça login para continuar');

              this.router.navigate(['/login']);
            },
            error: (httpError: HttpErrorResponse) => {
              this.snackBar.error(httpError.error.message || 'Erro inesperado');
            },
          });
        },
      },
    },
  );
}
