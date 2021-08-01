import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {News} from '../model/news';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {PictureService} from '../shared/picture.service';
import {NewsService} from '../shared/news.service';
import {AngularEditorConfig} from '@kolkov/angular-editor';
import {ToastrService} from 'ngx-toastr';
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Globals} from "../globals";

@Component({
    selector: 'app-news-preview',
    templateUrl: './news-preview.component.html',
    styleUrls: ['./news-preview.component.css']
})
export class NewsPreviewComponent implements OnInit {
    constructor(private apiService: NewsService, private toastr: ToastrService,
                private modalService: NgbModal, pictureService: PictureService,
                private dialog: MatDialog,
                private globals: Globals) {
        this.pictureService = pictureService;
        const currentYear = new Date().getFullYear();
        this.maxDate = new Date(currentYear + 1, 11, 31);
    }

    shortArticleEn: string;
    shortArticleHu: string;
    form: any = {};
    pictureService: PictureService;
    @Input() authority: string;
    @Input() news: News;
    @Output() onRemove = new EventEmitter<News>();
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

    decodeEntities(str) {
        // this prevents any overhead from creating the object each time
        const element = document.createElement('div');
        if (str && typeof str === 'string') {
            // strip script/html tags
            str = str.replace(/<script[^>]*>([\S\s]*?)<\/script>/gmi, '');
            str = str.replace(/<\/?\w(?:[^"'>]|"[^"]*"|'[^']*')*>/gmi, '');
            element.innerHTML = str;
            str = element.textContent;
            element.textContent = '';
        }
        return str;
    }

    ngOnInit(): void {
        this.shortArticleHu = this.decodeEntities(this.news.content_hu.replace(/<[^>]+>/g, ''));
        this.shortArticleHu = this.shortArticleHu.substring(0, 100) + '...';
        this.shortArticleEn = this.decodeEntities(this.news.content_en.replace(/<[^>]+>/g, ''));
        this.shortArticleEn = this.shortArticleEn.substring(0, 100) + '...';

        this.form.title = this.news.title_hu;
        this.form.content = this.news.content_hu;
        this.form.title_en = this.news.title_en;
        this.form.content_en = this.news.content_en;
        this.form.date = this.news.date;
    }

    openContent(content, lg) {
        this.modalService.open(content, {scrollable: true, centered: true, size: lg ? 'lg' : 'md'});
    }

    delete(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a hírt?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.onRemove.emit(this.news);
                // TODO: kép törlése
                this.apiService.deleteNews(id).subscribe(
                    (res) => {
                        this.showSuccess('Sikeres törlés');
                    },
                    (err) => {
                        this.showError(err.error.message, 'Sikertelen törlés');
                    }
                );
            }
        });
    }

    onSubmit(empForm: any, id: number) {
        this.news.title_hu = this.form.title;
        this.news.content_hu = this.form.content;
        this.news.title_en = this.form.title_en;
        this.news.content_en = this.form.content_en;
        this.news.date = this.form.date ? this.formatDate(this.form.date) : this.formatDate(this.news.date);
        this.news.pictures = this.files.length > 0 ? this.getImageUris() : this.news.pictures;
        if (this.files.length > 0) {
            this.pictureService.postMultipleFiles(this.files, 'news').subscribe(
                (data) => {
                    this.updateNews(empForm, this.news);
                },
                (error) => {
                    this.showError(error.message, 'Hiba a fájlfeltöltéskor');
                }
            );
        } else {
            this.updateNews(empForm, this.news);
        }
    }
    private updateNews(empForm: any, news: News) {
        this.apiService.putNews(news).subscribe(
            (res) => {
                this.showSuccess('Hír módosítva');
                this.modalService.dismissAll('put');
                this.form = empForm;
                // this.ngOnInit();
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen módosítás');
            }
        );
    }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }

    formatDate(date) {
        const newDate = new Date(date);
        const mm = newDate.getMonth() + 1; // getMonth() is zero-based
        const dd = newDate.getDate();

        const resultDate = [newDate.getFullYear(),
            (mm > 9 ? '' : '0') + mm,
            (dd > 9 ? '' : '0') + dd
        ].join('-');

        return resultDate;
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
