import { Component, OnInit, ViewChild } from '@angular/core';
import { GoogleMap, MapInfoWindow, MapMarker, MapAnchorPoint } from '@angular/google-maps';
import { Store } from '@ngrx/store';
import { ActivatedRoute, Router } from '@angular/router';
import { Pet } from 'src/app/interface/pet';
import { Reports } from 'src/app/interface/reports';
import { Sighting } from 'src/app/interface/sighting';
import { AuthService } from 'src/app/service/auth.service';
import { PetService } from 'src/app/service/pet.service';

@Component({
  selector: 'app-pet-profile',
  templateUrl: './pet-profile.component.html',
  styleUrls: ['./pet-profile.component.scss']
})
export class PetProfileComponent implements OnInit{

  pet: Pet = {
    id: '',
    ownerId: '',
    name: '',
    type: '',
    image: '',
    lost: 0
  }
  report: Reports = {
    id: "",
    petId: "",
    lat: "",
    lng: "",
    dateTime: "",
    zone: "",
    description: "",
    closed: 0
  }

  @ViewChild('googleMap', { static: false })
  map!: GoogleMap;
  @ViewChild(MapInfoWindow, { static: false })
  info!: MapInfoWindow;

  userId!: string | null;

  sightingsArray: Sighting[] = [];
  featuresArray: string[] = [];
  currentPage: number = 1;
  pageSize: number = 3;
  totalPages: number[] = [];

  viewWidth!: number;
  markers = []  as  any;
  infoContent = '';
  zoom = 17;
  maxZoom = 30;
  minZoom = 8;
  center!: google.maps.LatLngLiteral;
  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid',
    zoomControl: false,
    scrollwheel: false,
    disableDoubleClickZoom: true,
    streetViewControl: false,
    disableDefaultUI: true,
    maxZoom: 30,
    minZoom: 10,
  }
  
  constructor(
    private petService: PetService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private store: Store<{ viewWidth: number }>,
  ){}

  ngOnInit(): void {
    this.authService.verifyTokenValidity();
    this.store.select('viewWidth')
      .subscribe(data => this.viewWidth = data);
    this.userId = this.authService.getUserId();
    this.petService.getPetData(this.activatedRoute.snapshot.params['id'])
    .subscribe((data: any) => {
      this.pet.id = data.id;
        this.pet.ownerId = data.owner_id;
        this.pet.name = data.name;
        this.pet.type = data.type;
        this.pet.image = data.image;
        this.pet.lost = data.lost;

        this.petService.getAllFeaturesByPetId(this.pet.id)
          .subscribe(data => {
            this.featuresArray = [...data];
          })
        
        if (this.pet.lost) {
          this.petService.getReport(this.pet.id)
          .subscribe((data: any) => {
            this.report.id = data.id;
            this.report.petId = data.pet_id;
            this.report.lat = data.lat;
            this.report.lng = data.lng;
            this.report.dateTime = data.date_time;
            this.report.zone = data.zone;
            this.report.description = data.description;
            this.report.closed = data.closed;
            this.setMapsCenter();
            this.loadSightingsByPage(this.currentPage, this.pageSize, this.report.id)
            this.getTotalPages();
            }
          )}
        }
      )
  }

  handleAddSighting() {
    this.router.navigate(
        ['/sighting'],
        { queryParams: { reportId: this.report.id, petId: this.pet.id } }
      )  
  }

  handleMarkPetAsFound() {   
    this.petService.putReportClosed(this.report.id, this.pet.id)
      .subscribe({
        next: (data: any) => {
          alert(data.message)
          window.location.reload();
        },
        error: (data: any) => {
          alert(data.error.message);
        }
      })
  }

  loadSightingsByPage(page: number, pageSize: number, reportId: string) {
    this.petService.getAllSightings(page, pageSize, reportId)
    .subscribe({
      next: (data: any) => {
        this.sightingsArray = [...data.sightings];
      }
    })
  }

  getTotalPages() {
    this.petService.getTotalPages()
      .subscribe(data => {        
        for (let i = 0; i < data.pages; i++) {
          this.totalPages.push(i+1);
        }
      })
  }

  onPageChange(newPage: number) {
    this.currentPage = newPage;
    this.loadSightingsByPage(newPage, this.pageSize, this.report.id);
  }

  setMapsCenter() {
    this.center = {
      lat: parseFloat(this.report.lat),
      lng: parseFloat(this.report.lng),
    };
    this.markers.push({
      position: {
        lat: parseFloat(this.report.lat),
        lng: parseFloat(this.report.lng),
      },
      title: 'Marker title ' + (this.markers.length + 1),
      info: 'Last seen location',
      options: {
        animation: google.maps.Animation.DROP
      },
    })
  }

  dropMarker(event:any) {
    this.markers.push({
      position: {
        lat: event.latLng.lat(),
        lng: event.latLng.lng(),
      },
      options: {
        animation: google.maps.Animation.DROP,
      }
    })
  }

  openInfo(marker: MapMarker, content: string) {
    this.infoContent = content;
    this.info.open(marker)
  }

  zoomIn() {
    if (this.zoom < this.maxZoom) this.zoom++;
  }

  zoomOut() {
    if (this.zoom > this.minZoom) this.zoom--;
  }

  eventHandler(event: any ,name:string){   
    // Add marker on double click event
    if(name === 'mapDblclick'){
      this.dropMarker(event)
    }
  }
}
