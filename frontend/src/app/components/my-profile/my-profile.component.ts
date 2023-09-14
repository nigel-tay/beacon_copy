import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Pet } from 'src/app/interface/pet';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/service/auth.service';
import { HttpService } from 'src/app/service/http.service';
import { PetService } from 'src/app/service/pet.service';
import { PlacesService } from 'src/app/service/places.service';
import { toggleModal } from 'src/app/state/modalopen/modalopen.actions';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit, AfterViewInit{

  @ViewChild('editAddressInput', {static: true})
  editAddressInput!: ElementRef;

  modalOpen!: boolean;
  copiedUser!: User;
  user!: User;
  petsArray!: Pet[]

  autocomplete: google.maps.places.Autocomplete | undefined;

  constructor(
    private httpService: HttpService,
    private petService: PetService,
    private placesService: PlacesService,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private store: Store<{ modalOpen: boolean }>
    ){
      store.select('modalOpen').subscribe(data => this.modalOpen = data);
    }

  ngOnInit(): void {
    this.authService.verifyTokenValidity();
    let userId: string = this.activatedRoute.snapshot.params['id'];
    this.getUserData(userId);
    this.petService.getPetsData(userId)
        .subscribe((data: any) => {
          this.petsArray = [...data.pets]
        });
  }

  ngAfterViewInit(): void {
    this.copiedUser = {
      id: this.user.id,
      address: this.user.address,
      email: this.user.email,
      username: this.user.username,
      password: this.user.password,
      lat: this.user.lat,
      lng: this.user.lng,
      image: this.user.image
    }
    this.placesService.initialisePlacesAutoComplete(this.autocomplete, this.editAddressInput, this.copiedUser);
  }

  // get id param, make call to BE to retrieve user info
  getUserData(userId: string) {
    this.httpService.request('GET', `/api/users/${userId}`, '')
      .subscribe((data: User) => {
        this.user = data;
      })
  }

  handleOpenModal() {
    this.store.dispatch(toggleModal());
  }

  handleEditAddressSubmit() {
    
    if (this.user.address == this.copiedUser.address && this.editAddressInput.nativeElement.value.length !== 0) {
      alert('Please select an address from the dropdown');
      return;
    }
    else if (this.editAddressInput.nativeElement.value.trim.length === 0 && this.copiedUser.address === this.user.address) {
      this.copiedUser.address = "";
      this.copiedUser.lat = 0;
      this.copiedUser.lng = 0;
    }
    this.httpService.request('PUT', '/api/users/edit', this.copiedUser)
      .subscribe({
        next: (data: User) => {
          this.user = data;
          alert('Address changed successfully!')
          window.location.reload();
        },
        error: (data: any) => {
          alert(data.error.message);
        },
      })
    this.store.dispatch(toggleModal());
  }
}
