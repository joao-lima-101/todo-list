import { Component, signal, inject, computed, input } from '@angular/core';
import { HeaderComponent } from '@app/components/ui/header.component';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { TaskService } from '@/app/services/task.service';
import { rxResource } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { ListService } from '@/app/services/list.service';
import { MatMenu, MatMenuModule } from '@angular/material/menu';
import { DatePipe } from '@angular/common';
import { TaskEdit, TaskResponse } from '@/@types/task.types';
import { MatDialog } from '@angular/material/dialog';
import { CreateTaskDialog } from './components/dialogs/create-task/create-task.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmDialog } from '@/app/components/dialogs/confirm-dialog.component';
import { EditTaskDialog } from './components/dialogs/edit-task/edit-task.component';

@Component({
  templateUrl: 'tasks.component.html',
  imports: [HeaderComponent, MatIcon, MatMenu, MatMenuModule, DatePipe],
})
export class TasksComponent {
  private apiTask = inject(TaskService);
  private apiList = inject(ListService);
  private snackBar = inject(CustomSnackBarService);
  private router = inject(Router);
  readonly dialog = inject(MatDialog);
  listId = input.required<string>();

  searchTerm = signal('');

  filteredTasks = computed(() => {
    const search = this.searchTerm().trim().toLowerCase();
    const tasks = this.tasksResponse.value() ?? [];

    if (!search) {
      return tasks;
    }

    return tasks.filter((tsk) => tsk.name.toLowerCase().includes(search));
  });

  tasksResponse = rxResource({
    params: () => Number(this.listId()),
    stream: () => {
      return this.apiTask.getTasks(Number(this.listId())).pipe(
        catchError((httpError) => {
          this.snackBar.error(httpError.error.message || 'Erro inesperado');
          return of([]);
        }),
      );
    },
  });

  listResponse = rxResource({
    params: () => Number(this.listId()),
    stream: ({ params: id }) =>
      this.apiList.getList(id).pipe(
        catchError((httpError) => {
          this.snackBar.error(httpError.error.message || 'Erro inesperado');
          return of(undefined);
        }),
      ),
  });

  backToHome() {
    this.router.navigate(['/']);
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(CreateTaskDialog, {
      width: '400px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
      data: Number(this.listId()),
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tasksResponse.reload();
      }
    });
  }

  toggleDone(idItem: number, itemState: boolean) {}

  openEditDialog(task: Omit<TaskEdit, 'idList'>) {
    const dialogRef = this.dialog.open(EditTaskDialog, {
      width: '300px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
      data: { ...task, idList: Number(this.listId()) },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tasksResponse.reload();
      }
    });
  }

  openConfirmDialog(idTask: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '300px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
      data: {
        onConfirm: () => {
          this.apiTask.removeTask(Number(this.listId()), idTask).subscribe({
            next: () => {
              this.snackBar.success('Tarefa deletada com sucesso!');
              this.tasksResponse.reload();
            },
            error: (httpError: HttpErrorResponse) => {
              this.snackBar.error(httpError.error.message || 'Erro ao deletar tarefa');
            },
          });
        },
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tasksResponse.reload();
      }
    });
  }
}
