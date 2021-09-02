import { Injectable } from "@angular/core";
import { Sponsor } from "../model/sponsor";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Globals } from "../globals";
import { AllSponsors } from "../model/all-sponsors";

@Injectable({
  providedIn: "root",
})
export class SponsorService {
  private BASE_URL = this.globals.BASE_URL + "/api/sponsor";
  constructor(private http: HttpClient, public globals: Globals) {}
  getSponsors(): Observable<AllSponsors> {
    return this.http.get<AllSponsors>(this.BASE_URL);
  }
  postSponsor(sponsor: Sponsor): Observable<Sponsor> {
    return <Observable<Sponsor>>this.http.post(this.BASE_URL, sponsor);
  }
  deleteSponsor(id: number) {
    return this.http.delete(this.BASE_URL.concat("/").concat(id.toString()));
  }
  updateOrder(sponsors: Sponsor[]):  Observable<AllSponsors> {
    console.log(sponsors);
    return this.http.put<AllSponsors>(this.BASE_URL + "/updateorder", sponsors);
  }
}
