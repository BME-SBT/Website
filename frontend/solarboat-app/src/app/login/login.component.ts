import {Component, OnInit} from "@angular/core";

import {AuthService} from "../auth/auth.service";
import {TokenStorageService} from "../auth/token-storage.service";
import {AuthLoginInfo} from "../auth/login-info";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
    form: any = {};
    isLoggedIn = false;
    isLoginFailed = false;
    errorMessage = "";
    roles: string[] = [];
    private loginInfo: AuthLoginInfo;

    constructor(
        private authService: AuthService,
        private tokenStorage: TokenStorageService,
        private router: Router,
        private toastr: ToastrService
    ) {
    }

    ngOnInit() {
        if (this.tokenStorage.getToken()) {
            this.isLoggedIn = true;
            this.roles = this.tokenStorage.getAuthorities();
        }
    }

    onSubmit() {
        this.loginInfo = new AuthLoginInfo(this.form.username, this.form.password);

        this.authService.attemptAuth(this.loginInfo).subscribe(
            (data) => {
                this.tokenStorage.saveToken(data.accessToken);
                this.tokenStorage.saveUsername(data.username);
                this.tokenStorage.saveAuthorities(data.authorities);

                this.isLoginFailed = false;
                this.isLoggedIn = true;
                this.roles = this.tokenStorage.getAuthorities();
                this.router.navigateByUrl("/mainpage");
            },
            (error) => {
                this.showError('Hibás felhasználónév vagy jelszó');
                this.errorMessage = error.error.message;
                this.isLoginFailed = true;
            }
        );
    }

    showError(message) {
        this.toastr.error(message);
    }
}
