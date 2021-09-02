import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Article} from '../model/article';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ArticleService} from '../shared/article.service';
import {ToastrService} from 'ngx-toastr';
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Globals} from "../globals";

@Component({
  selector: 'app-press-preview',
  templateUrl: './press-preview.component.html',
  styleUrls: ['./press-preview.component.css']
})
export class PressPreviewComponent implements OnInit {
    constructor(private articleService: ArticleService, 
                private toastr: ToastrService,
                private modalService: NgbModal, 
                private dialog: MatDialog,
                private globals: Globals) {
        const currentYear = new Date().getFullYear();
        this.maxDate = new Date(currentYear + 1, 11, 31);
    }

    form: any = {};
    @Input() authority: string;
    @Input() article: Article;
    @Output() onRemove = new EventEmitter<Article>();
  
    maxDate: Date;

    ngOnInit(): void {
        this.form.title = this.article.title_hu;
        this.form.content = this.article.content_hu;
        this.form.title_en = this.article.title_en;
        this.form.content_en = this.article.content_en;
        this.form.date = this.article.date;
        this.form.link = this.article.link;
    }

    openLink(link){
      window.open(link);
    }

    openContent(content, lg) {
        this.modalService.open(content, {scrollable: true, centered: true, size: lg ? 'lg' : 'md'});
    }

    delete(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a cikket?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.onRemove.emit(this.article);
                // TODO: kép törlése
                this.articleService.deleteArticle(id).subscribe(
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
        this.article.title_hu = this.form.title;
        this.article.content_hu = this.form.content;
        this.article.title_en = this.form.title_en;
        this.article.content_en = this.form.content_en;
        this.article.date = this.form.date ? this.formatDate(this.form.date) : this.formatDate(this.article.date);
        this.updateArticle(empForm, this.article);
    }
    private updateArticle(empForm: any, article: Article) {
        this.articleService.putArticle(article).subscribe(
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
}
