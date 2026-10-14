import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ListResponse, ListRequest, ListEdit } from '@/@types/list.types';
import { environment } from '@/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ListService {
  private readonly apiUrl = environment.apiUrl;
  httpClient = inject(HttpClient);

  registerList(data: ListRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.apiUrl}/list`, data);
  }

  removeList(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/list/${id}`);
  }

  editList(data: ListEdit): Observable<{ message: string }> {
    return this.httpClient.patch<{ message: string }>(`${this.apiUrl}/list/${data.id}`, {
      name: data.name,
      description: data.description,
    });
  }

  getList(id: number): Observable<ListResponse> {
    return this.httpClient.get<ListResponse>(`${this.apiUrl}/list/${id}`);
  }

  getLists(): Observable<ListResponse[]> {
    return this.httpClient.get<ListResponse[]>(`${this.apiUrl}/list`);
  }
}
