import { Injectable } from '@angular/core';
import {RxStomp} from "@stomp/rx-stomp";
import {Subject} from "rxjs/Subject"
import * as SockJS from 'sockjs-client';
import {map} from "rxjs/operators";
import { BoatData } from './model/boat-data';
import {BoatDataComponent} from "./boat-data/boat-data.component"

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(){}

  private client: RxStomp;
  private dataCallback = new Subject<string>(); // Source
  dataCallback$ = this.dataCallback.asObservable(); // Stream
  private activityCallback = new Subject<string>(); // Source
  activityCallback$ = this.activityCallback.asObservable(); // Stream

  connect() {
    if (!this.client || this.client.connected) {
      this.client = new RxStomp();
      this.client.configure({
        webSocketFactory: () => new SockJS('http://localhost:8080/notifications'),
        debug: (msg: string) => console.log(msg)
      });
      this.client.activate();

      this.watchForData();
      this.watchForActivity();


      console.info('connected!');
    }
  }

  private watchForData() {
    this.client.watch('/user/notification/data')
      .pipe(
        map((response) => {
        const data = JSON.parse(response.body);
        console.log(data);
         // this.eventCallback.next(data);
        return data;
        }))
      .subscribe((notification) =>  this.dataCallback.next(notification));
  }
  private watchForActivity() {
    this.client.watch('/user/notification/activity')
      .pipe(
        map((response) => {
          const data = JSON.parse(response.body);
          console.log(data);
         // this.eventCallback.next(data);
          return data;
        })).subscribe((notification) =>  this.activityCallback.next(notification));
  }

  disconnect() {
    if (this.client && this.client.connected) {
      this.client.deactivate();
      this.client = null;
      console.info("disconnected :-/");
    }
  }

  startListening() {
    if (this.client && this.client.connected) {
      this.client.publish({destination: '/swns/start'});
    }
  }

  stopListening() {
    if (this.client && this.client.connected) {
      this.client.publish({destination: '/swns/stop'});
    }
  }
  isConnected(){
    return this.client.connected;
  }
}
