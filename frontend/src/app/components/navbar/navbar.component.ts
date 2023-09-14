import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { isMobile, isTablet, isDesktop } from '../../state/viewwidth/viewwidth.actions'
import { openDropdown, closeDropdown } from 'src/app/state/mobilenav/mobilenav.actions';
import { AuthService } from 'src/app/service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{
  loggedIn!: boolean;
  hamburgerStatus!: string;
  dropdownStatus$!: Observable<string>;
  viewWidth$!: Observable<number>;

  constructor(
    private store: Store<{ viewWidth: number, mobileNav: string, navLoginEnable: boolean }>,
    private authService: AuthService,
    private router: Router
    ) {
    this.viewWidth$ = store.select('viewWidth');
    this.dropdownStatus$ = store.select('mobileNav');
    this.dropdownStatus$.subscribe(data => this.hamburgerStatus = data);
    store.select('navLoginEnable').subscribe(data => this.loggedIn = data);
  }

  ngOnInit(): void {
    if (window.screen.width <= 640) {
      this.store.dispatch(isMobile());
    }
    else if (window.screen.width > 640 && window.screen.width <= 768) {
      this.store.dispatch(isTablet());
    }
    else {
      this.store.dispatch(isDesktop());
    }
  }

  navigateWithId(component: string) {
    let id:string = this.authService.getUserId() as string;
    this.router.navigate([`/${component}`, id])
  }

  toggleHamburger() {
    this.hamburgerStatus === 'close' ?
    this.openDropDown() :
    this.closeDropDown();
  }

  closeDropDown() {
    this.store.dispatch(closeDropdown());
  }

  openDropDown() {
    this.store.dispatch(openDropdown());
  }
}
