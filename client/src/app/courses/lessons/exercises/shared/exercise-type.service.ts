import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExerciseTypeService {

  private baseUrl = 'http://localhost:8080/api/exerciseTypes';

  constructor(private http: HttpClient) { }

  getExericseTypesList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  getExerciseTypeByValue(type: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/exercise-type-${type}`);
  }
}
