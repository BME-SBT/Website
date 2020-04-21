import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {News} from '../model/news';
import {Achievement} from '../model/achievement';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private BASE_URL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {

  }

  getNews(): Observable<News[]> {
    return this.http.get<News[]>(this.BASE_URL + '/news');
  }
  
  getAchievements(pageNum: number) {
    return this.http.get(
        this.BASE_URL + '/achievement/page/'.concat(pageNum.toString())
    );
  }

  /*  postItem(item: News): Observable<any> {
      return  this.http.post(this.BASE_URL, item);
    }

    updateItem(item: News): Observable<any> {
      return  this.http.put(this.BASE_URL);
    }*/
}

