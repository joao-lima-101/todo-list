import { Component, computed, inject, signal } from '@angular/core';
import { form, FormRoot, FormField, required } from '@angular/forms/signals';
import {
  MatDialogContent,
  MatDialogActions,
  MatDialogTitle,
  MatDialogClose,
  MatDialogRef,
} from '@angular/material/dialog';
import { ListService } from '@/app/services/list.service';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { ListRequest } from '@/@types/list.types';

@Component({
  templateUrl: 'create-list.component.html',
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogTitle,
    MatDialogClose,
    FormRoot,
    FormField,
  ],
})
export class CreateListDialog {
  private api = inject(ListService);
  private snackBar = inject(CustomSnackBarService);
  readonly dialogRef = inject(MatDialogRef<CreateListDialog>);

  hasNameError = computed(() => {
    const name = this.listForm.name();
    return name.invalid() && name.touched();
  });

  listModel = signal<ListRequest>({
    name: '',
    description: '',
  });

  listForm = form(
    this.listModel,
    (schema) => {
      required(schema.name, { message: 'Digite um nome para a lista' });
    },
    {
      submission: {
        action: async (field) => {
          this.api.registerList(field().value()).subscribe({
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
