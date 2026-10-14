import { Component, computed, signal, inject } from '@angular/core';
import { email, form, FormField, FormRoot, required } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { LoginRequest } from '@/@types/login.types';
import { AuthService } from '@/app/services/auth/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';

@Component({
  selector: 'login-component',
  templateUrl: './login.component.html',
  imports: [FormField, FormRoot, MatIconModule, RouterLink, MatProgressSpinnerModule],
})
export class LoginComponent {
  private api = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(CustomSnackBarService);

  isLoading = signal(false);

  hasEmailError = computed(() => {
    const email = this.loginForm.email();
    return email.invalid() && email.touched();
  });

  hasPasswordError = computed(() => {
    const password = this.loginForm.password();
    return password.invalid() && password.touched();
  });

  loginModel = signal<LoginRequest>({
    email: '',
    password: '',
  });

  loginForm = form(
    this.loginModel,
    (schema) => {
      required(schema.email, { message: 'E-mail é obrigatório' });
      email(schema.email, { message: 'E-mail inválido' });
      required(schema.password, { message: 'Senha é obrigatória' });
    },
    {
      submission: {
        action: async (field) => {
          this.isLoading.set(true);

          this.api.login(field().value()).subscribe({
            next: () => {
              this.snackBar.success('Login realizado com sucesso', 'Bem vindo!');

              this.isLoading.set(false);
              this.router.navigate(['']);
            },
            error: (httpError: HttpErrorResponse) => {
              this.isLoading.set(false);

              this.snackBar.error(httpError.error.message || 'Erro inesperado');
            },
          });
        },
      },
    },
  );
}
