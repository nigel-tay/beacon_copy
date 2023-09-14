import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Pet } from 'src/app/interface/pet';
import { Reports } from 'src/app/interface/reports';
import { PetService } from 'src/app/service/pet.service';

@Component({
  selector: 'app-boards',
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.scss']
})
export class BoardsComponent implements OnInit{

  selectedRegion: string = "";
  dependentRegion: string = "";
  reportsList!: Reports[];
  petsList: Pet[] = [];
  currentPage: number = 1;
  pageSize: number = 3;
  totalPages: number[] = [];

  constructor(
    private petService: PetService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.getReportsByRegion(this.currentPage, '');
    this.getTotalPages();
  }

  getReportsByRegion(currentPage: number, region: string) {
    this.petService.getAllReports(currentPage, 6, region)
      .subscribe({
        next: (data: any) => {
          this.reportsList = [...data.reports];

          this.reportsList.forEach((report: any) => {
            this.petsList = [];
            this.petService.getPetData(report.pet_id)
              .subscribe(data => {   
                let pet: Pet = {
                  id: data.id,
                  ownerId: data.owner_id,
                  name: data.name,
                  type: data.type,
                  image: data.image,
                  lost: data.lost
                }
                this.petsList.push(pet);
              })
          })
        },
        error: (data: any) => {
          this.reportsList = [];
          this.petsList = [];
        }
      })
  }

  handleSearchReportsFromRegion(region: string) {
    this.dependentRegion = this.selectedRegion;
    this.getReportsByRegion(this.currentPage, region);
  }

  handlePetClick(petId: string) {
    this.router.navigate(['/pet-profile', petId])
  }

  getTotalPages() {
    this.petService.getTotalReportPages()
      .subscribe(data => {
        
        for (let i = 0; i < data.pages; i++) {
          this.totalPages.push(i+1);
        }
      })
  }

  onPageChange(newPage: number) {
    this.currentPage = newPage;
    
    this.getReportsByRegion(newPage, this.selectedRegion);
  }
  
}
