import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Globals } from "../globals";

@Injectable({
  providedIn: "root",
})
export class FileStorageService {
  constructor(private http: HttpClient, public globals: Globals) {}

  BASE_URL = this.globals.BASE_URL;
  postFiles(filesToUpload: File[]) {
    const formData: FormData = new FormData();

    for (let file of filesToUpload) {
      formData.append("files", file);
    }
    formData.append("path", "file");
    console.log(formData);
    return this.http.post(
      this.BASE_URL.concat("/api/file/uploadMultipleFiles"),
      formData
    );
  }
  // getFilenames(): Observable<String[]> {
  //   return <Observable<String[]>>this.http.get(this.BASE_URL + "/api/gallery");
  // }
 
}
