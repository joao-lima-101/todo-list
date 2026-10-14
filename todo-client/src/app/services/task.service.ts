import { TaskEdit, TaskRequest, TaskResponse } from '@/@types/task.types';
import { environment } from '@/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private httpClient = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getTasks(idList: number): Observable<TaskResponse[]> {
    return this.httpClient.get<TaskResponse[]>(`${this.apiUrl}/list/${idList}/task`);
  }

  editTask(task: TaskEdit): Observable<{ message: string }> {
    const { id, idList, ...newTask } = task;
    return this.httpClient.patch<{ message: string }>(
      `${this.apiUrl}/list/${idList}/task/${id}`,
      newTask,
    );
  }

  toggleDone(idList: number, idTask: number): Observable<{ message: string }> {
    return this.httpClient.patch<{ message: string }>(
      `${this.apiUrl}/list/${idList}/task/${idTask}/done`,
      null,
    );
  }

  registerTasks(idList: number, data: TaskRequest): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.apiUrl}/list/${idList}/task`, data);
  }

  removeTask(idList: number, idTask: number): Observable<{ message: string }> {
    return this.httpClient.delete<{ message: string }>(
      `${this.apiUrl}/list/${idList}/task/${idTask}`,
    );
  }
}
