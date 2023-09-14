import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MapsService {

  constructor() { }

  // // If you want to move your map center to the searched lat long position. Use below any one
        // // (with animation)
        // this.gmap.panTo({ lat: place.geometry.location.lat(), lng: place.geometry.location.lng() }); 
        // // (without animation)
        // this.gmap.setCenter({ lat: place.geometry.location.lat(), lng: place.geometry.location.lng() }); 
}
