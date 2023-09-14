import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Reports } from 'src/app/interface/reports';
import { ZoneBoundaries } from 'src/app/interface/zone-boundaries';
import { AuthService } from 'src/app/service/auth.service';
import { HttpService } from 'src/app/service/http.service';
import { PetService } from 'src/app/service/pet.service';
import { PlacesService } from 'src/app/service/places.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit{

  @ViewChild('addressInput', {static: true})
  addressInput!: ElementRef;

  zoneBoundaries:ZoneBoundaries = {
    central: {
      latMin: 1.3000,
      latMax: 1.4500,
      lngMin: 103.8000,
      lngMax: 103.9400,
    },
    east: {
      latMin: 1.2200,
      latMax: 1.3500,
      lngMin: 103.9000,
      lngMax: 104.0400,
    },
    north: {
      latMin: 1.3500,
      latMax: 1.4700,
      lngMin: 103.7500,
      lngMax: 103.8500,
    },
    "north-east": {
      latMin: 1.3400,
      latMax: 1.4300,
      lngMin: 103.8800,
      lngMax: 103.9800,
    },
    west: {
      latMin: 1.2200,
      latMax: 1.3800,
      lngMin: 103.5800,
      lngMax: 103.7500,
    },
  };

  reportFormGroup!: FormGroup;
  report!: Reports;
  autocomplete: google.maps.places.Autocomplete | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private placesService: PlacesService,
    private httpService: HttpService,
    private petService: PetService,
    private authService: AuthService
  ){}

  ngOnInit(): void {
    this.authService.verifyTokenValidity();
    let petId: string = this.activatedRoute.snapshot.params['id'];
    this.report = {
      id: "",
      petId: petId,
      lat: "",
      lng: "",
      dateTime: "",
      zone: "",
      description: "",
      closed: 0
    }
    this.initialiseReportFormGroup();
    this.placesService.initialisePlacesAutoComplete(this.autocomplete, this.addressInput, this.report);
  }

  ngAfterInit(): void {
    this.placesService.initialisePlacesAutoComplete(this.autocomplete, this.addressInput, this.report);
  }

  initialiseReportFormGroup() {
    this.reportFormGroup = this.fb.group({
      dateTime: this.fb.control('', [Validators.required]),
      description: this.fb.control(''),
    })
  }

  determineZone(userLat: number, userLng: number): string {
    for (let zoneName in this.zoneBoundaries) {
      const zone = this.zoneBoundaries[zoneName];
      if (
        userLat >= zone.latMin &&
        userLat <= zone.latMax &&
        userLng >= zone.lngMin &&
        userLng <= zone.lngMax
      ) {
        return zoneName;
      }
    }
    // If user coordinates don't match any zone, you can handle it as needed.
    return "others";
  }

  handleFormSubmit() {
    if (this.report.lat.length === 0) {
      alert("Please select an address from the dropdown")
      return;
    }
    this.report.id = crypto.randomUUID().toString();
    this.report.dateTime = this.reportFormGroup.value.dateTime.toString();    
    this.report.zone = this.determineZone(parseFloat(this.report.lat), parseFloat(this.report.lng));
    this.report.description = this.reportFormGroup.value.description;
    this.httpService.request('POST', '/api/pets/reports',this.report)
      .subscribe({
        next: (data: any) => {
          this.petService.putPetLost(this.report.petId, '1')
            .subscribe(data => {
                this.router.navigate([`/pet-profile/${this.report.petId}`])
              })
        },
        error: (data: any) => {
          alert(data.error.message)
        }
    })
  }
}
 