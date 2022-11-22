import {NgModule} from "@angular/core";
import {Routes, RouterModule, CanActivate} from "@angular/router";
import {MainpageComponent} from "./mainpage/mainpage.component";
import {TeamComponent} from "./team/team.component";
import {NewsComponent} from "./news/news.component";
import {AchievementsComponent} from "./achievements/achievements.component";
import {SponsorsComponent} from "./sponsors/sponsors.component";
import {RegisterComponent} from "./admin/register/register.component";
import {LoginComponent} from "./login/login.component";
import {GalleryComponent} from "./gallery/gallery.component";
import {UsersComponent} from "./admin/users/users.component";
import {DataVisualizationComponent} from "./data-visualization/data-visualization.component";
import {AuthGuardService} from "./shared/auth-guard.service";
import {RoleType} from "./model/role-type.enum";
import { PressComponent } from "./press/press.component";
import { FileStorageComponent } from "./file-storage/file-storage.component";
import { GalleryMainPageComponent } from "./gallery-main-page/gallery-main-page.component";

const routes: Routes = [
    // {path: "gallery", component: GalleryComponent},
    {path: "gallery", component: GalleryMainPageComponent},
    {path: "gallery/:galleryId", component: GalleryComponent},
    {path: "auth/login", component: LoginComponent},
    {path: "signup", component: RegisterComponent},
    {path: "team", component: TeamComponent},
    {path: "mainpage", component: MainpageComponent},
    {path: "news", component: NewsComponent},
    {path: "achievements", component: AchievementsComponent},
    {path: "sponsors", component: SponsorsComponent},
    {path: "press", component: PressComponent},
    {path: "boatdata", component: DataVisualizationComponent, canActivate: [AuthGuardService]},
    {path: "admin", redirectTo: "/auth/login"},
    {path: "users", component: UsersComponent, canActivate: [AuthGuardService], data: {roles: [RoleType.Admin]}},
    {path: "fileStorage", component: FileStorageComponent},
    {path: "", redirectTo: "/mainpage", pathMatch: "full"},
    {path: "**", redirectTo: "/mainpage", pathMatch: "full"},
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true})],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
