import { Component, inject, signal } from '@angular/core';
import { ListEdit, ListRequest } from '@/@types/list.types';
import { form, FormField, FormRoot, required } from '@angular/forms/signals';
import { ListService } from '@/app/services/list.service';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogTitle,
  MatDialogClose,
  MatDialogRef,
} from '@angular/material/dialog';

@Component({
  templateUrl: 'edit-list.component.html',
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogTitle,
    MatDialogClose,
    FormField,
    FormRoot,
  ],
})
export class EditListDialog {
  private api = inject(ListService);
  private snackBar = inject(CustomSnackBarService);
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<ListEdit>(MAT_DIALOG_DATA);

  hasNameError() {
    const name = this.listForm.name();
    return name.invalid() && name.touched();
  }

  listModel = signal<ListEdit>({
    id: this.dialogData.id,
    name: this.dialogData.name,
    description: this.dialogData.description,
  });

  listForm = form(
    this.listModel,
    (schema) => {
      required(schema.name, { message: 'Um nome deve ser inserido' });
    },
    {
      submission: {
        action: async (field) => {
          this.api.editList(field().value()).subscribe({
            next: (response) => {
              this.snackBar.success(response.message || 'Lista editada com sucesso!');
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
