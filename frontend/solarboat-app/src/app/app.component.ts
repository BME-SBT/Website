import {NavigationEnd, Router} from '@angular/router';

import { Component, OnInit, Input } from "@angular/core";
import { TokenStorageService } from "./auth/token-storage.service";
import { TranslateService} from "@ngx-translate/core"

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'solarboat-app';
    public roles: string[];
    public authority: string;

constructor(private router: Router, private tokenStorage: TokenStorageService,
            public translate: TranslateService) {
    this.router.events.subscribe(event => {
        if (event instanceof NavigationEnd) {
            (window as any).ga('set', 'page', event.urlAfterRedirects);
            (window as any).ga('send', 'pageview');
        }
    });
    translate.addLangs(['en', 'hu']);
    translate.setDefaultLang('en');
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|hu/) ? browserLang : 'en');
}

    ngOnInit() {
        if (this.tokenStorage.getToken()) {
            this.roles = this.tokenStorage.getAuthorities();
            this.roles.every((role) => {
                if (role === 'ROLE_ADMIN') {
                    this.authority = 'admin';
                    return false;
                }
                this.authority = 'user';
                return true;
            });
        }
    }
}
