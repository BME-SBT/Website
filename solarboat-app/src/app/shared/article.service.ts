import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Globals} from '../globals';
import {Article} from '../model/article';
import {Observable} from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private BASE_URL = this.globals.BASE_URL + "/api/article";

  constructor(private http: HttpClient, public globals: Globals) {
  }
  getArticles(pageNum: number): Observable<any> {
    return this.http.get(
        this.BASE_URL + '/page/'.concat(pageNum.toString())
    );
}

deleteArticle(id: number): Observable<any> {
    return this.http
        .delete(this.BASE_URL + '/' + id);
}

putArticle(o: Article): Observable<any> {
    return this.http
        .put(this.BASE_URL, o);
}

addArticle(o: Object): Observable<any> {
    return this.http
        .post(this.BASE_URL, o);
}
}
