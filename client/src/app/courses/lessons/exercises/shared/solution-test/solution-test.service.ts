import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SolutonTestService {

  private baseUrl = 'http://localhost:8080/api/solution-tests';

  constructor(private http: HttpClient) { }

  createSolutionTest(solutionTest: Object): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, solutionTest);
  }
}
