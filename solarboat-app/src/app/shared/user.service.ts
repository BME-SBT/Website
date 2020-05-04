import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../model/user";

@Injectable({
  providedIn: "root",
})
export class UserService {
  private userUrl = "http://localhost:8080/api/user";
  //private BASE_URL = "http://solarboatteam.hu:81/api";

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return <Observable<User[]>>this.http.get(this.userUrl);
  }
}
