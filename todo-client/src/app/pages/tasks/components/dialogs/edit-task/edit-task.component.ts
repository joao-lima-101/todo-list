import { Component, inject, signal } from '@angular/core';
import { FormField, FormRoot, required, form } from '@angular/forms/signals';
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
import { TaskService } from '@/app/services/task.service';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { TaskEdit } from '@/@types/task.types';

@Component({
  templateUrl: 'edit-task.component.html',
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
export class EditTaskDialog {
  private api = inject(TaskService);
  private snackBar = inject(CustomSnackBarService);
  private dialogData = inject<TaskEdit>(MAT_DIALOG_DATA);
  readonly dialogRef = inject(MatDialogRef<EditTaskDialog>);

  hasNameError() {
    const name = this.taskForm.name();
    return name.invalid() && name.touched();
  }

  taskModel = signal<TaskEdit>({
    id: this.dialogData.id,
    idList: this.dialogData.idList,
    name: this.dialogData.name,
    description: this.dialogData.description,
    dueDate: this.dialogData.dueDate,
    done: this.dialogData.done,
    priority: this.dialogData.priority,
  });

  taskForm = form(
    this.taskModel,
    (schema) => {
      required(schema.name, { message: 'Digite um nome para a tarefa' });
    },
    {
      submission: {
        action: async (field) => {
          this.api.editTask(field().value()).subscribe({
            next: (response) => {
              this.snackBar.success(response.message || 'Tarefa editada com sucesso');
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
