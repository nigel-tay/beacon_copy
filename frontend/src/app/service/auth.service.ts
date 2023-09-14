import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import jwt_decode, { JwtPayload } from "jwt-decode";
import { enableLoginItems, disableLoginItems } from 'src/app/state/navloginenable/navloginenable.actions';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  currentComponent = window.location.href.split('/#/')[1];

  constructor(
    private router: Router,
    private store: Store<{ navLoginEnable: boolean }>,
  ) { }

  // Token
  getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

  setAuthToken(token: string | null): void {
    if (token !== null) {
      this.store.dispatch(enableLoginItems());
      window.localStorage.setItem("auth_token", token);
    }
    else {
      window.localStorage.removeItem("auth_token");
      window.localStorage.removeItem("user_id");
      window.localStorage.removeItem("username");
    }
  }

  

  verifyTokenValidity() {
      if (this.getAuthToken() !== null && this.getAuthToken() !== undefined) {
        const token: string | null = this.getAuthToken()
        const tokenPayload: JwtPayload = jwt_decode(token as string);
        const expirationTime: number | undefined = tokenPayload.exp ? tokenPayload.exp * 1000 : undefined
  
        if (expirationTime !== undefined && Date.now() >= expirationTime) {
          this.store.dispatch(disableLoginItems());
          if (this.currentComponent !== 'login' && this.currentComponent !== 'register' && this.currentComponent !== 'boards') {
            alert("Your session has expired. Please login again");
          }
          this.logout();
        }
      }
      else {
        if (this.currentComponent !== 'login' && this.currentComponent !== 'register' && this.currentComponent !== 'boards') {
          alert("Your session has expired. Please login again");
        }
        this.logout();
      }
  }

  logout() {
    this.setAuthToken(null);
    this.store.dispatch(disableLoginItems());
    if (this.currentComponent !== 'login' && this.currentComponent !== 'register' && this.currentComponent !== 'boards') {
      this.router.navigate(['/login']);
    }
  }

  // User data
  getUserId(): string | null {
    return window.localStorage.getItem("user_id");
  }

  setUserData(data: any | null): void {
    if (data !== null) {
      window.localStorage.setItem("user_id", data.id);
      window.localStorage.setItem("username", data.username);
    }
  }


}
