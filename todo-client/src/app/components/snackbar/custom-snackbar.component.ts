import { Component, inject } from '@angular/core';
import {
  MatSnackBarAction,
  MatSnackBarLabel,
  MatSnackBarActions,
} from '@angular/material/snack-bar';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { MatIcon } from '@angular/material/icon';
import { SnackBarData } from '@/@types/snack.types';

@Component({
  selector: 'custom-snackbar',
  templateUrl: 'custom-snackbar.component.html',
  imports: [MatIcon, MatSnackBarAction, MatSnackBarLabel, MatSnackBarActions],
})
export class CustomSnackBar {
  protected data = inject<SnackBarData>(MAT_SNACK_BAR_DATA);
  protected snackBarRef = inject(MatSnackBarRef);

  typeThemes = {
    success: 'border-green-200 bg-green-50 text-green-800',
    error: 'border-red-200 bg-red-50 text-red-800',
  };
}
