import { ChangePasswordData } from '@/@types/user.types';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { UserService } from '@/app/services/user.service';
import { Component, computed, inject, signal } from '@angular/core';
import {
  disabled,
  form,
  FormField,
  FormRoot,
  minLength,
  required,
  validate,
} from '@angular/forms/signals';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';

@Component({
  templateUrl: 'change-password.component.html',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    FormRoot,
    FormField,
  ],
})
export class ChangePasswordDialog {
  private readonly api = inject(UserService);
  private snackBar = inject(CustomSnackBarService);
  readonly dialogRef = inject(MatDialogRef<ChangePasswordDialog>);

  hasPasswordError = computed(() => {
    const password = this.changePasswordForm.password();
    return password.invalid() && password.touched();
  });

  hasNewPasswordError = computed(() => {
    const newPassword = this.changePasswordForm.newPassword();
    return newPassword.invalid() && newPassword.touched();
  });

  hasConfirmPasswordError = computed(() => {
    const confirmPassword = this.changePasswordForm.confirmPassword();
    return confirmPassword.invalid() && confirmPassword.touched();
  });

  changePasswordModel = signal<ChangePasswordData>({
    password: '',
    newPassword: '',
    confirmPassword: '',
  });

  changePasswordForm = form(
    this.changePasswordModel,
    (schema) => {
      required(schema.password, { message: 'Digita a sua senha' });
      required(schema.newPassword, { message: 'Digite uma nova senha' });
      required(schema.confirmPassword, { message: 'Digite a confirmação da senha' });
      minLength(schema.newPassword, 8, { message: 'Senha deve ter pelo menos 8 digitos' });
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
        const newPassword = ctx.valueOf(schema.newPassword);
        const confirmPassword = ctx.valueOf(schema.confirmPassword);

        if (newPassword !== confirmPassword) {
          return {
            kind: 'passwordMissmatch',
            message: 'Senhas não correspondem',
          };
        }

        return undefined;
      });
    },
    {
      submission: {
        action: async (field) => {
          this.api.changePassword(field().value()).subscribe({
            next: () => {
              this.snackBar.success('Lista criada com sucesso!');
              this.dialogRef.close(true);
            },
            error: (httpError) => {
              this.snackBar.error(httpError.error.message || 'Erro inesperado');
            },
          });
        },
      },
    },
  );
}
