import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Member} from '../model/member';
import {HttpClient} from '@angular/common/http';
import {TeamService} from '../shared/team.service';
import {MatSelectChange} from '@angular/material/select';
import {ToastrService} from "ngx-toastr";

interface Food {
    value: string;
    viewValue: string;
}

@Component({
    selector: 'app-team-editgroup-panel',
    templateUrl: './team-editgroup-panel.component.html',
    styleUrls: ['./team-editgroup-panel.component.css']
})
export class TeamEditgroupPanelComponent implements OnInit {
    @Input() members: Member[];
    @Input() leader: Member;
    @Input() isLeader: boolean;
    @Input() teamId: number;
    @Output() onChange = new EventEmitter<number>();
    @Output() onChangedLeaderOfTeam = new EventEmitter<number>();
    allmember: Member[];
    selected = null;

    constructor(private apiService: TeamService, private toastr: ToastrService,) {
    }

    ngOnInit() {
        this.getMembers();
        this.selected = this.leader.id;
    }

    getMembers() {
        this.allmember = [];
        this.apiService.getMembers().subscribe(
            (res) => {
                // tslint:disable-next-line:prefer-const
                let data: any = res;
                <Member[]>data.forEach((element) => {
                    this.allmember.push(element);
                });
            },
            (err) => {
                this.showError(err.error.message, 'Tagok sikertelen lekérése');
            }
        );
    }

    deleteMemberFromTeam(memberId: number) {
        this.apiService.removeMemberFromTeam(memberId, this.teamId).subscribe((data) => {
                this.onChange.emit(memberId);
                this.showSuccess('Tag sikeres eltávolítása');
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen eltávolítás');
            });
    }

    addMember($event: MatSelectChange) {
        this.apiService.addMemberToTeam($event.value, this.teamId).subscribe((data) => {
                this.onChange.emit($event.value);
                this.showSuccess('Tag sikeresen hozzáadva  csapathoz');
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen hozzáadás');
            });
    }

    changedMember(member: Member) {
        this.apiService.updateMember(member).subscribe((data) => {
                this.showSuccess('Sikeres mentés');
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen mentés');
            });
    }

    changeLeader($event: MatSelectChange) {
        console.log($event);
        this.onChangedLeaderOfTeam.emit($event.value);
    }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }
}
