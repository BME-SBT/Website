import {Component, OnInit} from "@angular/core";
import {SponsorService} from "../shared/sponsor.service";
import {Sponsor} from "../model/sponsor";
import {PictureService} from "../shared/picture.service";
import {TokenStorageService} from "../auth/token-storage.service";
import {AllSponsors} from "../model/all-sponsors";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {ToastrService} from "ngx-toastr";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Globals} from "../globals";

// import AOS from 'aos';
@Component({
    selector: "app-sponsors",
    templateUrl: "./sponsors.component.html",
    styleUrls: ["./sponsors.component.css"],
})
export class SponsorsComponent implements OnInit {
    constructor(
        private sponsorService: SponsorService,
        private pictureService: PictureService,
        private tokenStorage: TokenStorageService,
        private toastr: ToastrService,
        private dialog: MatDialog,
        private globals: Globals
    ) {
    }

    ngOnInit(): void {
        this.checkAuth();
        this.getSponsors();
    }

    allSponsors: AllSponsors;
    sponsorListToUpdateOrder: Sponsor[];
    main: Sponsor[] = [];
    top: Sponsor[] = [];
    other: Sponsor[] = [];
    uni = [];
    partner: Sponsor[] = [];
    bme: Sponsor = new Sponsor();
    newSponsor: Sponsor = new Sponsor();
    failed = false;
    errorMessage: string;
    fileToUpload: File = null;
    files: File[] = [];
    types = ["MAIN", "TOP", "OTHER", "PARTNER", "UNI"];

    public authority: string;
    public roles: string[];

    getSponsors() {
        this.sponsorService.getSponsors().subscribe(
            (res) => {
                this.allSponsors = res;
                this.allSponsors.main.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/" + s.picture)
                );
                this.allSponsors.top.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/" +s.picture)
                );
                this.allSponsors.uni.forEach(
                    (s) => {(s.picture = this.globals.IMG_ROUTE + "sponsors/"+s.picture);
                    console.log(this.allSponsors.uni);
                }
                );
                this.allSponsors.other.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/"+s.picture)
                );
                this.allSponsors.partner.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/"+ s.picture)
                );
                this.splitSponsors();
            },
            (err) => {
                this.showError("Nem sikerült a szponzorok lekérése", 'Error');
            }
        );
    }

    splitSponsors() {
        // this.main = this.allSponsors.filter((s) => s.group == "MAIN");
        // this.top = this.allSponsors.filter((s) => s.group == "TOP");
        // this.other = this.allSponsors.filter((s) => s.group == "OTHER");
        // this.partner = this.allSponsors.filter((s) => s.group == "PARTNER");
        this.bme = this.allSponsors.uni.filter((s) => s.row == 1)[0];
        this.uni = [];
        for (let i = 2; i < 10; i++) {
            var t: Sponsor[] = this.allSponsors.uni.filter(
                s => s.row == i
            );
            console.log(t)
         //   if (t) {
                this.uni.push(t);
          //  }
        }
    }

    onSubmit(event: Event) {
        this.pictureService.postSponsorLogo(this.files[0]).subscribe(
            (data) => {
                event.preventDefault();
                this.addSponsor();
            },
            (error) => {
                this.showError(error.error.message, 'Sikertelen fájlfeltöltés');
            }
        );
    }

    addSponsor() {
        this.sponsorService.postSponsor(this.newSponsor).subscribe(
            (data) => {
                this.showSuccess('Sikeres mentés');
                switch (data.group) {
                    case "main":
                        this.allSponsors.main.push(data);
                        break;
                    case "top":
                        this.allSponsors.top.push(data);
                        break;
                    case "other":
                        this.allSponsors.other.push(data);
                        break;
                    case "partner":
                        this.allSponsors.main.push(data);
                        break;
                    case "uni":
                        this.allSponsors.main.push(data);
                        this.splitSponsors();
                        break;
                    default:
                        break;
                }
                this.splitSponsors();
                this.files = [];
                this.newSponsor = new Sponsor();
            },
            (error) => {
                this.showError(error.error.message, 'Sikertelen mentés');
            }
        );
    }

    delete(sponsor: Sponsor) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztos, hogy törölni szeretnéd a következő szponzort: ' + sponsor.name + '?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                console.log("itt vagyok");
                this.sponsorService.deleteSponsor(sponsor.id).subscribe(
                    (data) => {
                        console.log(data);
                        this.showSuccess('Sikeres törlés');
                        switch (sponsor.group) {
                            case "main":
                                this.main.splice(this.main.indexOf(sponsor), 1);
                                break;
                            case "top":
                                this.top.splice(this.top.indexOf(sponsor), 1);
                                break;
                            case "other":
                                this.other.splice(this.other.indexOf(sponsor), 1);
                                break;
                            case "partner":
                                this.partner.splice(this.partner.indexOf(sponsor), 1);
                                break;
                            case "uni":
                                this.uni.splice(this.uni.indexOf(sponsor), 1);
                                this.splitSponsors();
                                break;
                            default:
                                break;
                        }

                        // var du = this.allSponsors.find((a) => a.id == id);
                        // const index = this.allSponsors.indexOf(du, 0);
                        // if (index > -1) {
                        //   this.allSponsors.splice(index, 1);
                        // }
                        // this.splitSponsores();
                    },
                    (error) => {
                        this.showError(error.error.message, 'Sikertelen törlés');
                    }
                );
            }
        });
    }

    onSelect(event) {
        if (this.files.length > 0) {
            this.files = [];
        }
        this.files.push(...event.addedFiles);
        this.newSponsor.picture = this.files[0].name;
        console.log(this.newSponsor);
    }

    onRemove(event) {
        this.files.splice(this.files.indexOf(event), 1);
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

    dropMain(event: CdkDragDrop<Sponsor[]>) {
        moveItemInArray(this.allSponsors.main, event.previousIndex, event.currentIndex);
        this.updateSponsors(this.allSponsors.main);
    }

    dropTop(event: CdkDragDrop<Sponsor[]>) {
        moveItemInArray(this.allSponsors.top, event.previousIndex, event.currentIndex);
        this.updateSponsors(this.allSponsors.top);
    }

    dropOther(event: CdkDragDrop<Sponsor[]>) {
        moveItemInArray(this.allSponsors.other, event.previousIndex, event.currentIndex);
        this.updateSponsors(this.allSponsors.other);
    }

    dropPartner(event: CdkDragDrop<Sponsor[]>) {
        moveItemInArray(this.allSponsors.partner, event.previousIndex, event.currentIndex);
        this.updateSponsors(this.allSponsors.partner);
    }

    updateSponsors(sponsors) {
        this.sponsorListToUpdateOrder = [];
        this.setOrderNumber(sponsors);


        this.sponsorService.updateOrder(this.sponsorListToUpdateOrder).subscribe(
            (data) => {
                this.allSponsors = data;
                this.allSponsors.main.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/" + s.picture)
                );
                this.allSponsors.top.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/" +s.picture)
                );
                this.allSponsors.uni.forEach(
                    (s) => {(s.picture = this.globals.IMG_ROUTE + "sponsors/"+s.picture);
                    console.log(this.allSponsors.uni);
                }
                );
                this.allSponsors.other.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/"+s.picture)
                );
                this.allSponsors.partner.forEach(
                    (s) => (s.picture = this.globals.IMG_ROUTE + "sponsors/"+ s.picture)
                );
                this.splitSponsors();
                this.showSuccess('Sikeres sorrend módosítás');
            },
            (error) => {
                console.log(error);
                this.showError(error.error.message, 'Sikertelen sorrend módosítás');
            }
        );
    }

    setOrderNumber(sponsorsToAdd: Sponsor[]) {
        let i = 0;
        sponsorsToAdd.forEach(s => {
            s.orderNumber = i;
            i++;
            s.picture= s.picture.split(this.globals.IMG_ROUTE + "sponsors/").join("");
            console.log(s.picture);
            this.sponsorListToUpdateOrder.push(s);
        });
    }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }
}
