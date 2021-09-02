import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../model/user";
import {Globals} from '../globals';

@Injectable({
  providedIn: "root",
})
export class UserService {
  private BASE_URL = this.globals.BASE_URL + "/api/user";

  constructor(private http: HttpClient, public globals: Globals) {}

  getUsers(): Observable<any[]> {
    return <Observable<any[]>>this.http.get(this.BASE_URL);
  }
  updateUser(user: User){
    var v = {
      
    }
    return this.http.patch(this.BASE_URL, user);
  }

  deleteUser(id: number){
    return this.http.delete(this.BASE_URL.concat("/").concat(id.toString()));
  }
}
