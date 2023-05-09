import {Component, OnInit} from '@angular/core';
import {TeamService} from '../shared/team.service';
import {Member} from '../model/member';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {PictureService} from '../shared/picture.service';
import {NgForm} from '@angular/forms';
import {ToastrService} from "ngx-toastr";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {Globals} from "../globals";

@Component({
    selector: 'app-team-editmembers',
    templateUrl: './team-editmembers.component.html',
    styleUrls: ['./team-editmembers.component.css']
})
export class TeamEditmembersComponent implements OnInit {
    members: Member[] = [];
    form: any = {};
    files: File[] = [];
    picturesSelected = false;
    TEAM_URL_BASE = '';

    constructor(private toastr: ToastrService,
                private apiService: TeamService,
                private modalService: NgbModal,
                private pictureService: PictureService,
                private dialog: MatDialog,
                private globals: Globals) {
    }

    ngOnInit(): void {
        this.getMembers();
        this.TEAM_URL_BASE = this.globals.IMG_ROUTE + "/members";
    }

    getMembers() {
        this.members = [];
        this.apiService.getMembers().subscribe(
            (res) => {
                // tslint:disable-next-line:prefer-const
                let data: any = res;
                <Member[]>data.forEach((element) => {
                    this.members.push(element);
                });
            },
            (err) => {
                this.showError(err.error.message, 'Csapattagok lekérése hiba');
            }
        );
    }

    openEditMemberForm(content, member) {
        this.form = JSON.parse(JSON.stringify(member));
        this.modalService.open(content, {scrollable: true, centered: true, size: 'lg'});
    }

    openAddMemberForm(content) {
        this.form = {};
        this.modalService.open(content, {scrollable: true, centered: true, size: 'lg'});
    }

    onSubmit(EditForm: NgForm) {
        if (this.picturesSelected) {
            this.pictureService.postFile(this.form.picture, 'members').subscribe(
                (data) => {
                    this.form.picture =  this.form.picture.name;
                    this.updateMember();
                },
                (error) => {
                    this.showError(error.message, 'Hiba a fájlfeltöltéskor');
                }
            );
        } else {
            this.updateMember();
        }
    }

    updateMember() {
        this.apiService.updateMember(this.form).subscribe((data) => {
                this.showSuccess('Sikeres mentés');
                this.getMembers();
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen mentés');
            });
        this.modalService.dismissAll('put');
        this.form = null;
        this.files = [];
    }

    onSubmitAddForm(AddForm: any) {
        this.pictureService.postFile(this.form.picture, 'members').subscribe(
            (data) => {
                this.form.picture = this.form.picture.name;
                this.addMember();
            },
            (error) => {
                this.showError(error.message, 'Hiba a fájlfeltöltéskor');
            }
        );
    }

    addMember() {
        this.apiService.addMember(this.form).subscribe((data) => {
                this.members.push(data);
                this.showSuccess('Sikeres mentés');
                this.modalService.dismissAll('put');
                this.form = null;
                this.files = [];
            },
            (err) => {
                this.showError(err.error.message, 'Sikertelen törlés');
            });
    }

    deleteMember(id: any) {
        this.modalService.dismissAll('delete');
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a csapattagot?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.apiService.deleteMember(id).subscribe((data) => {
                        this.showSuccess('Sikeres törlés');
                        this.getMembers();
                    },
                    (err) => {
                        this.showError(err.error.message, 'Sikertelen törlés');
                    });
                this.modalService.dismissAll('delete');
                this.form = null;
            }
        });
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
}
