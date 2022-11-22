import { PictureService } from "../shared/picture.service";
import {
  Component,
  OnInit,
  Output,
  HostListener,
  Pipe,
  PipeTransform,
} from "@angular/core";
import { Router } from "@angular/router";
import { TokenStorageService } from "../auth/token-storage.service";
import { GalleryPicture } from "../model/gallery-picture";
import { GalleryPictureRequest } from "../model/gallery-picture-request";
import { ToastrService } from "ngx-toastr";
import { Video } from "../model/video";
import { VideoService } from "../shared/video.service";
import { DomSanitizer } from "@angular/platform-browser";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmDialogComponent } from "../confirm-dialog/confirm-dialog.component";
import { Globals } from "../globals";
import { Gallery } from "../model/gallery";
import { Image } from "../model/image";
import { GalleryService } from "../shared/gallery.service";

@Component({
  selector: "app-gallery-main-page",
  templateUrl: "./gallery-main-page.component.html",
  styleUrls: ["./gallery-main-page.component.css"],
})
export class GalleryMainPageComponent implements OnInit {
  constructor(
    private galleryService: GalleryService,
    private router: Router,
    private tokenStorage: TokenStorageService,
    private videoService: VideoService,
    private toastr: ToastrService,
    private dialog: MatDialog,
    private globals: Globals
  ) {}

  @Output() gallery: Gallery[];
  newGroup: Gallery = new Gallery();
  newImages: File[];
  @Output() videos: Video[];
  newVideo: Video = new Video();
  failed = false;
  videoFailed = false;
  errorMessage = "";
  picturesSelected = false;
  public authority: string;
  public roles: string[];
  public largeWidth: boolean;
  fileToUpload: File;
  files: File[] = [];
  maxDate: Date;

  ngOnInit(): void {
    const currentYear = new Date().getFullYear();
    this.maxDate = new Date(currentYear, 12, 31);
    // AOS.init();
    this.checkAuth();
    this.loadGallery();
    // this.newPicture = new GalleryPictureRequest();
    this.largeWidth = window.innerWidth < 768 ? false : true;
  }

  @HostListener("window:resize", ["$event"])
  onResize(event) {
    this.largeWidth = window.innerWidth < 768 ? false : true;
    console.log(this.largeWidth);
  }

  // onSelect(event) {
  //   if (this.files.length > 0) {
  //     this.files = [];
  //   }
  //   this.files.push(...event.addedFiles);
  //   this.newImages = this.files;
  //   this.picturesSelected = true;
  // }
  onRemove(event) {
    this.files.splice(this.files.indexOf(event), 1);
    if (this.files.length == 0) this.picturesSelected = false;
  }

  loadGallery() {
    this.galleryService.getGallery().subscribe((res) => {
      console.log(res);
      this.gallery = res;
      this.gallery.forEach((s) => {
        if (s.coverImage == null) {
          s.coverImage = new Image();
          s.coverImage.image = this.globals.IMG_ROUTE + "gallery/logo.png";
          s.coverImage.smallImage = this.globals.IMG_ROUTE + "gallery/logo.png";
        } else {
          s.coverImage.image =
            this.globals.IMG_ROUTE + "gallery/" + s.coverImage.image;
          s.coverImage.smallImage =
            this.globals.IMG_ROUTE + "gallery/" + s.coverImage.smallImage;
        }
      });
    });
    console.log(this.gallery);
  }
  uploadImageGroup(event:Event, empForm: any) {
    if(this.newGroup.date){
      this.newGroup.date = this.globals.formatDate(this.newGroup.date);
    }
    this.galleryService.postNewImageGroup(this.newGroup).subscribe(
      (data) => {
        event.preventDefault();
        data.coverImage = new Image();
        data.coverImage.image = this.globals.IMG_ROUTE + "image/logo.png";
        data.coverImage.smallImage = this.globals.IMG_ROUTE + "image/logo.png";
        // do something, if upload success
        //this.uploadFileToActivity();
        // this.newGroup = new GalleryPictureRequest();
        this.gallery.push(data);
        //this.loadGallery();
        this.showSuccess("Sikeres mentés");
      },
      (error) => {
        this.showError(error.message, "Hiba a fájlfeltöltéskor");
      }
    );
    empForm.reset();
  }

  showSuccess(message) {
    this.toastr.success(message);
  }

  showError(message, title) {
    this.toastr.error(message, title);
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
  deleteImageGroup(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '300px',
        data: 'Biztosan ki szeretnéd törölni a csoportot?'
    });
    dialogRef.afterClosed().subscribe(result => {
        if (result) {
            this.galleryService.deleteImageGroup(id).subscribe(
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
}
