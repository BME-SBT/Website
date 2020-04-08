import { Component, OnInit } from '@angular/core';
import { ScrollToService } from 'ng2-scroll-to-el';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-cover',
  templateUrl: './cover.component.html',
  styleUrls: ['./cover.component.css']
})
export class CoverComponent implements OnInit {

  constructor(private scrollService: ScrollToService,  private http: HttpClient) { }

  ngOnInit(): void {
    this.getWatts();
  }
  getWatts(): void {
    console.log('getWatts');
    const json = this.http
        .get('https://api.forecast.solar/estimate/watts/47.475498098/19.05333312/0/0/0.3')
        .subscribe(data => {
          console.log(data);
        });
  }

}
