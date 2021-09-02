import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Dates } from "./model/dates";
import {Globals} from './globals';

@Injectable({
  providedIn: "root",
})
export class BoatDataService {
  private BASE_URL = this.globals.BASE_URL + "/api/dataGroup";
  httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: "fda66d02-9f2f-492e-9327-64bc68a7e509",
    }),
  };
  // tilt: MyCoordinates[] = [];
  constructor(private http: HttpClient, public globals: Globals) {}

  public getDataGroupById(id: number) {
    return this.http.get(
      this.BASE_URL.concat("/response/").concat(id.toString())
    );
  }

  public getLastClosedDataGroup() {
    return this.http.get(this.BASE_URL + "/lastclosed");
  }
  public getActiveDataGroup() {
    return this.http.get(this.BASE_URL + "/active");
  }
  public getDate() {
    return this.http.get<Dates[]>(this.BASE_URL.concat("/ids"));
  }
  public deleteAll() {
    this.http.delete(this.BASE_URL).subscribe((res) => {});
  }
  public deleteById(id: number) {
    this.http
        .delete(this.BASE_URL + '/' + id.toString() )
      .subscribe((res) => {});
  }
}
