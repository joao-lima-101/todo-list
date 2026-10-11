import { TaskRequest } from '@/@types/task.types';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { TaskService } from '@/app/services/task.service';
import { Component, inject, input, signal } from '@angular/core';
import { form, FormField, FormRoot, required } from '@angular/forms/signals';
import {
  MatDialogContent,
  MatDialogActions,
  MatDialogTitle,
  MatDialogClose,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatOption, MatSelect } from '@angular/material/select';

@Component({
  templateUrl: 'create-task.component.html',
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogTitle,
    MatDialogClose,
    MatFormField,
    MatSelect,
    MatLabel,
    MatOption,
    FormRoot,
    FormField,
  ],
})
export class CreateTaskDialog {
  private api = inject(TaskService);
  private snackBar = inject(CustomSnackBarService);
  private listId: number = inject(MAT_DIALOG_DATA);
  readonly dialogRef = inject(MatDialogRef<CreateTaskDialog>);

  hasNameError() {
    const name = this.taskForm.name();
    return name.invalid() && name.touched();
  }

  taskModel = signal<TaskRequest>({
    name: '',
    description: '',
    dueDate: null,
    done: false,
    priority: 'MEDIO',
  });

  taskForm = form(
    this.taskModel,
    (schema) => {
      required(schema.name, { message: 'Digite um nome para a tarefa' });
    },
    {
      submission: {
        action: async (field) => {
          console.log(field().value());
          this.api.registerTasks(Number(this.listId), field().value()).subscribe({
            next: (response) => {
              this.snackBar.success(response.message || 'Criado com sucesso');
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
