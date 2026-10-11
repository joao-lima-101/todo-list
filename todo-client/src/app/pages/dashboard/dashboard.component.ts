import { Component, inject, signal, computed } from '@angular/core';
import { HeaderComponent } from '../../components/ui/header.component';
import { ListService } from '@/app/services/list.service';
import { rxResource } from '@angular/core/rxjs-interop';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { catchError, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { MatIcon } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { CreateListDialog } from './components/dialogs/create-list/create-list.component';
import { Router } from '@angular/router';
import { ConfirmDialog } from '@/app/components/dialogs/confirm-dialog.component';
import { MatMenu, MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { ListEdit } from '@/@types/list.types';
import { EditListDialog } from './components/dialogs/edit-list/edit-list.component';

@Component({
  selector: 'dashboard-component',
  templateUrl: 'dashboard.component.html',
  imports: [HeaderComponent, MatIcon, MatMenu, MatMenuModule, MatMenuTrigger],
})
export class DashboardComponent {
  private api = inject(ListService);
  private snackBar = inject(CustomSnackBarService);
  private router = inject(Router);
  readonly dialog = inject(MatDialog);

  searchTerm = signal('');

  filteredLists = computed(() => {
    const search = this.searchTerm().trim().toLowerCase();
    const lists = this.listsResponse.value() ?? [];

    if (!search) {
      return lists;
    }

    return lists.filter(
      (list) => list.name.toLowerCase().includes(search) || list.id.toString().includes(search),
    );
  });

  listsResponse = rxResource({
    stream: () =>
      this.api.getLists().pipe(
        catchError((httpError) => {
          this.snackBar.error(httpError.error?.message);
          return of([]);
        }),
      ),
  });

  refreshData() {
    this.listsResponse.reload();
  }

  openConfirmDialog(idList: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '400px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
      data: {
        onConfirm: () => {
          this.api.removeList(idList).subscribe({
            next: () => {
              this.snackBar.success('Lista excluida com sucesso');
              this.refreshData();
            },
            error: (httpError: HttpErrorResponse) => {
              this.snackBar.error(httpError.error.message || 'Erro ao deletar lista');
            },
          });
        },
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.refreshData();
      }
    });
  }

  openEditDialog(list: ListEdit) {
    const dialogRef = this.dialog.open(EditListDialog, {
      width: '400px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
      data: list,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.refreshData();
      }
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(CreateListDialog, {
      width: '400px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.refreshData();
      }
    });
  }

  navigateList(id: number) {
    this.router.navigate([`/list/${id}`]);
  }
}
