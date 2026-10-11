import { ChangePasswordData } from '@/@types/user.types';
import { environment } from '@/environments/environment';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  httpClient = inject(HttpClient);

  changePassword(changePasswordData: ChangePasswordData): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(
      `${environment.apiUrl}/user/change-password`,
      changePasswordData,
      {
        withCredentials: true,
      },
    );
  }
}
