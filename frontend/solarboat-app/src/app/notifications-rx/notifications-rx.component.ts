import {Component, Injectable} from '@angular/core';
import {RxStomp} from "@stomp/rx-stomp";
import * as SockJS from 'sockjs-client';
import {map} from "rxjs/operators";
import {BoatDataComponent} from "../boat-data/boat-data.component"
import {Globals} from '../globals';

@Injectable({
  providedIn: 'root'
})
@Component({
  selector: 'app-notifications-rx',
  templateUrl: './notifications-rx.component.html'
})
export class NotificationsRxComponent {

  constructor(private boatDataComponent: BoatDataComponent, public globals: Globals){}

  private client: RxStomp;
  public notifications: string[] = [];
  private BASE_URL = this.globals.BASE_URL;

  connect() {
    if (!this.client || this.client.connected) {
      this.client = new RxStomp();
      this.client.configure({
        webSocketFactory: () => new SockJS(this.BASE_URL + '/notifications'),
        debug: (msg: string) => console.log(msg)
      });
      this.client.activate();

      this.watchForNotifications();

      console.info('connected!');
    }
  }

  private watchForNotifications() {
    this.client.watch('/user/notification/item')
      .pipe(
        map((response) => {
          const data = JSON.parse(response.body);
          this.boatDataComponent.setGraphData(data);
          return data;
        }))
      .subscribe((notification: string) => this.notifications.push(notification));
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

}
