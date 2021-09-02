import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { map } from "rxjs/operators";
import { TokenStorageService } from "../auth/token-storage.service";
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: "app-nav-bar",
  templateUrl: "./nav-bar.component.html",
  styleUrls: ["./nav-bar.component.css"],
})
export class NavBarComponent implements OnInit {
  public page: string;

  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService,
    public translate: TranslateService
  ) {
    this.page = this.router.url;
  }

  public roles: string[];
  public authority: string;

  ngOnInit() {
    this.checkAuth();
  }
  logout() {
    this.tokenStorage.signOut();
    this.checkAuth();
  }
  checkAuth() {
    this.authority = undefined;
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every((role) => {
        if (role === "ROLE_ADMIN") {
          this.authority = "admin";
          return false;
        }
        this.authority = "user";
        return true;
      });
    }
  }

}
