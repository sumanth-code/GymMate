import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'https://gymmate-ebuv.onrender.com/api';

  // State management variables for our App
  private currentUserId = '';
  private jwtToken = '';

  // Subject for reactive UI updates
  public sessionReady$ = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) { }

  // MOCK Auth Initialization
  async initializeMockSession(phone: string) {
    this.http.post<any>(`${this.baseUrl}/auth/login`, {
      phoneNumber: phone,
      firebaseToken: 'MOCK_JWT_DEV_MODE'
    }).subscribe({
      next: (res) => {
        this.currentUserId = res.userId;
        this.jwtToken = res.token;
        console.log('API Service: Mock Auth Successful. ID: ', res.userId);
        this.sessionReady$.next(true); // Fire event to load dashboard stats securely now
      },
      error: (err) => console.error('Failed to establish mock user session:', err)
    });
  }

  // Analytics API
  getInsights(): Observable<any> {
    return this.http.get(`${this.baseUrl}/workouts/insights/${this.currentUserId}`);
  }

  // Workout Tracker
  createWorkout(name: string, weight: number, reps: number, sets: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/workouts`, {
      userId: this.currentUserId,
      name, weightLifted: weight, reps, sets
    });
  }

  // OCR Endpoint
  analyzeDocument(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.baseUrl}/documents/upload/${this.currentUserId}`, formData);
  }
}
