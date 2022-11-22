import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { GalleryPicture } from "../model/gallery-picture";
import { GalleryPictureRequest } from "../model/gallery-picture-request";
import { Globals } from "../globals";
import { Gallery } from "../model/gallery";
import { Video } from "../model/video";
import { Image } from "../model/image";
@Injectable({
  providedIn: "root",
})
export class GalleryService {
  constructor(private http: HttpClient, public globals: Globals) {}

  BASE_URL = this.globals.BASE_URL + "/api/gallery";

  getGallery(): Observable<Gallery[]> {
    return <Observable<Gallery[]>>this.http.get(this.BASE_URL);
  }
  getGalleryById(imageGroupId: Number): Observable<Gallery> {
    return <Observable<Gallery>>(
      this.http.get(this.BASE_URL + "/" + imageGroupId)
    );
  }
  postImages(imageGroupId: number, filesToUpload: File[]): Observable<Gallery> {
    const formData: FormData = new FormData();

    for (let file of filesToUpload) {
      formData.append("files", file);
    }
    formData.append("imageGroupId", imageGroupId.toString());
    return <Observable<Gallery>>(
      this.http.post(this.BASE_URL.concat("/images"), formData)
    );
  }
  deleteImage(imageGroupId: number, imageId: number) {
    return this.http.delete(this.BASE_URL + "/" + imageGroupId + "/" + imageId);
  }
  deleteImageGroup(imageGroupId: number) {
    return this.http.delete(this.BASE_URL + "/" + imageGroupId);
  }
  postNewImageGroup(gallery: Gallery): Observable<any> {
    return this.http.post(this.BASE_URL, gallery);
  }
  deleteVideo(imageGroupId: number, videoId: number) {
    return this.http.delete(
      this.BASE_URL +
        "/" +
        imageGroupId +
        "/video/" +
        imageGroupId +
        "/" +
        videoId
    );
  }
  postNewVideo(imageGroupId: number, video: Video): Observable<Video> {
    return <Observable<Video>>(
      this.http.post(this.BASE_URL + "/video/" + imageGroupId, video)
    );
  }
  patchCoverImage(
    imageGroupId: number,
    image: Image
  ): Observable<Gallery> {
    return <Observable<Gallery>>(
      this.http.patch(
        this.BASE_URL + "/cover/" + imageGroupId, image
      )
    );
  }

 
}
