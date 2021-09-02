import { Component, OnInit, Input, HostListener, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';

@Component({
  selector: 'app-compass',
  templateUrl: './compass.component.html',
  styleUrls: ['./compass.component.css']
})
export class CompassComponent {

  @ViewChild('circle') circle: ElementRef;
  styleRotate: SafeStyle;
  angle: number;
  name = 'Compass';
  @Input('compass') 
  set compass(compass){
        this.setCompass(compass.x, compass.y);
  }  
  constructor(private sanitizer: DomSanitizer){ }
    setCompass(x, y){
        this.angle = Math.atan2(y, x) * (180 / Math.PI); 
        if (this.angle < 0) {
          this.angle = 360 + this.angle;
        }
        this.angle = Math.round(this.angle);
        this.styleRotate = this.sanitizer.bypassSecurityTrustStyle(`transform: rotate(${this.angle}deg)`);           
    }
}
