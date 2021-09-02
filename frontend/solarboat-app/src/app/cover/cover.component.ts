import { Component, OnInit } from '@angular/core';
import { ScrollToService } from 'ng2-scroll-to-el';
import {Observable, Subscription} from 'rxjs';
import 'rxjs/add/observable/interval';
import {TranslateService} from '@ngx-translate/core';
import {SolarForecastService} from '../shared/solar-forecast.service';
import {SessionStorageService} from '../shared/session-storage.service';

import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'app-cover',
  templateUrl: './cover.component.html',
  styleUrls: ['./cover.component.css'],
  animations: [
    trigger('flyIn1', [
      transition(':enter', [
        style({ transform: 'translateX(30px)', opacity: 0}),
        animate('500ms 100ms ease-in', style({ transform: 'translateX(0)', opacity: 1 }))
      ])
    ]),
    trigger('flyIn2', [
      state('in', style({ transform: 'translateX(0)' })),
      transition('void => *', [
        style({ transform: 'translateX(30px)', opacity: 0}),
        animate('500ms 300ms ease-in' )
      ])
    ]),
    trigger('flyIn3', [
      state('in', style({ transform: 'translateX(0)' })),
      transition('void => *', [
        style({ transform: 'translateX(-30px)', opacity: 0}),
        animate('500ms 600ms ease-in' )
      ])
    ])
  ]
})
export class CoverComponent implements OnInit {
  constructor(private scrollService: ScrollToService, public translate: TranslateService,
              private solarApiService: SolarForecastService, private sessionStorage: SessionStorageService) {}

  public  sub: Subscription;
  public watts;


  ngOnInit(): void {
    this.setWatts();
    this.sub = Observable.interval(6000000)
        .subscribe((val) => {
          this.setWatts();
        });
  }
  public setWatts() {
    const storageData = this.sessionStorage.getItem('solarpower');
    if (storageData != null) {
      this.watts = this.getWattsFromJsonByDate(storageData, this.getCurrentDateAndHour());
    } else {
      this.solarApiService.getCurrentSolarData()
          .subscribe((data) => {
            this.watts = this.getWattsFromJsonByDate(data, this.getCurrentDateAndHour());
            this.sessionStorage.setItem('solarpower', data);
          });
    }
  }

  public getCurrentDateAndHour() {
    const date = new Date();
    const month = 1 + date.getMonth();
    // formatum: dátum + óra, "2020-04-14 05"
    return date.getFullYear() + '-' +
          this.addZero(month) + '-' +
          this.addZero(date.getDate()) + ' ' +
          this.addZero(date.getHours());
  }
  private  addZero(i: number) {
    if (i < 10) {
      return '0' + i;
    }
    return i;
  }

  public getWattsFromJsonByDate(data, currentDateAndHour: string) {
    // console.log(data);
    // console.log(currentDateAndHour);
    const rawData = data;
    for (const [key, value] of Object.entries(rawData.result)) {
      // console.log(`${key}: ${value}`);
      if ( key.substring(0, 13) === currentDateAndHour) {
         return value;
      }
    }
    return 0;
  }

}
