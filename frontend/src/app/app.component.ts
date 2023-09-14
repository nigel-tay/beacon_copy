import { Component } from '@angular/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  modalOpen!: boolean;

  constructor(
    private store: Store<{ modalOpen: boolean }>
    ){
      store.select('modalOpen').subscribe(data => this.modalOpen = data);
    }
}
