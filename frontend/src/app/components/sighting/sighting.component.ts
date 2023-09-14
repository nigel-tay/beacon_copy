import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sighting } from 'src/app/interface/sighting';
import { AuthService } from 'src/app/service/auth.service';
import { HttpService } from 'src/app/service/http.service';

@Component({
  selector: 'app-sighting',
  templateUrl: './sighting.component.html',
  styleUrls: ['./sighting.component.scss']
})
export class SightingComponent implements OnInit{

  @ViewChild('imageInput')
  imageInput!: ElementRef;

  sighting!: Sighting;
  reportId!: string;
  petId!: string;
  userId!: string | null;

  sightingFormGroup!: FormGroup;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private httpService: HttpService,
    private fb: FormBuilder
  ){}

  ngOnInit(): void {
    this.authService.verifyTokenValidity();
    this.sighting = {
      id: '',
      user_id: '',
      report_id: '',
      content: '',
      date_time: '',
      image: '',
      deleted: 0
    }

    this.activatedRoute.queryParams
      .subscribe(params => {
        this.reportId = params['reportId'];
        this.petId = params['petId']
        this.userId = this.authService.getUserId();
      }
    );
    this.initialiseSightingForm();
  }

  handleSightingFormSubmit() {    
    const formData = new FormData();
      formData.append('imageFile', this.imageInput.nativeElement.files[0]);
      this.httpService.request('POST', '/api/upload', formData)
        .subscribe((data: any) => {
          this.sighting.id = crypto.randomUUID().toString();
          this.sighting.user_id = this.userId as string;
          this.sighting.report_id = this.reportId;
          this.sighting.content = this.sightingFormGroup.value.content;
          this.sighting.date_time = this.sightingFormGroup.value.date_time;
          this.sighting.image = data.image;
          this.sighting.deleted = 0;
          
          this.httpService.request('POST', '/api/sightings', this.sighting)
            .subscribe({
              next: (data: any) => {
                alert(data.message);
                this.router.navigate([`/pet-profile/${this.petId}`])
              }
            });
        });
  }

  initialiseSightingForm() {
    this.sightingFormGroup = this.fb.group({
      content: this.fb.control('', Validators.required),
      date_time: this.fb.control('', Validators.required)
    })
  }

}
