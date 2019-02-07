import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private baseUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) { }

  createTask(task: Object): Observable<Object> {
    console.log(task);
    return this.http.post(`${this.baseUrl}` + `/create`, task);
  }

  getTasksList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

}
