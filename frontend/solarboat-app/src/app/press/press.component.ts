import { Component, OnInit } from "@angular/core";
import { Article } from "../model/article";
import { ArticleService } from "../shared/article.service";
import { TokenStorageService } from "../auth/token-storage.service";
import { Globals } from "../globals";
import { AngularEditorConfig } from "@kolkov/angular-editor";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-press",
  templateUrl: "./press.component.html",
  styleUrls: ["./press.component.css"],
})
export class PressComponent implements OnInit {
  constructor(
    private articleService: ArticleService,
    private toastr: ToastrService,
    private tokenStorage: TokenStorageService,
    private globals: Globals
  ) {
    const currentYear = new Date().getFullYear();
    this.maxDate = new Date(currentYear + 1, 11, 31);
  }

  allArticles: Article[] = [];
  pageNumber = 0;
  isLastPage = false;
  public roles: string[];
  public authority: string;
  form: any = {};
  maxDate: Date;
  files: File[] = [];

  ngOnInit(): void {
    this.getArticles();
    this.checkAuth();
  }

  onSubmit(empForm: any, event: Event) {
    event.preventDefault();
    let article: Object;
    article = {
      title_hu: this.form.title,
      content_hu: this.form.content,
      title_en: this.form.title_en,
      content_en: this.form.content_en,
      link: this.form.link,
      date: this.form.date ? this.globals.formatDate(this.form.date) : null,
    };
    this.saveArticle(empForm, article);
  }

  private saveArticle(empForm: any, article) {
    this.articleService.addArticle(article).subscribe(
      (data) => {
        this.showSuccess("Sikeres mentés");
        this.pushArticle(data);
        this.form = empForm;
        this.files = null;
      },
      (err) => {
        this.showError(err.error.message, "Sikertelen mentés");
      }
    );
  }

  checkAuth() {
    this.authority = undefined;
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every((role) => {
        if (role === "ROLE_ADMIN") {
          this.authority = "admin";
          return false;
        }
        this.authority = "user";
        return true;
      });
    }
  }

  onDeleteArticle(article: Article) {
    this.allArticles = this.allArticles.filter((rowObj) => rowObj.id !== article.id);
  }

  public getArticles() {
    this.articleService.getArticles(this.pageNumber).subscribe(
      (res) => {
        // tslint:disable-next-line:prefer-const
        let data: any = res;
        console.log(data);
        <Article[]>data.content.forEach((element) => {
          this.allArticles.push(element);
        });
        this.pageNumber++;
        this.isLastPage = data.last;
      },
      (err) => {
        console.log(err);
        this.showError(err.error.message, "Hírek sikertelen lekérése");
      }
    );
  }

  private pushArticle(article) {
    let n: Article;
    n = {
      id: article.id,
      title_hu: article.title,
      content_hu: article.content,
      title_en: article.title_en,
      content_en: article.content_en,
      date: article.date,
      link: article.link
    };
    this.allArticles.unshift(n);
  }

  showSuccess(message) {
    this.toastr.success(message);
  }

  showError(message, title) {
    this.toastr.error(message, title);
  }
}
