import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {Video} from "../model/video";
import {Globals} from '../globals';

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private http: HttpClient, public globals: Globals) {}

  BASE_URL = this.globals.BASE_URL;

  postVideoLink(video: Video):  Observable<Video> {
    return <Observable<Video>> this.http.post(this.BASE_URL.concat("/api/videolink"), video);
  }
  getAllLinks(): Observable<Video[]> {
    return <Observable<Video[]>>(
      this.http.get(this.BASE_URL + "/api/videolink")
    );
  }
  deleteVideo(id: number){
    return this.http.delete(this.BASE_URL.concat("/api/videolink/").concat(id.toString()));
  }


}
