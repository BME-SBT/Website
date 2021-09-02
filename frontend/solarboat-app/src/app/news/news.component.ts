import {Component, OnInit} from '@angular/core';
import {News} from '../model/news';
import {NewsService} from '../shared/news.service';
import {TokenStorageService} from '../auth/token-storage.service';
import {PictureService} from '../shared/picture.service';
import {Globals} from '../globals';
import {AngularEditorConfig} from '@kolkov/angular-editor';
import {ToastrService} from 'ngx-toastr';

@Component({
    selector: 'app-news',
    templateUrl: './news.component.html',
    styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {
    constructor(private apiService: NewsService, private toastr: ToastrService,
                private tokenStorage: TokenStorageService, pictureService: PictureService, private globals: Globals) {
        this.pictureService = pictureService;
        const currentYear = new Date().getFullYear();
        this.maxDate = new Date(currentYear + 1, 11, 31);
    }

    allnews: News[] = [];
    pageNumber = 0;
    isLastPage = false;
    public roles: string[];
    public authority: string;
    form: any = {};
    pictureService: PictureService;
    config: AngularEditorConfig = {
        editable: true,
        spellcheck: true,
        height: '15rem',
        minHeight: '5rem',
        placeholder: 'Hír szövege...',
        translate: 'no',
        defaultParagraphSeparator: 'p',
        defaultFontName: 'Roboto',
        fonts: [
            {class: 'arial', name: 'Arial'},
            {class: 'times-new-roman', name: 'Times New Roman'},
            {class: 'calibri', name: 'Calibri'},
            {class: 'Roboto', name: 'Roboto'},
            {class: 'KelsonSans-Normal', name: 'KelsonSans-Normal'},
            {class: 'KelsonSans-Light', name: 'KelsonSans-Light'},
            {class: 'KelsonSans-Bold', name: 'KelsonSans-Bold'}
        ],
        toolbarHiddenButtons: [
            [
                'textColor',
                'backgroundColor',
                'customClasses',
                'unlink',
                'link',
                'insertVideo',
                'insertImage',
                'insertHorizontalRule'
            ]
        ]
    };
    maxDate: Date;
    files: File[] = [];

    ngOnInit(): void {
        this.getNews();
        this.checkAuth();
    }

    onSubmit(empForm: any, event: Event) {
        event.preventDefault();
        let news: Object;
        news = {
            title_hu: this.form.title,
            content_hu: this.form.content,
            title_en: this.form.title_en,
            content_en: this.form.content_en,
            date: this.form.date ? this.globals.formatDate(this.form.date) : null,
            pictures: this.files.length > 0 ? this.getImageUris() : null
        };
        if (this.files.length > 0) {
            this.pictureService.postMultipleFiles(this.files, 'news').subscribe(
                (data) => {
                    this.saveNews(empForm, news);
                },
                (error) => {
                    this.showError(error.message, 'Hiba a fájlfeltöltéskor');
                }
            );
        } else {
            this.saveNews(empForm, news);
        }
    }

    private saveNews(empForm: any, news) {
        this.apiService.addNews(news).subscribe(
            (data) => {
                this.showSuccess('Sikeres mentés');
                this.pushNews(data);
                this.form = empForm;
                this.files = null;
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen mentés');
            });
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

    onDeleteNews(news: News) {
        this.allnews = this.allnews.filter(rowObj => rowObj.id !== news.id);
    }

    public getNews() {
        this.apiService.getNews(this.pageNumber).subscribe(
            (res) => {
                // tslint:disable-next-line:prefer-const
                let data: any = res;
                 console.log(data);
                <News[]>data.content.forEach((element) => {
                    this.allnews.push(element);
                });
                this.pageNumber++;
                this.isLastPage = data.last;
            },
            (err) => {
                console.log(err);
                this.showError(err.error.message, 'Hírek sikertelen lekérése');
            }
        );
    }

    private pushNews(news) {
        let n: News;
        n = {
            id: news.id,
            title_hu: news.title,
            content_hu: news.content,
            title_en: news.title_en,
            content_en: news.content_en,
            date: news.date,
            pictures: news.pictures
        };
        this.allnews.unshift(n);
    }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }

    onSelectFile(event) {
        this.files.push(...event.addedFiles);
    }

    onRemoveFile(event) {
        this.files.splice(this.files.indexOf(event), 1);

    }

    private getImageUris() {
        let pictures: string[] = [];
        for (let image of this.files) {
            pictures.push(this.globals.IMG_ROUTE + 'news/' + image.name);
        }
        return pictures;
    }
}
