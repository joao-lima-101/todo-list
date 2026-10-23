import { ListResponse } from '@/@types/list.types';
import { CustomSnackBarService } from '@/app/services/custom-snackbar.service';
import { DecimalPipe } from '@angular/common';
import { Component, inject, input, output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatIcon } from '@angular/material/icon';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MatProgressBar } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ConfirmDialog } from '@/app/components/dialogs/confirm-dialog.component';
import { ListService } from '@/app/services/list.service';
import { Router } from '@angular/router';
import { EditListDialog } from '../../dialogs/edit-list/edit-list.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ListEdit } from '@/@types/list.types';

@Component({
  selector: 'list-card',
  templateUrl: 'list-card.component.html',
  imports: [MatIcon, MatProgressBar, MatTooltipModule, MatMenuModule, MatMenuTrigger, DecimalPipe],
})
export class ListCardComponent {
  list = input.required<ListResponse>();
  refreshData = output<void>();

  readonly dialog = inject(MatDialog);
  private snackBar = inject(CustomSnackBarService);
  private api = inject(ListService);
  private router = inject(Router);

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
              this.refreshData.emit();
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
        this.refreshData.emit();
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
        this.refreshData.emit();
      }
    });
  }

  navigateToList(id: number) {
    this.router.navigate([`/list/${id}`]);
  }
}
