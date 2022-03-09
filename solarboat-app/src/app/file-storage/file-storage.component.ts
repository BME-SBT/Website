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
import { ToastrService } from "ngx-toastr";
import { VideoService } from "../shared/video.service";
import { DomSanitizer } from "@angular/platform-browser";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmDialogComponent } from "../confirm-dialog/confirm-dialog.component";
import { Globals } from "../globals";
import { FileStorageService } from "../shared/file-storage.service";

@Component({
  selector: "app-file-storage",
  templateUrl: "./file-storage.component.html",
  styleUrls: ["./file-storage.component.css"],
})
export class FileStorageComponent implements OnInit {
  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService,
    private videoService: VideoService,
    private toastr: ToastrService,
    private dialog: MatDialog,
    private globals: Globals,
    private fileStorageService : FileStorageService
  ) {}
  @Output() filenames: String[];
  fileSelected: Boolean
  files: File[] = [];
  public authority: string;
  public roles: string[];

  ngOnInit(): void {
    this.checkAuth();

  }

  // handleFileInput(files: FileList) {
  //   for(var i = 0; i < files.length; i++){
  //     this.fileToUpload.push(files.item(i));
  //   }
// }

  onSelect(event) {
    if (this.files.length > 0) {
        this.files = [];
    }
    this.files.push(...event.addedFiles);
    this.fileSelected = true;
    console.log(this.files);
}
onRemove(event) {
  this.files.splice(this.files.indexOf(event), 1);
  // this.fileSelected = false;

}
uploadFile(event, empForm: any) {

  this.fileStorageService.postFiles(this.files).subscribe(
      (data) => {
          event.preventDefault();
          // this.newPicture = new GalleryPictureRequest();
          // this.gallery.push(data);
          console.log(data);
          this.showSuccess('Sikeres mentés');
      },
      (error) => {
          this.showError(error.message, 'Hiba a fájlfeltöltéskor');
      }
  );
  empForm.reset();

}
// loadFiles() {
//   this..getAllLinks().subscribe((res) => {
//       this.filenames = res;
//   });
// }

// deletePicture(id: number) {
//   const dialogRef = this.dialog.open(ConfirmDialogComponent, {
//       width: '300px',
//       data: 'Biztosan ki szeretnéd törölni a képet?'
//   });
//   dialogRef.afterClosed().subscribe(result => {
//       if (result) {
//           this.pictureService.deleteGalleryPicture(id).subscribe(
//               (data) => {
//                   const du = this.gallery.find((a) => a.id === id);
//                   const index = this.gallery.indexOf(du, 0);
//                   if (index > -1) {
//                       this.gallery.splice(index, 1);
//                   }
//                   this.showSuccess('Sikeres törlés');
//               },
//               (error) => {
//                   this.showError(error.error.message, 'Sikertelen képtörlés');
//               }
//           );
//       }
//   });
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
showSuccess(message) {
  this.toastr.success(message);
}

showError(message, title) {
  this.toastr.error(message, title);
}


}
