import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PetService {

  constructor(
    private httpService: HttpService
  ) { }

  getAllFeatures(): Observable<any> {
    return this.httpService.request('GET', '/api/pets/features', '');
  }

  getAllFeaturesByPetId(petId: string):Observable<any> {
    return this.httpService.request('GET', `/api/pets/features/${petId}`, '')
  }

  getPetsData(userId: string): Observable<any> {
    return this.httpService.request('GET', `/api/pets/user/${userId}`, '');
  }

  getPetData(petId: string): Observable<any> {   
    return this.httpService.request('GET', `/api/pets/${petId}`, '');
  }

  getReport(petId: string):Observable<any> {
    return this.httpService.request('GET', `/api/pets/reports/${petId}`, '');
  }

  getAllReports(page: number, pageSize: number, region: string): Observable<any> {
    return this.httpService.request('GET', `/api/pets/reports?page=${page}&pageSize=${pageSize}&region=${region}`, '');
  }

  getAllSightings(page: number, pageSize: number, reportId: string): Observable<any> {
    return this.httpService.request('GET', `/api/sightings?page=${page}&pageSize=${pageSize}&reportId=${reportId}`, '');
  }

  getTotalPages(): Observable<any> {
    return this.httpService.request('GET', '/api/sightings/count', '')
  }

  getTotalReportPages(): Observable<any> {
    return this.httpService.request('GET', '/api/pets/count/reports', '')
  }

  putPetLost(petId: string, lostValue: any): Observable<any> {
    return this.httpService.request('PUT', `/api/sightings/${petId}`, lostValue)
  }

  putReportClosed(reportId: string, petId: string) {
    return this.httpService.request('PUT', `/api/pets/reports?reportId=${reportId}&petId=${petId}`, '');
  }

}
