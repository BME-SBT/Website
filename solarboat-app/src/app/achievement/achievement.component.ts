import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Achievement} from '../model/achievement';
import {PictureService} from '../shared/picture.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AchievementService} from '../shared/achievement.service';
import {Globals} from '../globals';
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

// import AOS from 'aos';

@Component({
    selector: 'app-achievement',
    templateUrl: './achievement.component.html',
    styleUrls: ['./achievement.component.css']
})
export class AchievementComponent implements OnInit {

    constructor(private globals: Globals, private apiService: AchievementService, private dialog: MatDialog,
                private modalService: NgbModal, pictureService: PictureService, private toastr: ToastrService) {
        this.pictureService = pictureService;
        const currentYear = new Date().getFullYear();
        this.maxDate = new Date(currentYear + 1, 11, 31);
    }

    form: any = {};
    failed = false;
    errorMessage = '';
    pictureService: PictureService;
    files: File[] = [];
    picturesSelected = false;

    @Input() authority: string;
    @Input() achievement: Achievement;
    @Output() onRemove = new EventEmitter<Achievement>();
    maxDate: Date;
    animal: string;
    name: string;

    ngOnInit(): void {
        this.form.title_hu = this.achievement.title_hu;
        this.form.location_hu = this.achievement.location_hu;
        this.form.title_en = this.achievement.title_en;
        this.form.location_en = this.achievement.location_en;
        this.form.place_hu = this.achievement.place_hu;
        this.form.place_en = this.achievement.place_en;
        this.form.date = this.achievement.date;
    }

    delete(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni az eredményt?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.onRemove.emit(this.achievement);
                // TODO: kép törlése
                this.apiService.deleteAchievement(id).subscribe(
                    (res) => {
                        this.showSuccess('Sikeres törlés');
                    },
                    (err) => {
                        this.showError(err.message, 'Sikertelen törlés');
                    }
                );
            }
        });
    }

    openContent(longContent) {
        this.modalService.open(longContent, {scrollable: true, centered: true, size: 'lg'});
    }

    onSubmit(empForm: any, id: number) {
        this.achievement.title_hu = this.form.title_hu;
        this.achievement.location_hu = this.form.location_hu;
        this.achievement.title_en = this.form.title_en;
        this.achievement.location_en = this.form.location_en;
        this.achievement.date = this.form.date ? this.globals.formatDate(this.form.date) : this.achievement.date;
        this.achievement.place_hu = this.form.place_hu;
        this.achievement.place_en = this.form.place_en;
        const achievementId = id;
        const achievement = {
            id: achievementId,
            title_hu: this.form.title_hu,
            location_hu: this.form.location_hu,
            title_en: this.form.title_en,
            location_en: this.form.location_en,
            description_hu: "leiras",
            description_en: "description",
            date: this.form.date ? this.globals.formatDate(this.form.date) : this.achievement.date,
            place_hu: this.form.place_hu,
            place_en: this.form.place_en,
            isLast: false,
            picture: this.files.length > 0 ? this.globals.IMG_ROUTE + 'achievement/' + this.files[0].name : this.achievement.picture
        };
        if (this.files.length > 0) {
            this.pictureService.postFile(this.form.picture, 'achievement').subscribe(
                (data) => {
                    this.updateAchievement(achievement);
                },
                (error) => {
                    this.showError(error.message, 'Hiba a fájlfeltöltéskor');
                }
            );
        } else {
            this.updateAchievement(achievement);
        }
    }

    private updateAchievement(achievement) {
        this.apiService.updateAchievement(achievement).subscribe(
            (res) => {
                this.showSuccess('Sikeres mentés');
            },
            (err) => {
                this.showError(err.message, 'Sikertelen mentés');
            }
        );
        this.modalService.dismissAll('put');
    }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }

    onSelectFile(event) {
        if (this.files.length > 0) {
            this.files = [];
        }
        this.files.push(...event.addedFiles);
        this.form.picture = this.files[0];
        this.picturesSelected = true;
    }

    onRemoveFile(event) {
        this.files.splice(this.files.indexOf(event), 1);
        this.picturesSelected = false;

    }

    openDialog() {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '250px',
            data: 'Biztosan ki szeretnéd törölni?'
        });

        dialogRef.afterClosed().subscribe(result => {
            console.log('The dialog was closed');
            console.log(result);
        });
    }
}
