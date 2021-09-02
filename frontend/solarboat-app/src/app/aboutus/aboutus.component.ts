import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-aboutus',
  templateUrl: './aboutus.component.html',
  styleUrls: ['./aboutus.component.css']
})
export class AboutusComponent implements OnInit {
  constructor() { }
  ngOnInit(): void {
    const elements = document.querySelectorAll('.animate-me');
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.intersectionRatio > 0) {
          entry.target.classList.add('animated');
        }
      });
    });
    elements.forEach(el => {
      observer.observe(el);
    });
  }
}
