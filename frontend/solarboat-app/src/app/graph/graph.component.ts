import { Component, OnInit, Input } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BoatDataService } from "../boat-data.service";
import { DataGroup } from "../model/data-group";

@Component({
  selector: "app-graph",
  templateUrl: "./graph.component.html",
  styleUrls: ["./graph.component.css"],
})
export class GraphComponent implements OnInit {
  constructor(private http: HttpClient, private dataService: BoatDataService) {}
  ngOnInit() {}
  @Input() public data;
}
