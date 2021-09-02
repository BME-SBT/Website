import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import {Globals} from '../globals';

@Injectable({
  providedIn: "root",
})
export class AchievementService {
  private BASE_URL = this.globals.BASE_URL;
  constructor(private http: HttpClient, public globals: Globals) {}

  getAchievements(pageNum: number) {
    return this.http.get(
        this.BASE_URL + '/api/achievement/page/'.concat(pageNum.toString())
    );
  }

  addAchievement(o: Object) {
    return this.http
        .post(this.globals.BASE_URL + "/api/achievement", o);
  }

  deleteAchievement(id: number) {
    return this.http
        .delete(this.globals.BASE_URL + '/api/achievement/'.concat(id.toString()));
  }

  updateAchievement(o: Object) {
    return this.http
        .put(this.globals.BASE_URL + "/api/achievement", o);
  }
}
