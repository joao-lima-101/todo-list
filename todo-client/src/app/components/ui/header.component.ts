import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '@/app/services/auth/auth.service';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { ChangePasswordDialog } from '@/app/components/dialogs/change-password/change-password.component';

@Component({
  selector: 'header-component',
  templateUrl: 'header.component.html',
  imports: [MatIcon, MatMenuModule, MatButtonModule, RouterLink],
})
export class HeaderComponent {
  private api = inject(AuthService);
  private router = inject(Router);
  readonly dialog = inject(MatDialog);

  logout() {
    this.api.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
    });
  }

  openDialog() {
    this.dialog.open(ChangePasswordDialog, {
      width: '300px',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '300ms',
    });
  }
}
