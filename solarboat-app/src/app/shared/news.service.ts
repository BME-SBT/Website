import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import {Globals} from '../globals';
import {News} from '../model/news';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: "root",
})
export class NewsService {
  private BASE_URL = this.globals.BASE_URL + "/api";
  constructor(private http: HttpClient, public globals: Globals) {}

  getNews(pageNum: number) {
    return this.http.get(
        this.BASE_URL + "/news/page/".concat(pageNum.toString())
    );
  }
  getMainpageNews() {
    return this.http.get(this.BASE_URL + '/news/mainpage');
  }
  deleteNews(id: number) {
    this.http
        .delete(this.globals.BASE_URL + '/api/news/' + id)
        .subscribe((data) => {
          // console.log(data);
        });
  }
  putNews(o: News ) {
   this.http
      .put(this.globals.BASE_URL + '/api/news', o)
      .subscribe((data) => {
        // console.log(data);
      });
  }

  addNews(o: Object): Observable<any> {
    return this.http
        .post(this.globals.BASE_URL + '/api/news', o);
  }

}
