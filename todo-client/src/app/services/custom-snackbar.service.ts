import { SnackBarData } from '@/@types/snack.types';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomSnackBar } from '@app/components/snackbar/custom-snackbar.component';

@Injectable({
  providedIn: 'root',
})
export class CustomSnackBarService {
  private snackBar = inject(MatSnackBar);

  private readonly defaultConfig = {
    panelClass: ['snack-primary'],
    duration: 4000,
    horizontalPosition: 'end' as const,
    verticalPosition: 'top' as const,
  };

  success(message: string, title?: string) {
    return this.open({
      title,
      message,
      type: 'success',
    });
  }

  error(message: string, title?: string) {
    return this.open({
      title,
      message,
      type: 'error',
    });
  }

  private open(data: SnackBarData) {
    return this.snackBar.openFromComponent(CustomSnackBar, {
      ...this.defaultConfig,
      data,
    });
  }
}
