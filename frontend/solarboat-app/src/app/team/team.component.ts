import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {TokenStorageService} from '../auth/token-storage.service';
import {TeamUserviewComponent} from '../team-userview/team-userview.component';
import {TeamEditmembersComponent} from '../team-editmembers/team-editmembers.component';
import {TeamEditgroupsComponent} from '../team-editgroups/team-editgroups.component';
import {MatTabChangeEvent} from '@angular/material/tabs';

@Component({
    selector: 'app-team',
    templateUrl: './team.component.html',
    styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {
    public authority: string;
    public roles: string[];

    @ViewChild(TeamUserviewComponent) private userviewComponent: TeamUserviewComponent;
    @ViewChild(TeamEditmembersComponent) private editmembersComponent: TeamEditmembersComponent;
    @ViewChild(TeamEditgroupsComponent) private editgroupsComponent: TeamEditgroupsComponent;

    constructor(private tokenStorage: TokenStorageService) {
    }

    ngOnInit(): void {
        this.checkAuth();
    }

    checkAuth() {
        this.authority = undefined;
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

    onTabChanged(event: MatTabChangeEvent) {
        if (event.index === 0) {
            this.userviewComponent.ngOnInit();
        }
        if (event.index === 1) {
            this.editmembersComponent.ngOnInit();
        }
        if (event.index === 2) {
            this.editgroupsComponent.ngOnInit();
        }
    }
}
