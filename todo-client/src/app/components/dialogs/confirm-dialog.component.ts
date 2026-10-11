import { ConfirmDialogData } from '@/@types/confirm.types';
import { Component, inject } from '@angular/core';
import {
  MatDialogContent,
  MatDialogActions,
  MatDialogTitle,
  MatDialogClose,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';

@Component({
  templateUrl: 'confirm-dialog.component.html',
  imports: [MatDialogContent, MatDialogActions, MatDialogTitle, MatDialogClose],
})
export class ConfirmDialog {
  readonly dialogRef = inject(MatDialogRef<ConfirmDialog>);
  readonly confirmProps = inject<ConfirmDialogData>(MAT_DIALOG_DATA);

  handleClick() {
    this.confirmProps.onConfirm();
    this.dialogRef.close(true);
  }
}
