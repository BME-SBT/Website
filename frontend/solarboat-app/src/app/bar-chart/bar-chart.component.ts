import { Component, OnInit, Input } from "@angular/core";

@Component({
  selector: "app-bar-chart",
  templateUrl: "./bar-chart.component.html",
  styleUrls: ["./bar-chart.component.css"],
})
export class BarChartComponent implements OnInit {
  @Input() public data;
  constructor() {}

  ngOnInit(): void {}
}
