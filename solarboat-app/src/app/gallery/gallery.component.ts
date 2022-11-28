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
import {Globals} from "../globals";
import { ActivatedRoute } from '@angular/router';
import { GalleryService } from "../shared/gallery.service";
import { Gallery } from "../model/gallery";
import { Image } from "../model/image";



// import AOS from 'aos';

@Component({
    selector: "app-gallery",
    templateUrl: "./gallery.component.html",
    styleUrls: ["./gallery.component.css"],
})
export class GalleryComponent implements OnInit {
    constructor(
        private pictureService: PictureService,
        // private router: Router,
        private tokenStorage: TokenStorageService,
        private videoService: VideoService,
        private toastr: ToastrService,
        private dialog: MatDialog,
        private globals: Globals,
        private route: ActivatedRoute,
        private galleryService: GalleryService
    ) {
    }

    @Output() gallery: Gallery = new Gallery();
    // newPicture: GalleryPictureRequest;
    newVideo: Video;
    coverImage : Image;
    failed = false;
    coverFailed = false;
    videoFailed = false;
    errorMessage = "";
    // pic = false;
    // smallPic = false;
    public authority: string;
    public roles: string[];
    public largeWidth: boolean;
    // fileToUpload: File;
    files: File[] = [];
    private galleryId;

    ngOnInit(): void {
        // AOS.init();
        this.checkAuth();
        const routeParams = this.route.snapshot.paramMap;
        this.galleryId = Number(routeParams.get('galleryId'));
        this.loadGallery()
        this.newVideo = new Video();
        this.largeWidth = (window.innerWidth < 768) ? false : true;
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
        this.largeWidth = (window.innerWidth < 768) ? false : true;
    }

    onSelect(event) {
        if (this.files.length > 0) {
            this.files = [];
        }
        this.files.push(...event.addedFiles);
    }

    onRemove(event) {
        this.files.splice(this.files.indexOf(event), 1);
    }

    uploadImages(event, empForm: any) {
        this.galleryService.postImages(this.galleryId, this.files).subscribe(
            (data) => {
                event.preventDefault();
                // do something, if upload success
                //this.uploadFileToActivity();
                this.gallery.images = data.images;
                //this.loadGallery();
                this.showSuccess('Sikeres mentés');
            },
            (error) => {
                this.showError(error.error.message, 'Hiba a fájlfeltöltéskor');
                event.preventDefault();

            }
        );
        empForm.reset();

    }

    loadGallery() {
        this.galleryService.getGalleryById(this.galleryId).subscribe((res) => {
            this.gallery = res;
            this.gallery.images.forEach((s) => {
                s.image = this.globals.IMG_ROUTE + "gallery/"+s.image;
                s.smallImage = this.globals.IMG_ROUTE + "gallery/"+s.smallImage
            });
        });
    }

    deletePicture(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a képet?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.galleryService.deleteImage(this.galleryId, id).subscribe(
                    (data) => {
                        const du = this.gallery.images.find((a) => a.id === id);
                        const index = this.gallery.images.indexOf(du, 0);
                        if (index > -1) {
                            this.gallery.images.splice(index, 1);
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
        this.galleryService.postNewVideo(this.galleryId, this.newVideo).subscribe(
            (data) => {
                this.showSuccess('Sikeres mentés');
                this.newVideo = new Video();
                this.gallery.videos.push(data);
            },
            (error) => {
                this.showError(error.error.message, 'Sikertelen mentés');

            }
        );
        empForm.reset();
    }


    deleteVideo(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            width: '300px',
            data: 'Biztosan ki szeretnéd törölni a videót?'
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.galleryService.deleteVideo(this.galleryId, id).subscribe(
                    (data) => {
                        var du = this.gallery.videos.find((a) => a.id == id);
                        const index = this.gallery.videos.indexOf(du, 0);
                        if (index > -1) {
                            this.gallery.videos.splice(index, 1);
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
    setCoverImage(empForm : any){
        this.galleryService.patchCoverImage(this.galleryId, this.coverImage).subscribe(
            (data) => {
                this.showSuccess('Sikeres mentés');

                this.gallery.coverImage = data.coverImage
            },
            (error) => {
                this.showError(error.error.message, 'Sikertelen mentés');

            }
        );
        empForm.reset();
    }

    

}
