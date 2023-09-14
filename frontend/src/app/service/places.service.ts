import { ElementRef, Injectable } from '@angular/core';
import { User } from '../interface/user';
import { Pet } from '../interface/pet';
import { Reports } from '../interface/reports';

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor() { }

  initialisePlacesAutoComplete(autocomplete: google.maps.places.Autocomplete | undefined, input: ElementRef, entity: User | Reports) {
    autocomplete = new google.maps.places.Autocomplete(input.nativeElement);
    autocomplete.addListener('place_changed', () => {
      const place = autocomplete?.getPlace();
      entity.lat = place?.geometry?.location?.lat();
      entity.lng = place?.geometry?.location?.lng();
      if ('address' in entity) {
        entity.address = place?.formatted_address;
      }
    })
  }
}
