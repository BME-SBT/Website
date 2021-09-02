import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../globals';
import {Member} from '../model/member';
import {TeamTexts} from "../model/team-texts";

@Injectable({
    providedIn: 'root',
})
export class TeamService {
    private BASE_URL = this.globals.BASE_URL + '/api';

    constructor(private http: HttpClient, public globals: Globals) {
    }

    getTeams() {
        return this.http.get(this.BASE_URL + '/team');
    }

    getMembers() {
        return this.http.get(this.BASE_URL + '/member');
    }

    updateMember(member: Member) {
        return this.http
            .put(this.globals.BASE_URL + '/api/member/' + member.id, member);
    }

    addMember(member: any): Observable<any> {
        return this.http
            .post(this.globals.BASE_URL + '/api/member', member);
    }

    removeMemberFromTeam(memberId: number, teamId: number) {
        return this.http
            .delete(this.globals.BASE_URL + '/api/team/' + teamId + '/members/' + memberId);
    }

    deleteMember(memberId: any) {
        return this.http
            .delete(this.globals.BASE_URL + '/api/member/' + memberId);
    }

    addMemberToTeam(memberId: number, teamId: number) {
        return this.http
            .post(this.globals.BASE_URL + '/api/team/' + teamId + '/members/' + memberId, null);
    }

    updateLeaderOfTeam(memberId: number, teamId: number) {
        return this.http
            .put(this.globals.BASE_URL + '/api/team/' + teamId + '/leader/' + memberId, null);
    }

    updateDescriptionOfTeam(teamId: any, texts: TeamTexts) {
        return this.http
            .put(this.globals.BASE_URL + '/api/team/' + teamId + '/description', texts);
    }

    updateNameOfTeam(teamId: any, texts: TeamTexts) {
        return this.http
            .put(this.globals.BASE_URL + '/api/team/' + teamId + '/name', texts);
    }
}
