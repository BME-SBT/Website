import {Component, OnInit} from '@angular/core';
import {Team} from '../model/team';
import {TeamService} from '../shared/team.service';
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-team-userview',
    templateUrl: './team-userview.component.html',
    styleUrls: ['./team-userview.component.css']
})
export class TeamUserviewComponent implements OnInit {
    leaders: Team;
    electronics: Team;
    economics: Team;
    mechatronics: Team;
    mechatronicsMaterial: Team;
    mechatronicsSimulation: Team;

    constructor(private toastr: ToastrService, private apiService: TeamService) {
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
                this.showError(err.error.message, 'Csapatok lekérése hiba');
            }
        );
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }

}

