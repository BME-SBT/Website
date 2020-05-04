import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { News } from "../model/news";
import { Achievement } from "../model/achievement";
import { GalleryPicture } from "../model/gallery-picture";

@Injectable({
  providedIn: "root",
})
export class ApiService {
  private BASE_URL = "http://localhost:8080/api";
  //private BASE_URL = "http://solarboatteam.hu:81/api";
  constructor(private http: HttpClient) {}

  getNews(pageNum: number) {
    return this.http.get(
      this.BASE_URL + "/news/page/".concat(pageNum.toString())
    );
  }
  getMainpageNews() {
    return this.http.get(this.BASE_URL + '/news/mainpage');
  }
  
  getAchievements(pageNum: number) {
    return this.http.get(
      this.BASE_URL + "/achievement/page/".concat(pageNum.toString())
    );
  }
  getTeams() {
    return this.http.get(this.BASE_URL + '/teams');
  }
  getGallery(): Observable<GalleryPicture[]> {
    return <Observable<GalleryPicture[]>>(
      this.http.get(this.BASE_URL + "/gallery")
    );
  }

  /*  postItem(item: News): Observable<any> {
      return  this.http.post(this.BASE_URL, item);
    }

    updateItem(item: News): Observable<any> {
      return  this.http.put(this.BASE_URL);
    }*/
}
