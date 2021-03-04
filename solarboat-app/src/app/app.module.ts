import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule, NgForm } from "@angular/forms";
import { HttpClientModule, HttpClient } from "@angular/common/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { MainpageComponent } from "./mainpage/mainpage.component";
import { TeamComponent } from "./team/team.component";
import { FooterComponent } from "./footer/footer.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { NavBarComponent } from "./nav-bar/nav-bar.component";
import { MDBBootstrapModule } from "angular-bootstrap-md";
import { AboutusComponent } from "./aboutus/aboutus.component";
import { MainpageNewsComponent } from "./mainpage-news/mainpage-news.component";
import { NewsPreviewComponent } from "./news-preview/news-preview.component";
import { CoverComponent } from "./cover/cover.component";
import { NewsComponent } from "./news/news.component";
import { ScrollToModule } from "ng2-scroll-to-el";
import { BoatDataComponent } from "./boat-data/boat-data.component";
import { GraphComponent } from "./graph/graph.component";
import { NgxChartsModule } from "@swimlane/ngx-charts";
import { BarChartComponent } from "./bar-chart/bar-chart.component";
import { SponsorsComponent } from "./sponsors/sponsors.component";
import { AchievementsComponent } from "./achievements/achievements.component";
import { AchievementComponent } from "./achievement/achievement.component";
import { LoginComponent } from "./login/login.component";
import { RegisterComponent } from "./admin/register/register.component";
import { TeamMembersComponent } from "./team-members/team-members.component";
import {
  AuthGuardService as AuthGuard,
  AuthGuardService,
} from "./shared/auth-guard.service";
import { httpInterceptorProviders } from "./auth/auth-interceptor";
import { GalleryComponent } from "./gallery/gallery.component";
import { JwtModule } from "@auth0/angular-jwt";
import { UsersComponent } from "./admin/users/users.component";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { Globals } from "./globals";
import { AngularEditorModule } from "@kolkov/angular-editor";
import { NotificationsRxComponent } from "./notifications-rx/notifications-rx.component";
import { DataVisualizationComponent } from "./data-visualization/data-visualization.component";
import { MatTabsModule } from "@angular/material/tabs";
import { StatisticsComponent } from "./statistics/statistics.component";
import { MatDialogModule } from "@angular/material/dialog";
import { ConfirmComponent } from "./confirm/confirm.component";
import { CompassComponent } from "./compass/compass.component";
import { MileageComponent } from "./mileage/mileage.component";
import { WavesComponent } from "./waves/waves.component";
import { NgxDropzoneModule } from 'ngx-dropzone';
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    MainpageComponent,
    TeamComponent,
    FooterComponent,
    NavBarComponent,
    AboutusComponent,
    MainpageNewsComponent,
    NewsPreviewComponent,
    CoverComponent,
    NewsComponent,
    BoatDataComponent,
    GraphComponent,
    AchievementsComponent,
    AchievementComponent,
    BarChartComponent,
    SponsorsComponent,
    LoginComponent,
    RegisterComponent,
    TeamMembersComponent,
    GalleryComponent,
    NavBarComponent,
    UsersComponent,
    NotificationsRxComponent,
    DataVisualizationComponent,
    StatisticsComponent,
    ConfirmComponent,
    CompassComponent,
    MileageComponent,
    WavesComponent,
  ],
  // tslint:disable-next-line:max-line-length
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    MatTooltipModule,
    MatIconModule,
    BrowserAnimationsModule,
    MDBBootstrapModule,
    ScrollToModule.forRoot(),
    NgxChartsModule,
    CommonModule,
    JwtModule,
    AngularEditorModule,
    MatTabsModule,
    MatDialogModule,
    ReactiveFormsModule,
    NgxDropzoneModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],

  bootstrap: [AppComponent],

  providers: [
    httpInterceptorProviders,
    AuthGuard,
    AuthGuardService,
    JwtModule,
    Globals,
  ],
})
export class AppModule {}
