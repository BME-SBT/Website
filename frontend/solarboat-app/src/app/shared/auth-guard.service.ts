import { Injectable } from "@angular/core";
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
} from "@angular/router";
import { AuthService } from "./auth.service";
import { TokenStorageService } from "../auth/token-storage.service";
import { RoleType } from "../model/role-type.enum";
@Injectable()
export class AuthGuardService implements CanActivate {
  // constructor(public auth: AuthService, public router: Router) {}
  // canActivate(): boolean {
  //   if (!this.auth.isAuthenticated()) {
  //     this.router.navigate(["login"]);
  //     return false;
  //   }
  //   return true;
  // }
  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {}
  roles: string[];

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    this.checkAuth();

    //check some condition
    if (this.roles.indexOf(RoleType.User) == -1) {
      this.router.navigate(["admin"]);
      //redirect to login/home page etc
      //return false to cancel the navigation
      return false;
    } else {
      if (route.data.roles) {
        let role = false;
        this.roles.forEach((r) => {
          if (r == route.data.roles) {
            role = true;
          }
        });
        return role;
      } else {
        return true;
      }
    }
  }

  checkAuth() {
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      console.log(this.roles);
    }
  }
}
