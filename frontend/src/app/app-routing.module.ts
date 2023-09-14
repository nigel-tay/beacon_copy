import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { BoardsComponent } from './components/boards/boards.component';
import { ReportsComponent } from './components/reports/reports.component';
import { MyProfileComponent } from './components/my-profile/my-profile.component';
import { PetProfileComponent } from './components/pet-profile/pet-profile.component';
import { RegisterComponent } from './components/register/register.component';
import { LogoutComponent } from './components/logout/logout.component';
import { RegisterPetComponent } from './components/register-pet/register-pet.component';
import { SightingComponent } from './components/sighting/sighting.component';

const routes: Routes = [
  {path: "", component: LoginComponent},
  {path: "login", component: LoginComponent},
  {path: "logout", component: LogoutComponent},
  {path: "register", component: RegisterComponent},
  {path: "boards", component: BoardsComponent},
  {path: "reports/:id", component: ReportsComponent},
  {path: "sighting", component: SightingComponent},
  {path: "my-profile/:id", component: MyProfileComponent},
  {path: "pet-profile/:id", component: PetProfileComponent},
  {path: "register-pet", component: RegisterPetComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
