import {PictureService} from "../shared/picture.service";
import {Component, OnInit, Output, HostListener, Pipe, PipeTransform} from "@angular/core";
import {Router} from "@angular/router";
import {TokenStorageService} from "../auth/token-storage.service";
import {GalleryPicture} from "../model/gallery-picture";
import {GalleryPictureRequest} from "../model/gallery-picture-request";
import {ToastrService} from "ngx-toastr";
import {Video} from "../model/video";
import {VideoService} from "../shared/video.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";


// import AOS from 'aos';

@Component({
    selector: "app-gallery",
    templateUrl: "./gallery.component.html",
    styleUrls: ["./gallery.component.css"],
})
export class GalleryComponent implements OnInit {
    constructor(
        private pictureService: PictureService,
        private router: Router,
        private tokenStorage: TokenStorageService,
        private videoService: VideoService,
        private toastr: ToastrService,
        private dialog: MatDialog
    ) {
    }

    @Output() gallery: GalleryPicture[];
    newPicture: GalleryPictureRequest;
    @Output() videos: Video[];
    newVideo: Video;
    failed = false;
    videoFailed = false;
    errorMessage = "";
    picturesSelected = false;
    // pic = false;
    // smallPic = false;
    public authority: string;
    public roles: string[];
    public largeWidth: boolean;
    fileToUpload: File;
    files: File[] = [];

    ngOnInit(): void {
        // AOS.init();
        this.checkAuth();
        this.loadGallery();
        this.loadVideos();
        this.newPicture = new GalleryPictureRequest();
        this.newVideo = new Video();
        this.largeWidth = (window.innerWidth < 768) ? false : true;
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
        this.largeWidth = (window.innerWidth < 768) ? false : true;
        console.log(this.largeWidth);
    }

    // handleFileInput(files: FileList) {
    //   this.fileToUpload = files.item(0);
    //   this.newPicture.picture = files.item(0);
    //   console.log(this.newPicture);
    //   this.pic = true;
    //   if (this.pic) {
    //     this.picturesSelected = true;
    //   }
    // }
    onSelect(event) {
        if (this.files.length > 0) {
            this.files = [];
        }
        this.files.push(...event.addedFiles);
        this.newPicture.picture = this.files[0];
        this.picturesSelected = true;
    }

    onRemove(event) {
        this.files.splice(this.files.indexOf(event), 1);
        this.picturesSelected = false;

    }

    uploadGalleryPicture(event, empForm: any) {

        if (this.newPicture.title_hu == null) {
            //this.newPicture.title_hu = ' ';
        }
        if (this.newPicture.title_en == null) {
            //this.newPicture.title_en = ' ';
        }
        this.pictureService.postGalleryPicture(this.newPicture).subscribe(
            (data) => {
                event.preventDefault();
                // do something, if upload success
                //this.uploadFileToActivity();
                this.newPicture = new GalleryPictureRequest();
                this.gallery.push(data);
                //this.loadGallery();
                this.showSuccess('Sikeres mentés');
            },
            (error) => {
                this.showError(error.message, 'Hiba a fájlfeltöltéskor');
            }
        );
        empForm.reset();

    }

    loadGallery() {
        this.pictureService.getGallery().subscribe((res) => {
            this.gallery = res;
            this.gallery.forEach((s) => {
                s.picture = "./assets/gallery/".concat(s.picture);
            });
            this.gallery.forEach(
                (s) => (s.smallPicture = "./assets/gallery/".concat(s.smallPicture))
            );
        });
    }

    deletePicture(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a képet?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.pictureService.deleteGalleryPicture(id).subscribe(
                    (data) => {
                        const du = this.gallery.find((a) => a.id === id);
                        const index = this.gallery.indexOf(du, 0);
                        if (index > -1) {
                            this.gallery.splice(index, 1);
                        }
                        this.showSuccess('Sikeres törlés');
                    },
                    (error) => {
                        this.showError(error.error.message, 'Sikertelen képtörlés');
                    }
                );
            }
        });
    }

    // confirmDeletingVideo(id: number) {
    //     if (confirm("Biztos, hogy törölni szeretnéd ezt a videót?")) {
    //         this.deleteVideo(id);
    //     }
    // }

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

    // isEnabled(form: boolean) {
    //     return (form && this.fileToUpload);
    // }

    showSuccess(message) {
        this.toastr.success(message);
    }

    showError(message, title) {
        this.toastr.error(message, title);
    }


    uploadVideo(empForm: any) {
        this.videoService.postVideoLink(this.newVideo).subscribe(
            (data) => {
                this.newVideo = new Video();
                this.videos.push(data);
            },
            (error) => {
            }
        );
        empForm.reset();
    }

    loadVideos() {
        this.videoService.getAllLinks().subscribe((res) => {
            this.videos = res;
            console.log(this.videos);
        });
    }

    deleteVideo(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a képet?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.videoService.deleteVideo(id).subscribe(
                    (data) => {
                        var du = this.videos.find((a) => a.id == id);
                        const index = this.videos.indexOf(du, 0);
                        if (index > -1) {
                            this.videos.splice(index, 1);
                        }
                        this.showSuccess('Sikeres törlés');
                    },
                    (error) => {
                        this.showError(error.error.message, 'Sikertelen képtörlés');
                    }
                );
            }
        });
    }

}
