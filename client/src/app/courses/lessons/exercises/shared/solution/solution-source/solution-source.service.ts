import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SolutonSourceService {

  private baseUrl = `${environment.baseUrl}solution-sources`;

  constructor(private http: HttpClient) { }

  createSolutionSource(solutionSource: Object): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, solutionSource);
  }

  getSolutionSourcesByExerciseId(exerciseId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/exercise/${exerciseId}`);
  }
}
