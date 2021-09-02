import {
  Component,
  OnInit,
  Input,
  HostListener,
  ViewChild,
  ElementRef,
} from "@angular/core";
import { DomSanitizer, SafeStyle } from "@angular/platform-browser";

@Component({
  selector: "app-mileage",
  templateUrl: "./mileage.component.html",
  styleUrls: ["./mileage.component.css"],
})
export class MileageComponent implements OnInit {
  ngOnInit(): void {}
  @ViewChild("circle") circle: ElementRef;
  styleRotate: SafeStyle;
  angle: number;
  name = "mileage";
  @Input("velocity")
  set velocity(velocity) {
    this.setMileage(velocity);
  }
  constructor(private sanitizer: DomSanitizer) {}

  setMileage(velocity) {
    this.angle = velocity * 3 - 90; 
    if(this.angle > 90){
      this.angle += 180;
    }
    this.angle = Math.round(this.angle);
    this.styleRotate = this.sanitizer.bypassSecurityTrustStyle(
      `transform: rotate(${this.angle}deg)`
    );
  }
}
