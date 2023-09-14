import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService
    ) { }

  

  request(method: string, url: string, data: any): Observable<any> {
    let headers: any;

    if (this.authService.getAuthToken() !== null) {
      headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getAuthToken()}`);
    }

    switch(method.toLowerCase()) {
      case 'get':
          return this.httpClient.get<any>(url, {headers: headers}); 
        break;
      case 'post':
          return this.httpClient.post<any>(url, data, {headers: headers});
        break;
      case 'put':
          return this.httpClient.put<any>(url, data, {headers: headers});
        break;
      default:
          return this.httpClient.delete<any>(url, {headers: headers});
    }
  }
}
