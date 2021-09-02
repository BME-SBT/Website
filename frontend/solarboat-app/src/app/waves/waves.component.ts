import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-waves',
  templateUrl: './waves.component.html',
  styleUrls: ['./waves.component.css']
})
export class WavesComponent implements OnInit {

  constructor() { }
  @Input("text")
  text;

  ngOnInit(): void {
  }

}
