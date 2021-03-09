import {Component, OnInit} from '@angular/core';
import {Team} from '../model/team';
import {HttpClient} from '@angular/common/http';
import {TeamService} from '../shared/team.service';

@Component({
    selector: 'app-team-editgroups',
    templateUrl: './team-editgroups.component.html',
    styleUrls: ['./team-editgroups.component.css']
})
export class TeamEditgroupsComponent implements OnInit {

    leaders: Team;
    electronics: Team;
    economics: Team;
    mechatronics: Team;
    mechatronicsMaterial: Team;
    mechatronicsSimulation: Team;

    constructor(private http: HttpClient, private apiService: TeamService) {
    }

    ngOnInit(): void {
        this.getTeams();
    }

    public getTeams() {
        this.apiService.getTeams().subscribe(
            (res) => {
                let data: any = res;
                <Team[]> data.forEach((element) => {
                    if (element.teamType === 1) {
                        this.leaders = element;
                    }
                    if (element.teamType === 2) {
                        this.electronics = element;
                    }
                    if (element.teamType === 3) {
                        this.economics = element;
                    }
                    if (element.teamType === 4) {
                        this.mechatronics = element;
                    }
                    if (element.teamType === 5) {
                        this.mechatronicsMaterial = element;
                    }
                    if (element.teamType === 6) {
                        this.mechatronicsSimulation = element;
                    }
                });
            },
            (err) => {
                alert('get error');
            }
        );
    }

    onChange($event: any) {
        this.ngOnInit();
    }

    onChangedLeaderOfTeam(memberId: number, teamId: number) {
        this.apiService.updateLeaderOfTeam(memberId, teamId).subscribe((data) => {
            this.ngOnInit();
        });
    }
}

